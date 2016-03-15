package Active;

import Others.CoachStrategy;
import Passive.ContestantsBench;
import Passive.RefereeSite;
import java.util.Set;

/**
 * General Description:
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Coach extends Thread {
    private CoachState state;           // Coach state
    private int team;                   // Coach team
    private CoachStrategy strategy;     // Team picking strategy
    
    /**
     * 
     * @param name
     * @param team 
     * @param strategy 
     */
    public Coach(String name, int team, CoachStrategy strategy) {
        // Giving name to thread
        super(name);
        
        // Initial state
        this.state = CoachState.WAIT_FOR_REFEREE_COMMAND;
        
        // Team assignement
        this.team = team;
        
        // Team picking strategy
        this.strategy = strategy;
    }

    /**
     * 
     * @return 
     */
    public CoachState getCoachState() {
        return state;
    }

    /**
     * 
     * @param state 
     */
    public void setCoachState(CoachState state) {
        this.state = state;
    }
    
    /**
     * 
     * @return 
     */
    public int getCoachTeam() {
        return team;
    }
    
    /**
     * 
     */
    @Override
    public void run() {
        // TODO: Replace true by terminating condition
        while(true) {
            switch(state) {
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
     * The coach decides which players are selected for next round
     * and updates selectedContestants array at the Bench
     */
    private void callContestants() {
        // Contestants bench
        ContestantsBench bench = ContestantsBench.getInstance();
        
        // Referee site
        RefereeSite site = RefereeSite.getInstance();
        
        // Picking team
        Set<Integer> pickedContestants = this.strategy.pickTeam(bench, site);
        
        // Setting the selected team
        bench.setSelectedContestants(pickedContestants);
        
        // Updating coach state
        this.setCoachState(CoachState.ASSEMBLE_TEAM);
    }

    // TODO: Implement
    private void informReferee() {}

    /**
     * The coach updates his players which have played and game and
     * updates their strength
     */
    private void reviewNotes() {
        ContestantsBench bench = ContestantsBench.getInstance();
        Set<Integer> selectedContestants = bench.getSelectedContestants();
        Set<Contestant> allContestants = bench.getBench();
        
        for(Contestant contestant : allContestants) {
            if(selectedContestants.contains(contestant.getContestantId())) {
                contestant.setContestantStrength(contestant.getContestantStrength() - 1);
            } else {
                contestant.setContestantStrength(contestant.getContestantStrength() + 1);
            }
        }
        
        // Updating coach state
        this.setCoachState(CoachState.WAIT_FOR_REFEREE_COMMAND);
    }
    
    /**
     * 
     */
    public enum CoachState {
        WAIT_FOR_REFEREE_COMMAND (1, "WFRC"),
        ASSEMBLE_TEAM (2, "AETM"),
        WATCH_TRIAL (3, "WHTL");
        
        private int id;
        private String state;
        
        /**
         * 
         * @param id
         * @param state 
         */
        CoachState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * 
         * @return 
         */
        public int getId() {
            return id;
        }

        /**
         * 
         * @return 
         */
        public String getState() {
            return state;
        }
    }
}
