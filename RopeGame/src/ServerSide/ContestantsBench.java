package ServerSide;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.GeneralInformationRepositoryStub;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfaceContestantsBench;
import Others.Tuple;
import RopeGame.Constants;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * General Description: This is an passive class that describes the Contestants
 * bench for each team
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBench implements InterfaceContestantsBench {

    /**
     *
     */
    private static final ContestantsBench[] instances = new ContestantsBench[2];    // Doubleton containing the two teams benches

    /**
     *
     */
    private final Lock lock;

    /**
     *
     */
    private final Condition allPlayersSeated;

    /**
     *
     */
    private final Condition playersSelected;

    /**
     *
     */
    private final Condition waitForNextTrial;

    /**
     *
     */
    private final Condition waitForCoach;

    /**
     *
     */
    private final Set<InterfaceContestant> bench;                                   // Structure that contains the players in the bench

    /**
     *
     */
    private final Set<Integer> selectedContestants;                                 // Selected contestants to play the trial

    /**
     *
     */
    private boolean coachWaiting;

    private final GeneralInformationRepositoryStub informationRepository;
    
    /**
     * Gets all the instances of the Contestants Bench
     *
     * @return List containing contestants benches
     */
    public static synchronized List<ContestantsBench> getInstances() {
        List<ContestantsBench> temp = new LinkedList<>();

        for (int i = 0; i < instances.length; i++) {
            if (instances[i] == null) {
                instances[i] = new ContestantsBench();
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
    private ContestantsBench() {
        lock = new ReentrantLock();
        allPlayersSeated = lock.newCondition();
        playersSelected = lock.newCondition();
        waitForNextTrial = lock.newCondition();
        waitForCoach = lock.newCondition();
        bench = new TreeSet<>();
        selectedContestants = new TreeSet<>();
        informationRepository = new GeneralInformationRepositoryStub();
    }

    /**
     * The method adds a contestant to the bench.
     */
    @Override
    public void addContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        lock.lock();

        bench.add(contestant);

        if (contestant.getContestantState() != ContestantState.SEAT_AT_THE_BENCH) {
            contestant.setContestantState(ContestantState.SEAT_AT_THE_BENCH);
            informationRepository.updateContestant();
            informationRepository.printLineUpdate();
        }

        if (checkAllPlayersSeated()) {
            allPlayersSeated.signal();
        }

        try {
            do {
                playersSelected.await();
            } while (!isContestantSelected() && !RefereeSite.getInstance().hasMatchEnded());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
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
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        lock.lock();

        Iterator<InterfaceContestant> it = bench.iterator();

        while(it.hasNext()) {
            InterfaceContestant temp = it.next();
            
            if(temp.getContestantId() == contestant.getContestantId())
                it.remove();
        }
        
        lock.unlock();
    }

    /**
     * This method returns the bench which contains the Contestants
     *
     * @return List of the contestants in the bench
     */
    @Override
    public Set<Tuple<Integer, Integer>> getBench() {
        Set<Tuple<Integer, Integer>> temp;

        lock.lock();

        try {
            while (!checkAllPlayersSeated()) {
                allPlayersSeated.await();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            lock.unlock();
            return null;
        }

        temp = new HashSet<>();

        for(InterfaceContestant contestant : bench) {
            temp.add(new Tuple<>(contestant.getContestantId(), contestant.getContestantStrength()));
        }
        
        lock.unlock();

        return temp;
    }

    /**
     * Set selected contestants array. This arrays should be filled with the IDs
     * of the players for the next round.
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
     *
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
     * Synchronisation point where the Referee waits for the Coaches to pick the
     * teams
     */
    @Override
    public void pickYourTeam() {
        lock.lock();

        try {
            while (!coachWaiting) {
                waitForCoach.await();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        waitForNextTrial.signal();

        lock.unlock();
    }

    /**
     * Synchronisation point where Coaches wait for the next trial instructed by
     * the Referee
     */
    @Override
    public void waitForNextTrial() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        lock.lock();

        coach.setCoachState(CoachState.WAIT_FOR_REFEREE_COMMAND);
        informationRepository.updateCoach();
        informationRepository.printLineUpdate();

        coachWaiting = true;
        waitForCoach.signal();

        try {
            waitForNextTrial.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        coachWaiting = false;

        lock.unlock();
    }

    @Override
    public void updateContestantStrength(int id, int delta) {
        lock.lock();
        
        for(InterfaceContestant contestant : bench) {
            if(contestant.getContestantId() == id) {
                contestant.setContestantStrength(contestant.getContestantStrength() + delta);
                informationRepository.updateContestantStrength(contestant.getContestantTeam(), 
                        contestant.getContestantId(), contestant.getContestantStrength());
                informationRepository.printLineUpdate();
            }
        }
        
        lock.unlock();
    }
    
    /**
     * Gets the selected contestants array
     *
     * @return Integer array of the selected contestants for the round
     */
    private boolean isContestantSelected() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
        boolean result;

        lock.lock();

        result = selectedContestants.contains(contestant.getContestantId());

        lock.unlock();

        return result;
    }

    /**
     * Checks if all players are seated on bench
     *
     * @return True if all players seated
     */
    private boolean checkAllPlayersSeated() {
        return bench.size() == Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH;
    }
}
