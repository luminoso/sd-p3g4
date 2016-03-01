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
    private static ContestantsBench[] instances = new ContestantsBench[2];  // Doubleton
    
    private List<Contestant> bench;                                         //
    private int[] selectedContestants;                                      //
    private int team;                                                       //
  
    /**
     * 
     * @param id
     * @return 
     */
    public static ContestantsBench getInstance(int id) {
        if(instances[id-1] == null) {
            instances[id-1] = new ContestantsBench(id);
        }
        
        return instances[id-1];
    }
    
    /**
     * 
     * @param team 
     */
    private ContestantsBench(int team) {
        this.team = team;
        this.bench = new ArrayList<>();
        this.selectedContestants = new int[3];
    }

    /**
     * 
     * @return 
     */
    public int getTeam() {
        return team;
    }
    
    /**
     * 
     * @param contestant 
     */
    public void addContestant(Contestant contestant){
        bench.add(contestant);
    }
    
    /**
     * 
     * @param id
     * @return 
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
