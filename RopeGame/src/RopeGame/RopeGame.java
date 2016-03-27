package RopeGame;

import Active.Coach;
import Active.Contestant;
import Active.Referee;
import Others.KeepWinningTeam;
import Others.MostStrengthStrategy;
import Passive.GeneralInformationRepository;
import java.security.SecureRandom;


/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {
    public static void main(String[] args) throws InterruptedException {
        Referee rf;
        GeneralInformationRepository informationRepository;
        Contestant[][] contestants;
        Coach[] coaches = new Coach[2];
        
        coaches[0] = new Coach("Coach: 1", 1, new MostStrengthStrategy());
        coaches[1] = new Coach("Coach: 2", 2, new KeepWinningTeam());
        
        rf = new Referee("Arbit");
        informationRepository = GeneralInformationRepository.getInstance();
        
        contestants = new Contestant[2][Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH];
        
        for(int i = 0; i < 2; i++){    
            informationRepository.addCoach(coaches[i]);
            
            for(int j = 0; j < Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; j++){
                contestants[i][j] = new Contestant("Cont: " + (j+1) + " Team: " + (i+1), i+1, j+1, randomStrength() );
                informationRepository.addContestant(contestants[i-1][j-1]);
            }
        }

        informationRepository.addReferee(rf);
        informationRepository.printHeader();
        
        // start simulation
        for (int i = 0; i < 2; i++) {
            coaches[i].start();

            for (int j = 0; j < Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; j++) {
                contestants[i][j].start();
            }
        }
        
        rf.start();

        // waiting for simulation to end
        rf.join();
        
        for (int i = 0; i < 2; i++) {
            coaches[i].join();

            for (int j = 0; j < Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; j++) {
                contestants[i][j].join();
            }
        }
    }
    
    private static int randomStrength(){
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }
}
