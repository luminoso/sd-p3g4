package RopeGame;

import Active.Coach;
import Active.Contestant;
import Active.Referee;
import Others.MostStrengthStrategy;
import Passive.ContestantsBench;
import Passive.GeneralInformationRepository;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {
    public static void main(String[] args) throws InterruptedException {
        
        Contestant team1contestant1 = new Contestant("Joao10",1,11, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant2 = new Contestant("Joao11",1,12, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant3 = new Contestant("Joao12",1,13, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant4 = new Contestant("Joao13",1,14, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team1contestant5 = new Contestant("Joao14",1,15, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        
        System.out.println("Jogadores team1 adicionados ");
        
        Contestant team2contestant1 = new Contestant("Joao20",2,21, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant2 = new Contestant("Joao21",2,22, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant3 = new Contestant("Joao22",2,23, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant4 = new Contestant("Joao23",2,24, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
        Contestant team2contestant5 = new Contestant("Joao24",2,25, (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE) + Constants.INITIAL_MINIMUM_FORCE));
 
        System.out.println("Jogadores team2 adicionados ");
        
        Referee rf = new Referee("Arbit");
        
        Coach coach1 = new Coach("coach1", 1, new MostStrengthStrategy());
        Coach coach2 = new Coach("coach2", 2, new MostStrengthStrategy());
    
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
        
        coach1.start();
        coach2.start();
        rf.start();
        
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
        
        coach1.start();
        coach2.start();
        rf.start();
    }
}
