package clientSide;

import interfaces.InterfaceContestantsBench;
import interfaces.InterfaceGeneralInformationRepository;
import interfaces.InterfacePlayground;
import interfaces.InterfaceRefereeSite;
import others.CoachState;
import others.CoachStrategy;
import others.InterfaceCoach;
import others.TrialScore;
import others.Tuple;
import others.VectorTimestamp;
import others.Constants;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is active class Coach which implements the InterfaceCoach
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class Coach extends Thread implements Comparable<InterfaceCoach>, InterfaceCoach {

    private final InterfaceContestantsBench bench; // bench interface to be used
    private final InterfaceRefereeSite refereeSite; // refereeSite interface to be used
    private final InterfacePlayground playground;  // playground interface to be used
    private final InterfaceGeneralInformationRepository informationRepository; // general Information Repository interface to be used

    // coach definition
    private CoachState state;
    private int team;
    private CoachStrategy strategy;
    private VectorTimestamp vt;

    /**
     * Creates a Coach instantiation for running in a distributed environment
     *
     * @param name of the coach
     * @param team of the coach
     * @param strategy to be used by the coach
     * @param bench interface to be used
     * @param refereeSite interface to be used
     * @param playground interface to be used
     * @param informationRepository interface to be used
     */
    public Coach(String name, int team, CoachStrategy strategy,
            InterfaceContestantsBench bench,
            InterfaceRefereeSite refereeSite,
            InterfacePlayground playground,
            InterfaceGeneralInformationRepository informationRepository) {

        super(name);                    // giving name to thread

        // initial state
        state = CoachState.WAIT_FOR_REFEREE_COMMAND;

        this.team = team;               // team assignement
        this.strategy = strategy;       // team picking strategy

        this.bench = bench;
        this.refereeSite = refereeSite;
        this.playground = playground;
        this.informationRepository = informationRepository;
        this.vt = new VectorTimestamp(Constants.VECTOR_TIMESTAMP_SIZE, 1 + (team-1) * (Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH + 1));
    }

    @Override
    public CoachState getCoachState() {
        return state;
    }

    @Override
    public void setCoachState(CoachState state) {
        this.state = state;
    }

    @Override
    public int getCoachTeam() {
        return team;
    }

    @Override
    public void setCoachTeam(int team) {
        this.team = team;
    }

    @Override
    public CoachStrategy getCoachStrategy() {
        return strategy;
    }

    @Override
    public void setCoachStrategy(CoachStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void run() {

        try {
            informationRepository.updateCoach(team, state.getId(), vt.clone());
            Tuple<VectorTimestamp, Integer> waitForNextTrial = bench.waitForNextTrial(team, state.getId(), vt.clone());
            waitForNextTrial.getFirst();

            while (!((BooleanSupplier) () -> {
                vt.increment();
                boolean hasMatchEnded = false;
                try {
                    refereeSite.hasMatchEnded();
                } catch (RemoteException ex) {
                    Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
                return hasMatchEnded;
            }).getAsBoolean()) {
                switch (state) {
                    case WAIT_FOR_REFEREE_COMMAND:
                        callContestants();
                        break;
                    case ASSEMBLE_TEAM:
                        informReferee();
                        break;
                    case WATCH_TRIAL:
                        reviewNotes();
                        break;
                }
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The coach decides which players are selected for next round and updates
     * selected contestants array at the bench
     */
    private void callContestants() throws RemoteException {
        Set<Integer> pickedContestants = this.strategy.pickTeam(
                ((Supplier<Set<Tuple<Integer, Integer>>>) () -> {
                    Tuple<VectorTimestamp, Set<Tuple<Integer, Integer>>> getbenchres = null;
                    try {
                        vt.increment();
                        getbenchres = bench.getBench(team, vt.clone());
                    } catch (RemoteException ex) {
                        Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    vt.update(getbenchres.getFirst());
                    return getbenchres.getSecond();

                }).get(),
                ((Supplier<Set<Integer>>) () -> {
                    Tuple<VectorTimestamp, Set<Integer>> selectedContestants = null;
                    try {
                        vt.increment();
                        selectedContestants = bench.getSelectedContestants(team, vt.clone());
                    } catch (RemoteException ex) {
                        Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    vt.update(selectedContestants.getFirst());
                    return selectedContestants.getSecond();

                }).get(),
                ((Supplier<List<TrialScore>>) () -> {
                    Tuple<VectorTimestamp, List<TrialScore>> trialPoints = null;
                    try {
                        vt.increment();
                        trialPoints = refereeSite.getTrialPoints(vt.clone());
                    } catch (RemoteException ex) {
                        Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    vt.update(trialPoints.getFirst());
                    return trialPoints.getSecond();

                }).get());

        vt.increment();
        vt.update(bench.setSelectedContestants(team, pickedContestants, vt.clone()));

        vt.increment();
        Tuple<VectorTimestamp, Integer> checkTeamPlacement = playground.checkTeamPlacement(team, vt.clone());
        vt.update(checkTeamPlacement.getFirst());
        state = CoachState.getStateById(checkTeamPlacement.getSecond());
    }

    /**
     * Informs the Referee and watches the trial
     */
    private void informReferee() throws RemoteException {
        vt.increment();
        vt.update(refereeSite.informReferee(vt.clone()));

        vt.increment();
        Tuple<VectorTimestamp, Integer> watchTrial = playground.watchTrial(team, vt.clone());
        vt.update(watchTrial.getFirst());
        state = CoachState.getStateById(watchTrial.getSecond());

    }

    /**
     * The coach updates his players which have played and game and updates
     * their strength
     */
    private void reviewNotes() throws RemoteException {
        vt.increment();
        Tuple<VectorTimestamp, Set<Tuple<Integer, Integer>>> benchT = bench.getBench(team, vt.clone());
        vt.update(benchT.getFirst());

        vt.increment();
        Tuple<VectorTimestamp, Set<Integer>> selectedContestantsT = bench.getSelectedContestants(team, vt.clone());
        vt.update(selectedContestantsT.getFirst());
        Set<Integer> selectedContestants = selectedContestantsT.getSecond();

        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; i++) {
            if (selectedContestants.contains(i)) {
                vt.increment();
                vt.update(bench.updateContestantStrength(i, team, -1, vt.clone()));
            } else {
                vt.increment();
                vt.update(bench.updateContestantStrength(i, team, 1, vt.clone()));
            }
        }

        vt.increment();
        Tuple<VectorTimestamp, Integer> waitForNextTrial = bench.waitForNextTrial(team, state.getId(), vt.clone());
        vt.update(waitForNextTrial.getFirst());
        state = CoachState.getStateById(waitForNextTrial.getSecond());
    }

    @Override
    public int compareTo(InterfaceCoach coach) {
        return getCoachTeam() - coach.getCoachTeam();
    }

}
