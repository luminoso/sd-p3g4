package ClientSide.Coach;

import Others.CoachStrategy;
import Others.InterfaceCoach;
import Interfaces.InterfaceContestantsBench;
import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfacePlayground;
import Interfaces.InterfaceRefereeSite;
import Others.Tuple;
import RopeGame.Constants;
import java.util.Set;

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

    /**
     * Creates a Coach instantiation for running in a distributed environment
     *
     * @param name of the coach
     * @param team of the coach
     * @param strategy to be used by the coach
     */
    public Coach(String name, int team, CoachStrategy strategy) {
        super(name);                    // giving name to thread

        // initial state
        state = CoachState.WAIT_FOR_REFEREE_COMMAND;

        this.team = team;               // team assignement
        this.strategy = strategy;       // team picking strategy

        bench = new ContestantsBenchStub(team);
        refereeSite = new RefereeSiteStub();
        playground = new PlaygroundStub();
        informationRepository = new GeneralInformationRepositoryStub();
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
        informationRepository.updateCoach();
        bench.waitForNextTrial();

        while (!refereeSite.hasMatchEnded()) {
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
    }

    /**
     * The coach decides which players are selected for next round and updates
     * selected contestants array at the bench
     */
    private void callContestants() {
        Set<Integer> pickedContestants = this.strategy.pickTeam(bench, refereeSite);
        bench.setSelectedContestants(pickedContestants);
        playground.checkTeamPlacement();
    }

    /**
     * Informs the Referee and watches the trial
     */
    private void informReferee() {
        refereeSite.informReferee();
        playground.watchTrial();
    }

    /**
     * The coach updates his players which have played and game and updates
     * their strength
     */
    private void reviewNotes() {
        Set<Tuple<Integer, Integer>> contestants = bench.getBench();
        Set<Integer> selectedContestants = bench.getSelectedContestants();

        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; i++) {
            if (selectedContestants.contains(i)) {
                bench.updateContestantStrength(i, -1);
            } else {
                bench.updateContestantStrength(i, 1);
            }
        }

        bench.waitForNextTrial();
    }

    @Override
    public int compareTo(InterfaceCoach coach) {
        return getCoachTeam() - coach.getCoachTeam();
    }

}
