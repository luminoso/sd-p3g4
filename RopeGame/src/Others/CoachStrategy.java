package Others;

import Interfaces.InterfaceRefereeSite;
import Interfaces.InterfaceContestantsBench;
import java.io.Serializable;
import java.util.Set;

/**
 * Interface that defines the operations available over the objects that
 * represent the coach strategies
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public interface CoachStrategy extends Serializable {

    /**
     * Method in which the coach picks the Contestants to go to referee site
     *
     * @param bench with all the contestants
     * @param site implementation
     * @return set with the picked contestants
     */
    Set<Integer> pickTeam(InterfaceContestantsBench bench, InterfaceRefereeSite site);
}
