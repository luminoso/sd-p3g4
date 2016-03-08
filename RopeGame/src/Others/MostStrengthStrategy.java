/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Active.Contestant;
import Passive.ContestantsBench;
import Passive.RefereeSite;
import java.util.Comparator;
import java.util.List;

/**
 * General Description:
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class MostStrengthStrategy implements CoachStrategy {
    // Comparator to sort by strength
    private Comparator<Contestant> comparator = new Comparator<Contestant>() {
        @Override
        public int compare(Contestant o1, Contestant o2) {
            return o2.getContestantStrength()- o1.getContestantStrength();
        }
    };
    
    /**
     * 
     * @param bench
     * @param site
     * @return 
     */
    @Override
    public int[] pickTeam(ContestantsBench bench, RefereeSite site) {
        int[] pickedTeam = new int[3];
        
        List<Contestant> contestants = bench.getBench();
        contestants.sort(comparator);
        
        for(int i = 0; i < 3; i++) {
            pickedTeam[i] = contestants.get(i).getContestantId();
        }
        
        return pickedTeam;
    }
}
