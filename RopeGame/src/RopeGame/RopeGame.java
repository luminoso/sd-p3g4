package RopeGame;

import Active.Coach;
import Active.Contestant;
import Active.Referee;
import Others.KeepWinningTeam;
import Others.MostStrengthStrategy;
import Passive.ContestantsBench;
import Passive.GeneralInformationRepository;
import Passive.Playground;
import Passive.RefereeSite;

/**
 * General description:
 * This class starts the Rope Game. It instantiates all the active 
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {
    public static void main(String[] args) throws InterruptedException {
        // Instantiating all passive entities
        ContestantsBench.getInstances();
        RefereeSite.getInstance();
        Playground.getInstance();
        GeneralInformationRepository informationRepository = GeneralInformationRepository.getInstance();
        
        // Instantiating all active entities
        Referee rf = new Referee("Arbit");
        
        Coach[] coaches = new Coach[2];
        coaches[0] = new Coach("Coach: 1", 1, new MostStrengthStrategy());
        coaches[1] = new Coach("Coach: 2", 2, new KeepWinningTeam());
        
        Contestant[][] contestants = new Contestant[2][Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH];
        
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; j++){
                contestants[i][j] = new Contestant("Cont: " + (j+1) + " Team: " + (i+1), i+1, j+1, randomStrength() );
            }
        }

        // Adding initial information to the General Information Repository
        informationRepository.addReferee(rf);
        
        for(int i = 0; i < coaches.length; i++) {
            informationRepository.addCoach(coaches[i]);
            for(int j = 0; j < contestants[i].length; j++) {
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

        // Waiting for simulation to end
        rf.join();
        
        for (int i = 0; i < coaches.length; i++) {
            coaches[i].join();

            for (int j = 0; j < contestants[i].length; j++) {
                contestants[i][j].join();
            }
        }
    }
    
    private static int randomStrength(){
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }
}
