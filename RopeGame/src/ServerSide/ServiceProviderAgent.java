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
        InterfaceReferee {

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
    private final ContestantsBenchInterface cbi;

    /**
     *
     */
    private final PlaygroundInterface pgi;

    /**
     *
     */
    private final RefereeSiteInterface rsi;

    /**
     *
     */
    private final GeneralInformationRepositoryInterface giri;

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
            ContestantsBenchInterface cbi,
            PlaygroundInterface pgi,
            RefereeSiteInterface rsi,
            GeneralInformationRepositoryInterface giri) {

        super(Integer.toString(serviceProviderAgentId++));
        this.sconi = sconi;
        this.cbi = cbi;
        this.pgi = pgi;
        this.rsi = rsi;
        this.giri = giri;

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
        if(inMessage.getName() != null)
            this.setName(inMessage.getName());
        
        this.state = inMessage.getState();
        this.team = inMessage.getTeam();
        this.contestantId = inMessage.getId();
        this.strength = inMessage.getId();

        // Coach
        this.strategy = inMessage.getStrategy();

        // Referee (not neeeded)
        // process message in correct shared memory
        try {
            switch (inMessage.getMessageCategory()) {
                case CB:
                    outMessage = cbi.processAndReply(inMessage);
                    break;
                case PG:
                    outMessage = pgi.processAndReply(inMessage);
                    break;
                case GIR:
                    outMessage = giri.processAndReply(inMessage);
                    break;
                case RS:
                    outMessage = rsi.processAndReply(inMessage);
                    break;
                default:
            }
        } catch (Exception e) {
            //TODO deal with error;
        }

        sconi.writeObject(outMessage);
        sconi.close();
    }

    @Override
    public CoachState getCoachState() {
        return (CoachState) state;
    }

    @Override
    public int getTeam() {
        return team;
    }

    @Override
    public CoachStrategy getStrategy() {
        return strategy;
    }

    @Override
    public void setState(CoachState state) {
        this.state = state;
    }

    @Override
    public void setStrategy(CoachStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void setTeam(int team) {
        this.team = team;
    }

    @Override
    public int getContestatId() {
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
    public void setState(Contestant.ContestantState state) {
        this.state = state;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public RefereeState getRefereeState() {
        return (RefereeState) state;
    }

    @Override
    public void setState(RefereeState state) {
        this.state = state;
    }

}
