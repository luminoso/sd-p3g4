package ServerSide;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee.RefereeState;
import Communication.Message;
import Others.CoachStrategy;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfaceReferee;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ServiceProviderAgent extends Thread implements InterfaceCoach,
        InterfaceContestant,
        InterfaceReferee,
        Comparable<InterfaceContestant> {

    /**
     *
     */
    private ServerCom sconi;

    /**
     *
     */
    private static int serviceProviderAgentId = 0;

    /**
     *
     */
    private final ServerInterface servInterface;

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
     *
     * @param sconi
     * @param cbi
     * @param pgi
     * @param rsi
     * @param giri
     */
    ServiceProviderAgent(ServerCom sconi,
            ServerInterface servInterface) {

        super(Integer.toString(serviceProviderAgentId++));
        this.sconi = sconi;
        this.servInterface = servInterface;

        this.state = null;
        this.strength = 0;
        this.team = 0;
        this.contestantId = 0;
        this.strategy = null;
    }

    /**
     *
     */
    @Override
    public void run() {
        Message inMessage = null,
                outMessage = null;

        inMessage = (Message) sconi.readObject();

        // TODO: validate message
        // Adapt current thread to message running conditions
        // TODO: move to inside each cbi, pgi, rsi?
        // Contestant
        this.state = inMessage.getState();
        this.team = inMessage.getTeam();
        this.contestantId = inMessage.getContestantId();
        this.strength = inMessage.getStrength();

        // Referee (not neeeded)
        // process message in correct shared memory
        try {
            outMessage = servInterface.processAndReply(inMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sconi.writeObject(outMessage);
        sconi.close();
    }

    // COACH METHODS
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

    // CONTESTANT METHODS
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
    
    // REFEREE METHODS
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
