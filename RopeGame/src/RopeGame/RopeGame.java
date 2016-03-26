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
import static java.lang.System.out;
public class RopeGame {
    public static void main(String[] args) throws InterruptedException {
        
        Referee rf;
        GeneralInformationRepository informationRepository;
        Contestant [][] contestants;
        Coach [] coaches = new Coach[2];
        
        coaches[0] = new Coach("Coach Team 1", 1, new MostStrengthStrategy());
        coaches[1] = new Coach("coach2", 2, new KeepWinningTeam());
        
        rf = new Referee("Arbit");
        informationRepository = GeneralInformationRepository.getInstance();
        
        contestants = new Contestant[2][Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH];
        
        for(int i = 0; i < 2; i++){
            
            informationRepository.addCoach(coaches[i]);
            
            for(int j = 0, id=1; j < Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; j++, id++){
                
                contestants[i][j] = new Contestant("Contestant team:" + i + " id:" + id , i+1, id, randomStrength() );
                informationRepository.addContestant(contestants[i][j]);
            
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
        
        System.out.println("Tudo terminou correctamente");
        
        
    }
    
    private static int randomStrength(){
        SecureRandom rand = new SecureRandom();
        return (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE);
    }
}
