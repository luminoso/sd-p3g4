package others;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Interface that defines the operations available over the objects that
 * represent the coach strategies
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface CoachStrategy extends Serializable {

    /**
     * Method in which the coach picks the Contestants to go to referee site
     *
     * @param contestants Set containing Tuples of id,strength of all players
     * (same as getBench())
     * @param selectedContestants Set with selected contestants id
     * @param trialPoints List containing trial points
     * @return set with the picked contestants
     */
    Set<Integer> pickTeam(Set<Tuple<Integer, Integer>> contestants,
            Set<Integer> selectedContestants,
            List<TrialScore> trialPoints);
}
