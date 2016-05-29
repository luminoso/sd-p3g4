package ClientSide.Coach;

import Interfaces.InterfaceContestantsBench;
import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfacePlayground;
import Interfaces.InterfaceRefereeSite;
import Others.CoachStrategy;
import Others.InterfaceCoach;
import Others.Tuple;
import Others.VectorTimestamp;
import RopeGame.Constants;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is active class Coach which implements the InterfaceCoach
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
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
        this.vt = null;
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
            vt.increment();
            informationRepository.updateCoach(team, state.getId(), vt.clone());

            vt.increment();
            vt.update(bench.waitForNextTrial(vt.clone()));

            while (!((BooleanSupplier) () -> {
                vt.increment();
                Tuple<VectorTimestamp, Boolean> hasMatchEnded = null;
                try {
                    hasMatchEnded = refereeSite.hasMatchEnded(vt.clone());
                } catch (RemoteException ex) {
                    Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
                vt.update(hasMatchEnded.getFirst());
                return hasMatchEnded.getSecond();
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
            Logger.getLogger(Coach.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    /**
     * The coach decides which players are selected for next round and updates
     * selected contestants array at the bench
     */
    private void callContestants() throws RemoteException {
        Set<Integer> pickedContestants = this.strategy.pickTeam(bench, refereeSite);

        vt.increment();
        vt.update(bench.setSelectedContestants(pickedContestants, vt.clone()));

        vt.increment();
        vt.update(playground.checkTeamPlacement(team, vt.clone()));
    }

    /**
     * Informs the Referee and watches the trial
     */
    private void informReferee() throws RemoteException {
        vt.increment();
        vt.update(refereeSite.informReferee(vt.clone()));

        vt.increment();
        vt.update(playground.watchTrial(vt.clone()));
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
        vt.update(bench.waitForNextTrial(vt.clone()));
    }

    @Override
    public int compareTo(InterfaceCoach coach) {
        return getCoachTeam() - coach.getCoachTeam();
    }

}