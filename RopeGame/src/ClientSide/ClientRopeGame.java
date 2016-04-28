package ClientSide;

import Others.CoachStrategy;
import Others.KeepWinningTeam;
import Others.MostStrengthStrategy;
import RopeGame.Constants;
import static java.lang.System.out;

/**
 * This class implements the Client of RopeGame client-server architecture. It
 * also implements initialization of the active ententies.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class ClientRopeGame {

    /**
     * Initializes the Client of RopeGame Client/Server implementation.
     * <p>
     * <ul>
     * <li>Referee: RF
     * <li>Coach: ClientRopeGame CH team strategy
     * <li>Contestant: ClientRopeGame CT team id
     * </ul><p>
     *
     * @param args --help for full details
     */
    public static void main(String[] args) {

        /**
         * Initializes the ClientRopeGame According to the argument
         */
        if (args.length >= 1) {
            switch (args[0]) {
                case "RF": {
                    Referee ref = new Referee("Referee");
                    ref.start();
                    out.println("Client Referee started");
                    break;
                }
                case "CH": {
                    int team = Integer.parseInt(args[1]);
                    CoachStrategy strategy = (Integer.parseInt(args[2]) == 1 ? new KeepWinningTeam() : new MostStrengthStrategy());
                    Coach coach = new Coach("Coach " + team, team, strategy);
                    coach.start();
                    out.println("Client Coach started. Team:" + team);
                    break;
                }
                case "CT": {
                    int team = Integer.parseInt(args[1]);
                    int id = Integer.parseInt(args[2]);
                    int strength = randomStrength();
                    Contestant contestant = new Contestant("Contestant " + team + ":" + id, team, id, strength);
                    contestant.start();
                    out.println("Client Contestant started. Team:" + team + " ID:" + id);
                    break;
                }
                default: {
                    out.println("Error: hit switch default case. Exiting.");
                    System.exit(1);
                }
            }
        } else {
            out.println("No arguments given. Use one of the following arguments:");
            out.println("- Referee: ClientRopeGame RF");
            out.println("- Coach: ClientRopeGame CH <int:team> <int:strategy>");
            out.println("- Contestant: ClientRopeGame CT <int:team> <int:id>");
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
