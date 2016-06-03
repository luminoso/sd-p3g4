package clientSide;

import interfaces.InterfaceContestantsBench;
import interfaces.InterfaceGeneralInformationRepository;
import interfaces.InterfacePlayground;
import interfaces.InterfaceRefereeSite;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import others.Constants;
import others.ContestantState;
import others.InterfaceContestant;
import others.Triple;
import others.Tuple;
import others.VectorTimestamp;

/**
 * This is active class Contestant which implements the InterfaceContestant
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class Contestant extends Thread implements Comparable<InterfaceContestant>, InterfaceContestant {

    private final InterfaceContestantsBench bench; // bench interface to be used
    private final InterfacePlayground playground; // playground interface to be used
    private final InterfaceRefereeSite refereeSite; // refereeSite interface to be used
    private final InterfaceGeneralInformationRepository informationRepository; // general Information Repository interface to be used

    // contestant definition
    private ContestantState state;
    private int strength;
    private int team;
    private int id;
    private VectorTimestamp vt;

    /**
     * Creates a Contestant instantiation for running in a distributed
     * environment
     *
     * @param name of the contestant
     * @param team of the contestant
     * @param id of the contestant
     * @param strength of the contestant
     * @param bench interface to be used
     * @param playground interface to be used
     * @param refereeSite interface to be used
     * @param informationRepository interface to be used
     */
    public Contestant(String name, int team, int id, int strength,
            InterfaceContestantsBench bench,
            InterfacePlayground playground,
            InterfaceRefereeSite refereeSite,
            InterfaceGeneralInformationRepository informationRepository) {

        super(name);

        state = ContestantState.SEAT_AT_THE_BENCH;

        this.team = team;
        this.id = id;
        this.strength = strength;

        this.bench = bench;
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.informationRepository = informationRepository;
        this.vt = new VectorTimestamp(Constants.VECTOR_TIMESTAMP_SIZE,
        1 + (team-1)*(Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH + 1) + id);
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

    @Override
    public void run() {
        try {
            vt.increment();
            informationRepository.updateContestant(id, team, state.getId(), strength, vt.clone());

            vt.increment();
            Triple<VectorTimestamp, Integer, Integer> addContestant = bench.addContestant(id, team, state.getId(), strength, vt.clone());
            vt.update(addContestant.getFirst());
            //addContestant.getSecond() and addContestant.getThird() not needed, since there are no updates

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
        } catch (RemoteException ex) {
            Logger.getLogger(Contestant.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    /**
     * Contestant checks if is selected to the game. If so, goes to the
     * playground
     */
    private void followCoachAdvice() throws RemoteException {

        bench.getContestant(id, team);

        if (!refereeSite.hasMatchEnded()) {

            vt.increment();
            Tuple<VectorTimestamp, Integer> addContestant = playground.addContestant(id, team, state.getId(), strength, vt.clone());
            vt.update(addContestant.getFirst());

            state = ContestantState.getStateById(addContestant.getSecond());
        }
    }

    /**
     * Contestant gets ready. Changes the Contestant state to DO_YOUR_BEST
     */
    private void getReady() throws RemoteException {
        setContestantState(ContestantState.DO_YOUR_BEST);

        vt.increment();
        informationRepository.updateContestant(id, team, state.getId(), strength, vt.clone());
    }

    /**
     * Contestant pulls the rope
     */
    private void pullTheRope() throws RemoteException {
        playground.pullRope();
    }

    /**
     * If contestant was playing moves to his bench and changes his state to
     * SEAT_AT_THE_BENCH
     */
    private void seatDown() throws RemoteException {
        vt.increment();
        vt.update(playground.getContestant(id, team, vt.clone()));

        vt.increment();
        Triple<VectorTimestamp, Integer, Integer> addContestant = bench.addContestant(id, team, state.getId(), strength, vt.clone());
        vt.update(addContestant.getFirst());

        state = ContestantState.getStateById(addContestant.getSecond());
        strength = addContestant.getThird();
    }

    @Override
    public int compareTo(InterfaceContestant contestant) {
        return getContestantId() - contestant.getContestantId();
    }

}
