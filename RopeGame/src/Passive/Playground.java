package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Playground {
    private static Playground instance;
    
    private int flagPosition;
    private int lastFlagPosition;
    private List<Contestant>[] teams;
    
    /**
     * The method returns the Playground object. This method is thread-safe and 
     * uses the implicit monitor of the class.
     * 
     * @return Playground object to be used.
     */
    public static synchronized Playground getInstance() {
        if(instance == null) {
            instance = new Playground();
        }
        
        return instance;
    }
    
    /**
     * Private constructor to be used in the singleton.
     */
    private Playground() {
        this.flagPosition = 0;
        
        this.teams = new List[2];
        this.teams[0] = new ArrayList<>();
        this.teams[1] = new ArrayList<>();
    }
    
    /**
     * The method adds a contestant to the playground.
     * 
     * @param teamId Team identifier.
     * @param contestant Contestant to be added.
     */
    public void addContestantToTeam(int teamId, Contestant contestant){
        this.teams[teamId-1].add(contestant);
    }
    
    /**
     * The method removes the contestant from the playground.
     * 
     * @param teamId Team identifier.
     * @param contestantId Contestant identifier.
     * @return Contestant specified for removal.
     */
    public Contestant getContestantFromTeam(int teamId, int contestantId){
        for(Contestant contestant : this.teams[teamId-1]){
            if(contestant.getId() == contestantId){
                this.teams[teamId-1].remove(contestant);
                return contestant;
            }
        }
        
        return null;
    }
    
    /**
     * The method returns the flag position in relation to the middle. 
     * Middle = 0.
     * 
     * @return Position of the flag. 
     */
    public int getFlagPosition(){
        return this.flagPosition;
    }

    /**
     * The method sets the flag position in relation to the middle. Middle = 0.
     * 
     * @param flagPosition Position of the flag.
     */
    public void setFlagPosition(int flagPosition) {
        this.lastFlagPosition = this.flagPosition;
        this.flagPosition = flagPosition;
    }

    /**
     * The method gets the flag position in the begging of the match/trial
     * @return 
     */
    public int getLastFlagPosition() {
        return lastFlagPosition;
    }
    
    /**
     * Checks if all contestants are in the playground and ready to play the game
     * @return True if all 6 players are in game
     */
    public boolean checkAllContestantsReady(){
        return (teams[0].size() + teams[1].size()) == 6;
    }

    public List<Contestant>[] getTeams() {
        return teams;
    }
    
    
    
}
