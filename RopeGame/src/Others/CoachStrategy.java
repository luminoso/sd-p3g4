package Others;

import java.io.Serializable;
import java.util.Set;

/**
 * General Description: Interface for Coaches strategies
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public interface CoachStrategy extends Serializable {
    /**
     * 
     * @param bench
     * @param site
     * @return 
     */
    public Set<Integer> pickTeam(InterfaceContestantsBench bench, InterfaceRefereeSite site);
}
