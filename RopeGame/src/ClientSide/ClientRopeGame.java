package ClientSide;

import ClientSide.Referee.Referee;
import ClientSide.Contestant.Contestant;
import ClientSide.Coach.Coach;
import Others.CoachStrategy;
import Interfaces.InterfaceContestantsBench;
import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfacePlayground;
import Interfaces.InterfaceRefereeSite;
import Others.KeepWinningTeam;
import Others.MostStrengthStrategy;
import RopeGame.Constants;
import static java.lang.System.out;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * <ul>
     * <li>Referee: RF
     * <li>Coach: ClientRopeGame CH team strategy
     * <li>Contestant: ClientRopeGame CT team id
     * </ul>
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
                    InterfaceContestantsBench bench = new ContestantsBenchStub(1);
                    bench.waitForEveryoneToStart();

                    Referee ref = new Referee("Referee");
                    ref.start();
                    out.println("Client Referee started");
                    
                    try {
                        ref.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientRopeGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    InterfaceRefereeSite refereeSite = new RefereeSiteStub();
                    InterfacePlayground playground = new PlaygroundStub();
                    InterfaceGeneralInformationRepository informationRepository = new GeneralInformationRepositoryStub();

                    bench.interrupt();

                    bench.shutdown();
                    refereeSite.shutdown();
                    playground.shutdown();
                    informationRepository.shutdown();

                    break;
                }
                case "CH": {
                    int team = Integer.parseInt(args[1]);
                    CoachStrategy strategy = (Integer.parseInt(args[2]) == 1 ? new KeepWinningTeam() : new MostStrengthStrategy());
                    Coach coach = new Coach("Coach " + team, team, strategy);
                    coach.start();
                    out.println("Client Coach started. Team:" + team);

                    try {
                        coach.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientRopeGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    InterfaceContestantsBench bench = new ContestantsBenchStub(team);
                    InterfaceRefereeSite refereeSite = new RefereeSiteStub();
                    InterfacePlayground playground = new PlaygroundStub();
                    InterfaceGeneralInformationRepository informationRepository = new GeneralInformationRepositoryStub();

                    bench.shutdown();
                    refereeSite.shutdown();
                    playground.shutdown();
                    informationRepository.shutdown();

                    break;
                }
                case "CT": {
                    int team = Integer.parseInt(args[1]);
                    int id = Integer.parseInt(args[2]);
                    int strength = randomStrength();
                    Contestant contestant = new Contestant("Contestant " + team + ":" + id, team, id, strength);
                    contestant.start();
                    out.println("Client Contestant started. Team:" + team + " ID:" + id);

                    try {
                        contestant.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientRopeGame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    InterfaceContestantsBench bench = new ContestantsBenchStub(team);
                    InterfaceRefereeSite refereeSite = new RefereeSiteStub();
                    InterfacePlayground playground = new PlaygroundStub();
                    InterfaceGeneralInformationRepository informationRepository = new GeneralInformationRepositoryStub();

                    bench.shutdown();
                    refereeSite.shutdown();
                    playground.shutdown();
                    informationRepository.shutdown();

                    break;
                }
                default: {
                    out.println("Error: hit switch default case. Exiting.");
                    System.exit(1);
                }
            }
        } else {
            out.println("No arguments given. Use one of the following arguments:");
            out.println("");
            out.println("Clients:");
            out.println("- Referee: ClientRopeGame RF");
            out.println("- Coach: ClientRopeGame CH <int:team> <int:strategy>");
            out.println("- Contestant: ClientRopeGame CT <int:team> <int:id>");
            out.println("");
            out.println("Server:");
            out.println("- Playground: PG");
            out.println("- RefereeSite: RS");
            out.println("- General Information Repository: GR");
            out.println("- ContestantsBech: CB");
            System.exit(1);
        }
    }

    /**
     * Function to generate a random strength when a player is instantiated
     *
     * @return a strength for a player instantiation
     */
    private static int randomStrength() {
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }

}
