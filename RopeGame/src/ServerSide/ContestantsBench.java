package ServerSide;

import Others.Bench;
import Others.InterfaceContestantsBench;
import ClientSide.Coach;
import ClientSide.Coach.CoachState;
import ClientSide.Contestant;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee;
import RopeGame.Constants;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * General Description:
 * This is an passive class that describes the Contestants bench for each team
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBench extends Bench implements InterfaceContestantsBench {
    private static final ContestantsBench[] instances = new ContestantsBench[2];    // Doubleton containing the two teams benches
    
    // Final fields
    private final int team;                                                         // Team identifier
    private final Lock lock;
    private final Condition allPlayersSeated;
    private final Condition playersSelected;
    private final Condition waitForNextTrial;
    private final Condition waitForCoach;
    
    private final Set<Contestant> bench;                                            // Structure that contains the players in the bench
    private final Set<Integer> selectedContestants;                                 // Selected contestants to play the trial
    
    private boolean coachWaiting;
    
    /**
     * Method that returns a ContestantsBench object. The method is thread-safe
     * and uses the implicit monitor of the class.
     * 
     * @return ContestantsBench object specified by the team identifier passed.
     */
    public static synchronized ContestantsBench getInstance(int team) {
        
        if(instances[team-1] == null) {
            instances[team-1] = new ContestantsBench(team);
        }
        
        return instances[team-1];
    }
    
    /**
     * Gets all the instances of the Contestants Bench
     * @return List containing contestants benches
     */
    public static synchronized List<ContestantsBench> getInstances() {
        List<ContestantsBench> temp = new LinkedList<>();
        
        for(int i = 0; i < instances.length; i++) {
            if(instances[i] == null) {
                instances[i] = new ContestantsBench(i);
            }
            
            temp.add(instances[i]);
        }
        
        return temp;
    }
    
    @Override
    public synchronized List<Bench> getBenches() {

        List<Bench> temp = new LinkedList<>();

        for (int i = 0; i < instances.length; i++) {
            if (instances[i] == null) {
                instances[i] = new ContestantsBench(i);
            }

            temp.add(instances[i]);
        }

        return temp;

    }
    
    
    /**
     * Private constructor to be used in the doubleton.
     * 
     * @param team Team identifier.
     */
    private ContestantsBench(int team) {
        this.team = team;
        
        lock = new ReentrantLock();
        allPlayersSeated = lock.newCondition();
        playersSelected = lock.newCondition();
        waitForNextTrial = lock.newCondition();
        waitForCoach = lock.newCondition();
        bench = new TreeSet<>();
        selectedContestants = new TreeSet<>();
    }
    
    /**
     * The method adds a contestant to the bench.
     * 
     */
    @Override
    public void addContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();
        
        lock.lock();
        
        bench.add(contestant);

        if(contestant.getContestantState() != ContestantState.SEAT_AT_THE_BENCH) {
            contestant.setState(ContestantState.SEAT_AT_THE_BENCH);
            GeneralInformationRepository.getInstance().printLineUpdate();
        }
        
        if(checkAllPlayersSeated()) {
            allPlayersSeated.signal();
        }
        
        try {
            do {
                playersSelected.await();
            } while(!isContestantSelected() && !RefereeSite.getInstance().hasMatchEnded());
        } catch (InterruptedException ex) {
            lock.unlock();
            return;
        } 
            
        lock.unlock();
    }

    /**
     * The method removes a contestant from the bench.
     */
    @Override
    public void getContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();
        
        lock.lock();
        
        bench.remove(contestant);
        
        lock.unlock();
    }

    /**
     * This method returns the bench which contains the Contestants
     * @return List of the contestants in the bench
     */
    @Override
    public Set<Contestant> getBench() {
        Set<Contestant> temp;
        
        lock.lock();
        
        try {
            while(!checkAllPlayersSeated()) {
                allPlayersSeated.await();
            }
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }
        
        temp = new TreeSet<>(this.bench);
        
        lock.unlock();
        
        return temp;
    }

    /**
     * Set selected contestants array.
     * This arrays should be filled with the IDs of the players for the next round.
     * 
     * @param selected identifiers for the selected players
     */
    @Override
    public void setSelectedContestants(Set<Integer> selected) {
        lock.lock();
        
        selectedContestants.clear();
        selectedContestants.addAll(selected);
            
        playersSelected.signalAll();
        
        lock.unlock();
    }
    
    /**
     * Gets the selected contestants to play
     * @return Set with the selected contestants
     */
    @Override
    public Set<Integer> getSelectedContestants() {
        Set<Integer> selected = null;
        
        lock.lock();
        
        selected = new TreeSet<>(this.selectedContestants);
        
        lock.unlock();
        
        return selected;
    }
    
    /**
     * Synchronisation point where the Referee waits for the Coaches to pick the teams
     */
    @Override
    public void pickYourTeam(){
        lock.lock();
        
        
        try {
            while(!coachWaiting)
                waitForCoach.await();
        } catch (InterruptedException ex) {
        }
        
        waitForNextTrial.signal();
        
        lock.unlock();
    }
    
    /**
     * Synchronisation point where Coaches wait for the next trial instructed by the Referee
     */
    @Override
    public void waitForNextTrial() {
        Coach coach = (Coach) Thread.currentThread();
        
        lock.lock();
        
        coach.setState(CoachState.WAIT_FOR_REFEREE_COMMAND);
        GeneralInformationRepository.getInstance().printLineUpdate();
        
        coachWaiting = true;
        waitForCoach.signal();
        
        try {
            waitForNextTrial.await();
        } catch (InterruptedException ex) {}
        
        coachWaiting = false;
        
        lock.unlock();
    }
    
    /**
     * Gets the selected contestants array
     * @return Integer array of the selected contestants for the round
     */
    private boolean isContestantSelected() {
        Contestant contestant = (Contestant) Thread.currentThread();
        boolean result;
        
        lock.lock();
        
        result = selectedContestants.contains(contestant.getContestatId());
        
        lock.unlock();
        
        return result;
    }

    /**
     * Checks if all players are seated on bench
     * @return True if all players seated
     */
    private boolean checkAllPlayersSeated() {
        return bench.size() == Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH;
    }
    
    
}
