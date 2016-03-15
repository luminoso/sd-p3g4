/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Active.Contestant;
import Passive.ContestantsBench;
import Passive.RefereeSite;
import RopeGame.Constants;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    public Set<Integer> pickTeam(ContestantsBench bench, RefereeSite site) {
        Set<Integer> pickedTeam = new HashSet<>();
        
        List<Contestant> contestants = new LinkedList<>(bench.getBench());
        contestants.sort(comparator);
        
        for(Contestant cont : contestants) {
            if(pickedTeam.size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                break;
            }
            
            pickedTeam.add(cont.getContestantId());
        }
        
        return pickedTeam;
    }
}
