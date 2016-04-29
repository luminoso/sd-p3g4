package ServerSide;

import ClientSide.GeneralInformationRepositoryStub;
import ClientSide.RefereeSiteStub;
import Others.InterfaceCoach;
import Others.InterfaceCoach.CoachState;
import Others.InterfaceContestant;
import Others.InterfaceContestant.ContestantState;
import Others.InterfaceContestantsBench;
import Others.InterfaceGeneralInformationRepository;
import Others.InterfaceRefereeSite;
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
 * This is an passive class that describes the contestants bench for each team.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class ContestantsBench implements InterfaceContestantsBench {

    /**
     * Doubleton for ConstestantsBench
     */
    private static final ContestantsBench[] instances = new ContestantsBench[2];    // Doubleton containing the two teams benches

    private final Lock lock;

    /**
     * Conditions for waiting
     */
    private final Condition allPlayersSeated;
    private final Condition playersSelected;
    private final Condition waitForNextTrial;
    private final Condition waitForCoach;

    /**
     * Structure that contains the players in the bench
     */
    private final Set<InterfaceContestant> bench;

    /**
     * Selected contestants to play the trial
     */
    private final Set<Integer> selectedContestants;

    /**
     * Sets if the coach is waiting
     */
    private boolean coachWaiting;

    private int shutdownVotes;
    
    /**
     * 
     */
    private final InterfaceRefereeSite refereeSite;
    
    /**
     * General Information Repository implementation to be used
     */
    private final InterfaceGeneralInformationRepository informationRepository;

    /**
     * Gets all the instances of the Contestants Bench.
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
     * @param team identifier.
     */
    private ContestantsBench() {
        lock = new ReentrantLock();
        allPlayersSeated = lock.newCondition();
        playersSelected = lock.newCondition();
        waitForNextTrial = lock.newCondition();
        waitForCoach = lock.newCondition();
        bench = new TreeSet<>();
        selectedContestants = new TreeSet<>();
        refereeSite = new RefereeSiteStub();
        informationRepository = new GeneralInformationRepositoryStub();
        shutdownVotes = 0;
    }

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
            } while (!isContestantSelected() && !refereeSite.hasMatchEnded());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            lock.unlock();
            return;
        }

        lock.unlock();
    }

    @Override
    public void getContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        lock.lock();

        Iterator<InterfaceContestant> it = bench.iterator();

        while (it.hasNext()) {
            InterfaceContestant temp = it.next();

            if (temp.getContestantId() == contestant.getContestantId()) {
                it.remove();
            }
        }

        lock.unlock();
    }

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

        for (InterfaceContestant contestant : bench) {
            temp.add(new Tuple<>(contestant.getContestantId(), contestant.getContestantStrength()));
        }

        lock.unlock();

        return temp;
    }

    @Override
    public void setSelectedContestants(Set<Integer> selected) {
        lock.lock();

        selectedContestants.clear();
        selectedContestants.addAll(selected);

        playersSelected.signalAll();

        lock.unlock();
    }

    @Override
    public Set<Integer> getSelectedContestants() {
        Set<Integer> selected = null;

        lock.lock();

        selected = new TreeSet<>(this.selectedContestants);

        lock.unlock();

        return selected;
    }

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
        } catch (InterruptedException ex) {}

        coachWaiting = false;

        lock.unlock();
    }

    @Override
    public void updateContestantStrength(int id, int delta) {
        lock.lock();

        for (InterfaceContestant contestant : bench) {
            if (contestant.getContestantId() == id) {
                contestant.setContestantStrength(contestant.getContestantStrength() + delta);
                informationRepository.updateContestantStrength(contestant.getContestantTeam(),
                        contestant.getContestantId(), contestant.getContestantStrength());
                informationRepository.printLineUpdate();
            }
        }

        lock.unlock();
    }

    /**
     * 
     */
    @Override
    public void interrupt() {
        lock.lock();
        
        while (!checkAllPlayersSeated()) {
            try {
                allPlayersSeated.await();
            } catch (InterruptedException ex) {}
        }
        playersSelected.signalAll();
        
        while (!coachWaiting) {
            try {
                waitForCoach.await();
            } catch (InterruptedException ex) {}
        }
        waitForNextTrial.signal();
        
        lock.unlock();
    }
    
    @Override
    public boolean shutdown() {
        boolean result = false;
        
        lock.lock();
        
        shutdownVotes++;
        
        result = shutdownVotes == (1 + 2 * (1 + Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH));
        
        lock.unlock();
        
        return result;
    }
    
    /**
     * Gets the selected contestants array.
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
     * Checks if all players are seated on bench.
     *
     * @return True if all players seated
     */
    private boolean checkAllPlayersSeated() {
        return bench.size() == Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH;
    }
}
