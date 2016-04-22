package ClientSide;

import Others.KeepWinningTeam;
import Others.MostStrengthStrategy;
import RopeGame.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ClientRopeGame {

    /**
     * 
     */
    static String serverHostName = "localhost";
    
    /**
     * 
     */
    static int serverPortNumb = 4000;

    public static void main(String[] args) {

        ContestantsBenchStub.getInstances();
        RefereeSiteStub.getInstance();
        PlaygroundStub.getInstance();
        GeneralInformationRepositoryStub informationRepository = GeneralInformationRepositoryStub.getInstance();

        // estatico para já
        // inicializar todos os jogadores, arbitro e coaches
        // Instantiating all active entities
        Referee rf = new Referee("Arbit", false);

        Coach[] coaches = new Coach[2];
        coaches[0] = new Coach("Coach: 1", 1, new MostStrengthStrategy(), false);
        coaches[1] = new Coach("Coach: 2", 2, new KeepWinningTeam(), false);

        Contestant[][] contestants = new Contestant[2][Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; j++) {
                contestants[i][j] = new Contestant("Cont: " + (j + 1) + " Team: " + (i + 1), i + 1, j + 1, randomStrength(), true);
            }
        }

        // Adding initial information to the General Information Repository
        informationRepository.addReferee(rf);

        for (int i = 0; i < coaches.length; i++) {
            informationRepository.addCoach(coaches[i]);
            for (int j = 0; j < contestants[i].length; j++) {
                informationRepository.addContestant(contestants[i][j]);
            }
        }

        // Print the main header
        informationRepository.printHeader();

        // Starting simulation
        for (int i = 0; i < coaches.length; i++) {
            coaches[i].start();

            for (int j = 0; j < contestants[i].length; j++) {
                contestants[i][j].start();
            }
        }

        rf.start();

        try {
            // Waiting for simulation to end
            rf.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientRopeGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < coaches.length; i++) {
            while (coaches[i].isAlive()) {
                coaches[i].interrupt();
            }

            for (Contestant contestant : contestants[i]) {
                while (contestant.isAlive()) {
                    contestant.interrupt();
                }
            }
        }
    }

    public static String getServerHostName() {
        return serverHostName;
    }

    public static int getServerPortNumb() {
        return serverPortNumb;
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
