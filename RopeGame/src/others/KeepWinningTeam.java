package others;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This strategy keeps the Contestants in game if they won or picks a random
 * players if they lost
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class KeepWinningTeam implements CoachStrategy {

    @Override
    public Set<Integer> pickTeam(Set<Tuple<Integer, Integer>> contestants,
            Set<Integer> selectedContestants,
            List<TrialScore> trialPoints) {

        int team = ((InterfaceCoach) Thread.currentThread()).getCoachTeam();

        boolean didWeLost = false;

        // we can only make a decicion if there are any trial points
        if (trialPoints.size() > 0) {
            //TrialScore lastScore = trialPoints.get(trialPoints.size() - 1);

            TrialScore lastScore = trialPoints.get(trialPoints.size() - 1);

            if (team == 1 && lastScore == TrialScore.VICTORY_TEAM_2) {
                didWeLost = true;
            } else if (team == 2 && lastScore == TrialScore.VICTORY_TEAM_1) {
                didWeLost = true;
            }
        }

        // if we lost of if it is the first game we're going to pick random Contestants
        if (didWeLost || trialPoints.isEmpty()) {
            List<Tuple<Integer, Integer>> contestants_list = new LinkedList<>(contestants);
            Collections.shuffle(contestants_list);

            Set<Integer> pickedTeam = new HashSet<>();

            for (Tuple<Integer, Integer> contestant : contestants_list) {
                if (pickedTeam.size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                    break;
                }

                pickedTeam.add(contestant.getFirst());
            }

            return pickedTeam;

        } else {
            // just return the same winning team
            return selectedContestants;
        }

    }

}
