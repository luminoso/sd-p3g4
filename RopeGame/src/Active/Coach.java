package Active;

import Passive.ContestantsBench;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Coach extends Thread {
    private CoachState state;           // Coach state
    private int team;                   // Coach team
    
    public Coach(String name, int team) {
        super(name);
        
        this.state = CoachState.WAIT_FOR_REFEREE_COMMAND;
        
        this.team = team;
    }

    public CoachState getCoachState() {
        return state;
    }

    public void setCoachState(CoachState state) {
        this.state = state;
    }
    
    public int getCoachTeam() {
        return team;
    }
    
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
        // Basic strategy. Calls for the strongest players
        
        ContestantsBench bench = ContestantsBench.getInstance(this.team);
        bench.sort();                                   // Arrange players by stregth
        int[] contestantsids = bench.getIDs();          // choose the 3 strongest
        bench.setSelectedContestants(contestantsids);   // add to selectedContestants
    }

    // TODO: Implement
    private void informReferee() {}

    /**
     * The coach updates his players which have played and game and
     * updates their strength
     */
    private void reviewNotes() {
        ContestantsBench bench = ContestantsBench.getInstance(this.team);
        int[] contestantsids = bench.getSelectedContestants();
        
        for(int i = 0; i < 3; i++){
            Contestant contestant = bench.getContestant(contestantsids[i]);
            contestant.setStrength(contestant.getStrength() - 1);
        }
    }
    
    public enum CoachState {
        WAIT_FOR_REFEREE_COMMAND (1, "WFRC"),
        ASSEMBLE_TEAM (2, "AETM"),
        WATCH_TRIAL (3, "WHTL");
        
        private int id;
        private String state;
        
        CoachState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        public int getId() {
            return id;
        }

        public String getState() {
            return state;
        }
    }
}
