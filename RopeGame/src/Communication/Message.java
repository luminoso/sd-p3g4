package Communication;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee;
import Others.CoachStrategy;
import java.io.Serializable;

/**
 *
 * @author luminoso
 */
public class Message implements Serializable {
    
    private static final long serialVersionUID = 20160412;
    
    
    // variables required to define the Contestant
    private int team;
    private int id;
    private ContestantState contestantState;
    private int strength;
    
    // additional variables required to define the Coach
    private CoachState coachState;
    private CoachStrategy strategy;
    
    // additional variables required to define the Referee
    private Referee.RefereeState refereeState;
    private String name;
    
    // message variables
    private messageType type;
    
    public enum messageType{
        
    }
    
}
