package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBench {
    private static final ContestantsBench[] instances = new ContestantsBench[2];    // Doubleton containing the two teams benches
    
    private Lock lock;
    
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
        return team;
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
        lock.lock();
        try {
            for (Contestant contestant : bench) {
                if (contestant.getId() == id) {
                    bench.remove(contestant);
                    return contestant;
                }
            }
        } finally {
            lock.unlock();
        }

        return null;
    }

    /**
     * This method returns the bench which contains the Contestants
     * @return List of the contestants in the bench
     */
    public List<Contestant> getBench() {
        lock.lock();
        try {
            return bench;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Sorts the bench by Contestants strength 
     */
    public void sort() {
        lock.lock();
        try {
            Collections.sort(this.bench);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get players ID present at the bench by Contestant ID
     * @return Integer array of contestants IDs
     */
    public int[] getIDs() {
        int[] ids = new int[bench.size()];
        
        lock.lock();
        try {
            for (int i = 0; i < bench.size(); i++) {
                ids[i] = bench.get(i).getContestantId();
            }
        } finally {
            lock.unlock();
        }
        
        return ids;
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
    
}