package Others;

import RopeGame.Constants;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Coach strategy implementation that picks the players with the most strength
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class MostStrengthStrategy implements CoachStrategy, Comparator<Tuple<Integer, Integer>> {

    @Override
    public int compare(Tuple<Integer, Integer> o1, Tuple<Integer, Integer> o2) {
        return o2.getSecond() - o1.getSecond();
    }

    @Override
    public Set<Integer> pickTeam(Set<Tuple<Integer, Integer>> contestants,
            Set<Integer> selectedContestants,
            List<TrialScore> trialPoints) {

        List<Tuple<Integer, Integer>> contestants_list = new LinkedList<>(contestants);

        Set<Integer> pickedTeam = new HashSet<>();

        contestants_list.sort(this);

        for (Tuple<Integer, Integer> cont : contestants_list) {
            if (pickedTeam.size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                break;
            }

            pickedTeam.add(cont.getFirst());
        }

        return pickedTeam;
    }
}
