package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBench {
    private static final ContestantsBench[] instances = new ContestantsBench[2];    // Doubleton containing the two teams benches
    
    private List<Contestant> bench;                                                 // Structure that contains the players in the bench
    private int[] selectedContestants;                                              // Selected contestants to play the trial
    private int team;                                                               // Team identifier
  
    /**
     * Method that returns a ContestantsBench object. The method is thread-safe
     * and uses the implicit monitor of the class.
     * 
     * @param id Team identifier.
     * @return ContestantsBench object specified by the team identifier passed.
     */
    public static synchronized ContestantsBench getInstance(int id) {
        if(instances[id-1] == null) {
            instances[id-1] = new ContestantsBench(id);
        }
        
        return instances[id-1];
    }
    
    /**
     * Private constructor to be used in the doubleton.
     * 
     * @param team Team identifier.
     */
    private ContestantsBench(int team) {
        this.team = team;
        this.bench = new ArrayList<>();
        this.selectedContestants = new int[3];
    }

    /**
     * The method returns the team identifier that is in the bench.
     * 
     * @return Team identifier.
     */
    public int getTeam() {
        return team;
    }
    
    /**
     * The method adds a contestant to the bench.
     * 
     * @param contestant Contestant that belongs to the team and needs to be 
     * added to the bench.
     */
    public void addContestant(Contestant contestant){
        bench.add(contestant);
    }
    
    /**
     * The method removes a contestant from the bench.
     * 
     * @param id Contestant identifier.
     * @return Contestant that needed to be removed.
     */
    public Contestant getContestant(int id){
        for(Contestant contestant : bench){
            if(contestant.getId() == id){
                bench.remove(contestant);
                return contestant;
            }
        }
        
        return null;
    }
}
