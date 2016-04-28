package ClientSide;

import Others.InterfaceContestant;
import Others.InterfaceContestantsBench;
import Others.InterfaceGeneralInformationRepository;
import Others.InterfacePlayground;
import Others.InterfaceRefereeSite;

/**
 * This is active class Contestant which implements the InterfaceContestant.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class Contestant extends Thread implements Comparable<InterfaceContestant>, InterfaceContestant {

    /**
     * Bench interface to be used
     */
    private final InterfaceContestantsBench bench;

    /**
     * Playground interface to be used
     */
    private final InterfacePlayground playground;

    /**
     * RefereeSite interface to be used
     */
    private final InterfaceRefereeSite refereeSite;

    /**
     * General Information Repository interface to be used
     */
    private final InterfaceGeneralInformationRepository informationRepository;

    /**
     * Current Contestant state
     */
    private ContestantState state;

    /**
     * Current Contestant strength
     */
    private int strength;

    /**
     * Current contestant team
     */
    private int team;

    /**
     * Current contestant id
     */
    private int id;

    /**
     * Creates a Contestant instantiation for running in a distributed
     * environment.
     *
     * @param name Name of the contestant
     * @param team Team of the contestant
     * @param id Id of the contestant
     * @param strength Strength of the contestant
     */
    public Contestant(String name, int team, int id, int strength) {
        super(name);

        state = ContestantState.SEAT_AT_THE_BENCH;

        this.team = team;
        this.id = id;
        this.strength = strength;

        bench = new ContestantsBenchStub(team);
        playground = new PlaygroundStub();
        refereeSite = new RefereeSiteStub();
        informationRepository = new GeneralInformationRepositoryStub();
    }

    @Override
    public ContestantState getContestantState() {
        return state;
    }

    @Override
    public void setContestantState(ContestantState state) {
        this.state = state;
    }

    @Override
    public int getContestantTeam() {
        return team;
    }

    @Override
    public void setContestantTeam(int team) {
        this.team = team;
    }

    @Override
    public int getContestantId() {
        return id;
    }

    @Override
    public void setContestantId(int id) {
        this.id = id;
    }

    @Override
    public int getContestantStrength() {
        return strength;
    }

    @Override
    public void setContestantStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        informationRepository.updateContestant();
        bench.addContestant();

        while (!refereeSite.hasMatchEnded()) {
            switch (state) {
                case SEAT_AT_THE_BENCH:
                    followCoachAdvice();
                    break;
                case STAND_IN_POSITION:
                    getReady();
                    break;
                case DO_YOUR_BEST:
                    pullTheRope();
                    seatDown();
                    break;
            }
        }
    }

    /**
     * Contestant checks if is selected to the game. If so, goes to the
     * playground.
     */
    private void followCoachAdvice() {
        bench.getContestant();

        if (!refereeSite.hasMatchEnded()) {
            playground.addContestant();
        }
    }

    /**
     * Contestant gets ready. Changes the Contestant state to DO_YOUR_BEST
     */
    private void getReady() {
        setContestantState(ContestantState.DO_YOUR_BEST);
        informationRepository.updateContestant();
        informationRepository.printLineUpdate();
    }

    /**
     * Contestant pulls the rope
     */
    private void pullTheRope() {
        playground.pullRope();
    }

    /**
     * If contestant was playing moves to his bench and changes his state to
     * SEAT_AT_THE_BENCH
     */
    private void seatDown() {
        playground.getContestant();
        bench.addContestant();
    }

    @Override
    public int compareTo(InterfaceContestant contestant) {
        return getContestantId() - contestant.getContestantId();
    }

}
