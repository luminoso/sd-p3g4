package Others;

import Passive.ContestantsBench;
import Passive.RefereeSite;
import java.util.Set;

/**
 * General Description:
 * Interface for Coaches strategies 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */

public interface CoachStrategy {
    public Set<Integer> pickTeam(ContestantsBench bench, RefereeSite site);
}
