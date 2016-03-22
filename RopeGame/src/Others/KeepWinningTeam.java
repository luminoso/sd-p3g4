/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import Active.Coach;
import Active.Contestant;
import Passive.ContestantsBench;
import Passive.RefereeSite;
import Passive.RefereeSite.TrialScore;
import RopeGame.Constants;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This strategy keeps the Contestants in game if they won or picks a random players if they lost
 * @author Guilherme Cardoso
 */
public class KeepWinningTeam implements CoachStrategy {

    @Override
    public Set<Integer> pickTeam(ContestantsBench bench, RefereeSite site) {
        List<TrialScore> trialPoints = site.getTrialPoints();

        int team = ((Coach) Thread.currentThread()).getCoachTeam();

        boolean didWeLost = false;
        
        // we can only make a decicion if there are any trial points
        if(trialPoints.size() > 0){
            TrialScore lastScore = trialPoints.get(trialPoints.size() - 1);

            if (team == 1 && lastScore == TrialScore.VICTORY_TEAM_2) {
                didWeLost = true;
            } else if (team == 2 && lastScore == TrialScore.VICTORY_TEAM_1) {
                didWeLost = true;
            }
        }

        // if we lost of if it is the first game we're going to pick random Contestants
        if (didWeLost || trialPoints.size() == 0) {
            List<Contestant> contestants = new LinkedList<>(bench.getBench());
            Collections.shuffle(contestants);

            Set<Integer> pickedTeam = new HashSet<>();

            for (Contestant contestant : contestants) {
                if (pickedTeam.size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                    break;
                }

                pickedTeam.add(contestant.getContestantId());
            }

            return pickedTeam;

        } else {
            // just return the same winning team
            return bench.getSelectedContestants();
        }

    }

}
