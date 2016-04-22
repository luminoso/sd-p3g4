package Others;

import ClientSide.Contestant;
import RopeGame.Constants;
import ServerSide.RefereeSite;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * General Description:
 * Coach strategy implementation that picks the players with the most strength.
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class MostStrengthStrategy implements CoachStrategy, Comparator<Contestant> {

    /**
     * Comparator to sort the Contestant accordingly to this strategy
     */
    @Override
    public int compare(Contestant o1, Contestant o2) {
        return o2.getStrength()- o1.getStrength();
    }
        
    /**
     * Picks a team using the most strength strategy
     * @param bench Bench containing all Contestants to pick from
     * @param site Current Playground Site 
     * @return Set of the picked Contestants
     */
    @Override
    public Set<Integer> pickTeam(Bench bench, RefereeSite site) {
        Set<Integer> pickedTeam = new HashSet<>();
        
        List<Contestant> contestants = new LinkedList<>(bench.getBench());
        contestants.sort(this);
        
        for(Contestant cont : contestants) {
            if(pickedTeam.size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                break;
            }
            
            pickedTeam.add(cont.getContestatId());
        }
        
        return pickedTeam;
    }
}
