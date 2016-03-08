package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General Description:
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBench {
    private static final ContestantsBench[] instances = new ContestantsBench[2];    // Doubleton containing the two teams benches
    
    private Lock lock;
    private Condition allPlayersSeated;
    private Condition playersSelected;
    
    private List<Contestant> bench;                                                 // Structure that contains the players in the bench
    private int[] selectedContestants;                                              // Selected contestants to play the trial
    private int team;                                                               // Team identifier
  
    /**
     * Method that returns a ContestantsBench object. The method is thread-safe
     * and uses the implicit monitor of the class.
     * 
     * @param id Team identifier.
     * @return ContestantsBench object specified by the team identifier passed.
     */
    public static synchronized ContestantsBench getInstance(int id) {
        if(instances[id-1] == null) {
            instances[id-1] = new ContestantsBench(id);
        }
        
        return instances[id-1];
    }
    
    /**
     * Private constructor to be used in the doubleton.
     * 
     * @param team Team identifier.
     */
    private ContestantsBench(int team) {
        this.lock = new ReentrantLock();
        this.allPlayersSeated = this.lock.newCondition();
        this.playersSelected = this.lock.newCondition();
        this.team = team;
        this.bench = new ArrayList<>();
        this.selectedContestants = new int[3];
    }

    /**
     * The method returns the team identifier that is in the bench.
     * 
     * @return Team identifier.
     */
    public int getTeam() {
        lock.lock();
        
        try {
            return team;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * The method adds a contestant to the bench.
     * 
     * @param contestant Contestant that belongs to the team and needs to be 
     * added to the bench.
     */
    public void addContestant(Contestant contestant) {
        lock.lock();
        
        try {
            bench.add(contestant);
            
            if(checkAllPlayersSeated()) {
                allPlayersSeated.signal();
            }
            
            while(checkPlayerSelected()) {
                playersSelected.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
    }

    /**
     * The method removes a contestant from the bench.
     *
     * @param id Contestant identifier.
     * @return Contestant that needed to be removed.
     */
    public Contestant getContestant(int id) {
        Contestant cont = null;
        
        lock.lock();
        
        try {
            Iterator<Contestant> it = bench.iterator();
            while(it.hasNext()) {
                cont = it.next();
                
                if(cont.getContestantId() == id) {
                    it.remove();
                    break;
                }
                
                cont = null;
            }
            
            return cont;
        } finally {
            lock.unlock();
        }
    }

    /**
     * This method returns the bench which contains the Contestants
     * @return List of the contestants in the bench
     */
    public List<Contestant> getBench() {
        List<Contestant> temp = null;
        
        lock.lock();
        
        try {
            while(checkAllPlayersSeated() != true) {
                allPlayersSeated.await();
            }
            
            temp = bench;
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        } finally {
            lock.unlock();
        }
        
        return bench;
    }

    /**
     * Set selected contestants array.
     * This arrays should be filled with the IDs of the players for the next round.
     * 
     * @param selectedContestants
     */
    public void setSelectedContestants(int[] selectedContestants) {
        lock.lock();
        
        try {
            this.selectedContestants = selectedContestants;
            
            playersSelected.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Gets the selected contestants array
     * @return Integer array of the selected contestants for the round
     */
    public int[] getSelectedContestants() {
        lock.lock();
        
        try {
            return selectedContestants;
        } finally {
            lock.unlock();
        }
    }

    private boolean checkPlayerSelected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean checkAllPlayersSeated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
