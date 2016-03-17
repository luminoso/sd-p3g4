package RopeGame;

import Active.Coach;
import Active.Contestant;
import Active.Referee;
import Others.MostStrengthStrategy;
import Passive.ContestantsBench;
import Passive.GeneralInformationRepository;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {
    public static void main(String[] args) throws InterruptedException {
        SecureRandom rand = new SecureRandom();
        
        Contestant team1contestant1 = new Contestant("Joao10", 1, 1, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant2 = new Contestant("Joao11", 1, 2, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant3 = new Contestant("Joao12", 1, 3, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant4 = new Contestant("Joao13", 1, 4, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant5 = new Contestant("Joao14", 1, 5, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        
        System.out.println("Jogadores team1 adicionados ");
        
        Contestant team2contestant1 = new Contestant("Joao20", 2, 1, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant2 = new Contestant("Joao21", 2, 2, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant3 = new Contestant("Joao22", 2, 3, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant4 = new Contestant("Joao23", 2, 4, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant5 = new Contestant("Joao24", 2, 5, (int) (rand.nextInt(2) * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
 
        System.out.println("Jogadores team2 adicionados ");
        
        Referee rf = new Referee("Arbit");
        
        Coach coach1 = new Coach("coach1", 1, new MostStrengthStrategy());
        Coach coach2 = new Coach("coach2", 2, new MostStrengthStrategy());
    
        Scanner sc = new Scanner(System.in);
        sc.next();
        
        GeneralInformationRepository informationRepository = GeneralInformationRepository.getInstance();
        
        informationRepository.addContestant(team1contestant1);
        informationRepository.addContestant(team1contestant2);
        informationRepository.addContestant(team1contestant3);
        informationRepository.addContestant(team1contestant4);
        informationRepository.addContestant(team1contestant5);
        
        informationRepository.addContestant(team2contestant1);
        informationRepository.addContestant(team2contestant2);
        informationRepository.addContestant(team2contestant3);
        informationRepository.addContestant(team2contestant4);
        informationRepository.addContestant(team2contestant5);
        
        team1contestant1.start();
        team1contestant2.start();
        team1contestant3.start();
        team1contestant4.start();
        team1contestant5.start();
        
        team2contestant1.start();
        team2contestant2.start();
        team2contestant3.start();
        team2contestant4.start();
        team2contestant5.start();
        
        informationRepository.addCoach(coach1);
        informationRepository.addCoach(coach2);
        informationRepository.addReferee(rf);
        
        informationRepository.printHeader();
        
        coach1.start();
        coach2.start();
        rf.start();
        
        rf.join();
        coach1.join();
        coach2.join();
        
        team1contestant1.join();
        team1contestant2.join();
        team1contestant3.join();
        team1contestant4.join();
        team1contestant5.join();
        
        team2contestant1.join();
        team2contestant2.join();
        team2contestant3.join();
        team2contestant4.join();
        team2contestant5.join();
        
        
    }
}
