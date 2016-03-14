package Passive;

import Active.Coach;
import Active.Contestant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * General Description: 
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Playground {
    private static Playground instance;
    
    private Lock lock;
    private Condition startTrial;
    private Condition teamsInPosition;
    private Condition finishedPulling;
    private Condition resultAssert;
    private int pullCounter;
    private int flagPosition;
    private int lastFlagPosition;
    private List<Contestant>[] teams;
    
    /**
     * The method returns the Playground object. This method is thread-safe and 
     * uses the implicit monitor of the class.
     * 
     * @return Playground object to be used.
     */
    public static synchronized Playground getInstance() {
        if(instance == null) {
            instance = new Playground();
        }
        
        return instance;
    }
    
    /**
     * Private constructor to be used in the singleton.
     */
    private Playground() {
        this.flagPosition = 0;
        this.lock = new ReentrantLock();
        this.startTrial = this.lock.newCondition();
        this.teamsInPosition = this.lock.newCondition();
        this.finishedPulling = this.lock.newCondition();
        this.resultAssert = this.lock.newCondition();
        this.pullCounter = 0;
        this.teams = new List[2];
        this.teams[0] = new ArrayList<>();
        this.teams[1] = new ArrayList<>();
    }
    
    /**
     * The method adds a contestant to the playground.
     * 
     */
    public void addContestant(){
        Contestant contestant = (Contestant) Thread.currentThread();
        
        lock.lock();
        
        try {
            this.teams[contestant.getContestantTeam()-1].add(contestant);
            
            if(isTeamInPlace(contestant.getContestantTeam()-1)) {
                this.teamsInPosition.signalAll();
            }
            
            startTrial.await();
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 
     */
    public void checkTeamPlacement() {
        Coach coach = (Coach) Thread.currentThread();
        
        lock.lock();
        
        try {
            while(!isTeamInPlace(coach.getCoachTeam()-1)) {
                this.teamsInPosition.await();
            }
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 
     */
    public void watchTrial() {
        lock.lock();
        
        try {
            this.resultAssert.await();
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 
     */
    public void finishedPullingRope() {
        lock.lock();
        
        try {
            this.pullCounter++;
            
            if(haveAllPulled()) {
                this.finishedPulling.signal();
            }
            
            this.resultAssert.await();
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 
     */
    public void resultAsserted() {
        lock.lock();
        
        try {
            this.pullCounter = 0;
            
            this.resultAssert.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * The method removes the contestant from the playground.
     * 
     * @return Contestant specified for removal.
     */
    public boolean getContestant(){
        Contestant contestant = (Contestant) Thread.currentThread();
        boolean result;
        
        lock.lock();
        
        result = teams[contestant.getContestantTeam()-1].remove(contestant);
        
        lock.unlock();
        
        return result;
    }
    
    /**
     * The method returns the flag position in relation to the middle. 
     * Middle = 0.
     * 
     * @return Position of the flag. 
     */
    public int getFlagPosition(){
        int result;
        
        lock.lock();
        
        result = this.flagPosition;
        
        lock.unlock();
        
        return result;
    }

    /**
     * The method sets the flag position in relation to the middle. Middle = 0.
     * 
     * @param flagPosition Position of the flag.
     */
    public void setFlagPosition(int flagPosition) {
        lock.lock();
        
        this.lastFlagPosition = this.flagPosition;
        this.flagPosition = flagPosition;
        
        lock.unlock();
    }

    /**
     * The method gets the flag position in the begging of the match/trial
     * @return 
     */
    public int getLastFlagPosition() {
        int result;
        
        lock.lock();
        
        result = lastFlagPosition;
        
        lock.unlock();
        
        return result;
    }

    private boolean isTeamInPlace(int teamId) {
        return this.teams[teamId].size() == 3;
    }

    private boolean haveAllPulled() {
        return this.pullCounter == (this.teams[0].size() + this.teams[1].size());
    }
    
    /**
     * Checks if all contestants are in the playground and ready to play the game
     * @return True if all 6 players are in game
     */
    public boolean checkAllContestantsReady(){
        return (teams[0].size() + teams[1].size()) == 6;
    }

    public List<Contestant>[] getTeams() {
        return teams;
    }
    
    
    
}
