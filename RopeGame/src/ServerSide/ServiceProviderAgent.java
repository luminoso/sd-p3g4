package ServerSide;

import ClientSide.Coach;
import ClientSide.Contestant;
import ClientSide.Referee;
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
    private static int id = 0;
    
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
    private final Enum state;
    
    /**
     * 
     */
    private final int strength;
    
    /**
     * 
     */
    private final int team;
    
    /**
     * 
     */
    private final int contestantId;

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

        super(Integer.toString(id++));
        this.cbi = cbi;
        this.pgi = pgi;
        this.rsi = rsi;
        this.giri = giri;
        
        this.state = null;
        this.strength = 0;
        this.team = 0;
        this.contestantId = 0;
        
    }

    /**
     * 
     */
    @Override
    public void run() {
        Message inMessage, outMessage = null;

        inMessage = (Message) sconi.readObject();

        try {
            switch (inMessage.getMessageCategory()) {
                case CB:
                    outMessage = cbi.processAndReply(inMessage);
                case PG:
                    outMessage = pgi.processAndReply(inMessage);
                case GIR:
                    outMessage = giri.processAndReply(inMessage);
                case RS:
                    outMessage = rsi.processAndReply(inMessage);
                default:
            }
        } catch (Exception e) {
            //TODO deal with error;
        }

        sconi.writeObject(outMessage);
        sconi.close();
    }

    /**
     * 
     * @return 
     */
    @Override
    public Coach.CoachState getCoachState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public int getTeam() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public CoachStrategy getStrategy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param state 
     */
    @Override
    public void setState(Coach.CoachState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param strategy 
     */
    @Override
    public void setStrategy(CoachStrategy strategy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param team 
     */
    @Override
    public void setTeam(int team) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public int getContestatId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param id 
     */
    @Override
    public void setContestantId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public Contestant.ContestantState getContestantState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param state 
     */
    @Override
    public void setState(Contestant.ContestantState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public int getStrength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param strength 
     */
    @Override
    public void setStrength(int strength) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return 
     */
    @Override
    public Referee.RefereeState getRefereeState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param state 
     */
    @Override
    public void setState(Referee.RefereeState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
