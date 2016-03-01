package Passive;

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
    
    private int[] gamePoints, trialPoints;
    private int gameRound, trialRound;

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
        
        this.gameRound = 0;
        this.trialRound = 0;
    
        this.trialPoints = new int[2];
        this.gamePoints = new int[2];
    }

    /**
     * The method returns the game round number.
     * 
     * @return Game round number.
     */
    public int getGameRound() {
        lock.lock();
        
        try {
            return gameRound;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method returns the trial round number.
     * 
     * @return Trial round number.
     */
    public int getTrialRound() {
        lock.lock();
        
        try {
            return trialRound;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method returns the game points in the form of an array.
     * 
     * @return Game points.
     */
    public int[] getGamePoints() {
        lock.lock();
        
        try {
            return gamePoints;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method returns the trial points in the form of an array.
     * 
     * @return Trial points.
     */
    public int[] getTrialPoints() {
        lock.lock();
        
        try {
            return trialPoints;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method allows to set the value of the number of game rounds played.
     * 
     * @param gameRound Number of game rounds.
     */
    public void setGameRound(int gameRound) {
        lock.lock();
        
        try {
            this.gameRound = gameRound;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method allows to set the value of the number of trial rounds played.
     * 
     * @param trialRound Number of trial rounds.
     */
    public void setTrialRound(int trialRound) {
        lock.lock();
        
        try {
            this.trialRound = trialRound;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method allows to set the game points for both team.
     * 
     * @param gamePoints Game points of both teams.
     */
    public void setGamePoints(int[] gamePoints) {
        lock.lock();
        
        try {
            this.gamePoints = gamePoints;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method allows to set the trial points for both team.
     * 
     * @param trialPoints Trial points of both teams.
     */
    public void setTrialPoints(int[] trialPoints) {
        lock.lock();
        
        try {
            this.trialPoints = trialPoints;
        } finally {
            lock.unlock();
        }
    }
}
