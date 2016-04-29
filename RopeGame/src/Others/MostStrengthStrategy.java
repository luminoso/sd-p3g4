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
 * @version 2016-2
 */
public class MostStrengthStrategy implements CoachStrategy, Comparator<Tuple<Integer, Integer>> {

    @Override
    public int compare(Tuple<Integer, Integer> o1, Tuple<Integer, Integer> o2) {
        return o2.getRight() - o1.getRight();
    }

    @Override
    public Set<Integer> pickTeam(InterfaceContestantsBench bench, InterfaceRefereeSite site) {
        Set<Integer> pickedTeam = new HashSet<>();

        List<Tuple<Integer, Integer>> contestants = new LinkedList<>(bench.getBench());
        contestants.sort(this);

        for (Tuple<Integer, Integer> cont : contestants) {
            if (pickedTeam.size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                break;
            }

            pickedTeam.add(cont.getLeft());
        }

        return pickedTeam;
    }
}
