package Passive;

import RopeGame.Constants;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RefereeSite {
    private static RefereeSite instance;
    
    private Lock lock;
    
    private List<TrialScore> trialStatus;
    private List<GameScore> gameStatus;
    
    /**
     * The method returns the RefereeSite object. The method is thread-safe and
     * uses the implicit monitor of the class.
     * 
     * @return RefereeSite object to be used.
     */
    public static synchronized RefereeSite getInstance() {
        if(instance == null) {
            instance = new RefereeSite();
        }
        
        return instance;
    }
    
    /**
     * Private constructor to be used in singleton.
     */
    private RefereeSite() {
        this.lock = new ReentrantLock();
        
        this.trialStatus = new LinkedList<>();
        this.gameStatus = new LinkedList<>();
    }

    /**
     * The method returns the game round number.
     * 
     * @return Game round number.
     */
    public int getGameRound() {
        int gameRound;
        
        lock.lock();
        
        gameRound = this.gameStatus.size() + 1;
        
        lock.unlock();
        
        return gameRound;
    }

    /**
     * The method returns the trial round number.
     * 
     * @return Trial round number.
     */
    public int getTrialRound() {
        int trialNumber;
        
        lock.lock();
        
        trialNumber = this.trialStatus.size() + 1;
        
        lock.unlock();
        
        return trialNumber;
    }

    /**
     * The method returns the game points in the form of an array.
     * 
     * @return Game points.
     */
    public List<GameScore> getGamePoints() {
        List<GameScore> gamePoints;
        
        lock.lock();
        
        gamePoints = new LinkedList<>(this.gameStatus);
        
        lock.unlock();
        
        return gamePoints;
    }

    /**
     * The method returns the trial points in the form of an array.
     * 
     * @return Trial points.
     */
    public List<TrialScore> getTrialPoints() {
        List<TrialScore> trialPoints;
        
        lock.lock();
        
        trialPoints = new LinkedList<>(this.trialStatus);
        
        lock.unlock();
        
        return trialPoints;
    }
    
    public void resetTrialPoints(){
        lock.lock();
        
        this.trialStatus = new LinkedList<>();
        
        lock.unlock();
    }
    /**
     * 
     * @return 
     */
    public int getRemainingTrials() {
        int remaining;
        
        lock.lock();
        
        remaining = Constants.NUMBER_OF_TRIALS - this.trialStatus.size();
        
        lock.unlock();
        
        return remaining;
    }
    
    /**
     * 
     * @return 
     */
    public int getRemainingGames() {
        int remaining;
        
        lock.lock();
        
        remaining = Constants.NUMBER_OF_GAMES - this.gameStatus.size();
        
        lock.unlock();
        
        return remaining;
    }
    
    /**
     * The method allows to set the game points for both team.
     * 
     * @param gamePoints Game points of both teams.
     */
    public void addGamePoint(GameScore score) {
        lock.lock();
        
        this.gameStatus.add(score);
        this.trialStatus.clear();
        
        lock.unlock();
    }

    /**
     * The method allows to set the trial points for both team.
     * 
     * @param trialPoints Trial points of both teams.
     */
    public void addTrialPoint(TrialScore score) {
        lock.lock();
        
        this.trialStatus.add(score);
        
        lock.unlock();
    }
    
    public enum TrialScore {
        DRAW(0, "D"),
        VICTORY_TEAM_1(1, "VT1"),
        VICTORY_TEAM_2(2, "VT2");
        
        private int id;
        private String status;
        
        private TrialScore(int id, String status) {
            this.id = id;
            this.status = status;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getStatus() {
            return this.status;
        }
    }
    
    public enum GameScore {
        DRAW(0, "D"),
        VICTORY_TEAM_1_BY_POINTS(1, "VT1PT"),
        VICTORY_TEAM_1_BY_KNOCKOUT(2, "VT1KO"),
        VICTORY_TEAM_2_BY_POINTS(3, "VT2PT"),
        VICTORY_TEAM_2_BY_KNOCKOUT(4, "VT2KO");
        
        private int id;
        private String status;
        
        private GameScore(int id, String status) {
            this.id = id;
            this.status = status;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getStatus() {
            return this.status;
        }
    }
}
