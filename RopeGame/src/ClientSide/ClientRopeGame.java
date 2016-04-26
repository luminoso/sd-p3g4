package ClientSide;

import Others.CoachStrategy;
import Others.KeepWinningTeam;
import Others.MostStrengthStrategy;
import RopeGame.Constants;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ClientRopeGame {
    public static void main(String[] args) {
        if(args.length >= 1) {
            switch(args[0]) {
                case "RF": {
                    Referee ref = new Referee("Referee");
                    ref.start();
                    break;
                }
                case "CH": {
                    int team = Integer.parseInt(args[1]);                    
                    CoachStrategy strategy = (Integer.parseInt(args[2]) == 1 ? new KeepWinningTeam() : new MostStrengthStrategy());
                    Coach coach = new Coach("Coach " + team, team, strategy);
                    coach.start();
                    break;
                }
                case "CT": {
                    int team = Integer.parseInt(args[1]);
                    int id = Integer.parseInt(args[2]);
                    int strength = randomStrength();
                    Contestant contestant = new Contestant("Contestant " + team + ":" + id, team, id, strength);
                    contestant.start();
                    break;
                }
                default: {
                    System.out.println("");
                    System.exit(1);
                }
            }
        } else {
            System.out.println("");
            System.exit(1);
        }
    }

    /**
     * Function to generate a random strength when a player is instantiated.
     *
     * @return a strength for a player instantiation
     */
    private static int randomStrength() {
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }

}
