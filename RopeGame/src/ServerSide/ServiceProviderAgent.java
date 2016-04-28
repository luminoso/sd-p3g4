package ServerSide;

import ClientSide.Contestant;
import Communication.Message;
import Others.CoachStrategy;
import Others.InterfaceCoach;
import Others.InterfaceCoach.CoachState;
import Others.InterfaceContestant;
import Others.InterfaceContestant.ContestantState;
import Others.InterfaceReferee;
import Others.InterfaceReferee.RefereeState;

/**
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class ServiceProviderAgent extends Thread implements InterfaceCoach,
        InterfaceContestant,
        InterfaceReferee,
        Comparable<InterfaceContestant> {

    /**
     *
     */
    private final ServerCom sconi;

    /**
     *
     */
    private static int serviceProviderAgentId = 0;

    /**
     *
     */
    private final InterfaceServer servInterface;

    /**
     *
     */
    private Enum state;

    /**
     *
     */
    private int strength;

    /**
     *
     */
    private int team;

    /**
     *
     */
    private int contestantId;

    /**
     *
     */
    private CoachStrategy strategy;

    /**
     * Initialisation of the server interface,
     *
     * @param sconi connection to be dispatched
     * @param servInterface server interface to be used
     */
    ServiceProviderAgent(ServerCom sconi,
            InterfaceServer servInterface) {

        super(Integer.toString(serviceProviderAgentId++));
        this.sconi = sconi;
        this.servInterface = servInterface;

        this.state = null;
        this.strength = 0;
        this.team = 0;
        this.contestantId = 0;
        this.strategy = null;
    }

    @Override
    public void run() {
        Message inMessage = null,
                outMessage = null;

        Thread.currentThread().setName("SPA-" + Integer.toString(serviceProviderAgentId++));

        inMessage = (Message) sconi.readObject();

        // TODO: validate message
        this.state = inMessage.getState();
        this.team = inMessage.getTeam();
        this.contestantId = inMessage.getContestantId();
        this.strength = inMessage.getStrength();

        try {
            outMessage = servInterface.processAndReply(inMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sconi.writeObject(outMessage);
        sconi.close();
    }

    // Coach methods
    @Override
    public CoachState getCoachState() {
        return (CoachState) state;
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

    // Contestant methods
    @Override
    public int getContestantId() {
        return contestantId;
    }

    @Override
    public void setContestantId(int id) {
        contestantId = id;
    }

    @Override
    public ContestantState getContestantState() {
        return (ContestantState) state;
    }

    @Override
    public void setContestantState(Contestant.ContestantState state) {
        this.state = state;
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
    public int getContestantTeam() {
        return team;
    }

    @Override
    public void setContestantTeam(int team) {
        this.team = team;
    }

    // Referee methods
    @Override
    public RefereeState getRefereeState() {
        return (RefereeState) state;
    }

    @Override
    public void setRefereeState(RefereeState state) {
        this.state = state;
    }

    @Override
    public int compareTo(InterfaceContestant contestant) {
        return getContestantId() - contestant.getContestantId();
    }

}
