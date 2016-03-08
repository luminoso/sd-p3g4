package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
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
    private boolean assertedResult;
    private boolean trialStarted;
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
     * @param teamId Team identifier.
     * @param contestant Contestant to be added.
     */
    public void addContestantToTeam(int teamId, Contestant contestant){
        lock.lock();
        try {
            this.teams[teamId-1].add(contestant);
            
            if(isTeamInPlace(teamId-1)) {
                this.teamsInPosition.signalAll();
            }
            
            while(!wasTrialStarted()) {
                startTrial.await();
            }
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 
     * @param teamId 
     */
    public void checkTeamPlacement(int teamId) {
        lock.lock();
        
        try {
            while(!isTeamInPlace(teamId)) {
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
            while(!isResultAsserted()) {
                this.resultAssert.await();
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
    public void finishedPullingRope() {
        lock.lock();
        try {
            this.pullCounter++;
            
            if(haveAllPulled()) {
                this.finishedPulling.signal();
            }
            
            while(!isResultAsserted()) {
                this.resultAssert.await();
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
     * @param teamId Team identifier.
     * @param contestantId Contestant identifier.
     * @return Contestant specified for removal.
     */
    public Contestant getContestantFromTeam(int teamId, int contestantId){
        for(Contestant contestant : this.teams[teamId-1]){
            if(contestant.getId() == contestantId){
                this.teams[teamId-1].remove(contestant);
                return contestant;
            }
        }
        
        return null;
    }
    
    /**
     * The method returns the flag position in relation to the middle. 
     * Middle = 0.
     * 
     * @return Position of the flag. 
     */
    public int getFlagPosition(){
        return this.flagPosition;
    }

    /**
     * The method sets the flag position in relation to the middle. Middle = 0.
     * 
     * @param flagPosition Position of the flag.
     */
    public void setFlagPosition(int flagPosition) {
        this.lastFlagPosition = this.flagPosition;
        this.flagPosition = flagPosition;
    }

    /**
     * The method gets the flag position in the begging of the match/trial
     * @return 
     */
    public int getLastFlagPosition() {
        return lastFlagPosition;
    }

    private boolean wasTrialStarted() {
        return this.trialStarted;
    }

    private boolean isTeamInPlace(int teamId) {
        return this.teams[teamId].size() == 3;
    }

    private boolean isResultAsserted() {
        return this.assertedResult;
    }

    private boolean haveAllPulled() {
        return this.pullCounter == (this.teams[0].size() + this.teams[1].size());
    }
}
