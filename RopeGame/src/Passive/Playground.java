package Passive;

import Active.Coach;
import Active.Contestant;
import RopeGame.Constants;
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
            lock.unlock();
            return;
        }
            
        lock.unlock();
    }
    
    /**
     * 
     */
    public void watchTrial() {
        lock.lock();
        
        try {
            this.resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return;
        }
            
        lock.unlock();
    }
    
    /**
     * 
     */
    public void pullRope() {
        lock.lock();
        
        try {
            long waitTime = (long) (Constants.MINIMUM_WAIT_TIME + Math.random() * (Constants.MAXIMUM_WAIT_TIME - Constants.MINIMUM_WAIT_TIME));
            
            Thread.currentThread().wait(waitTime);
            
            this.pullCounter++;
            
            if(haveAllPulled()) {
                updateFlagPosition();
                this.finishedPulling.signal();
            }
            
            this.resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return;
        }
            
        lock.unlock();
    }
    
    /**
     * 
     */
    public void resultAsserted() {
        lock.lock();
        
        this.pullCounter = 0;
            
        this.resultAssert.signalAll();
        
        lock.unlock();
    }
    
    /**
     * The method removes the contestant from the playground.
     * 
     */
    public void getContestant(){
        Contestant contestant = (Contestant) Thread.currentThread();
        
        lock.lock();
        
        teams[contestant.getContestantTeam()-1].remove(contestant);
        
        lock.unlock();
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

    private boolean isTeamInPlace(int teamId) {
        return this.teams[teamId].size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND;
    }

    private boolean haveAllPulled() {
        return this.pullCounter == 2 * Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND;
    }

    /**
     * 
     * @return 
     */
    public boolean checkAllContestantsReady(){
        return (teams[0].size() + teams[1].size()) == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND * 2;
    }

    public List<Contestant>[] getTeams() {
        List<Contestant>[] teams = new List[2];
        
        lock.lock();
        
        teams[0] = new ArrayList<>(this.teams[0]);
        teams[1] = new ArrayList<>(this.teams[1]);
        
        lock.unlock();
        
        return teams;
    }

    /**
     * 
     */
    private void updateFlagPosition() {
        int team1 = 0;
        int team2 = 0;
        
        for(Contestant contestant : this.teams[0]) {
            team1 += contestant.getContestantStrength();
        }
        
        for(Contestant contestant : this.teams[1]) {
            team2 += contestant.getContestantStrength();
        }
        
        if(team1 > team2) {
            this.flagPosition--;
        } else if(team1 < team2) {
            this.flagPosition++;
        }
    }    
}
