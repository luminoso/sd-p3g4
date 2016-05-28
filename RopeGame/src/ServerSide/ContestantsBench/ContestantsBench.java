package ServerSide.ContestantsBench;

import Interfaces.InterfaceContestantsBench;
import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfaceRefereeSite;
import Others.InterfaceCoach;
import Others.InterfaceCoach.CoachState;
import Others.InterfaceContestant;
import Others.InterfaceContestant.ContestantState;
import Others.Triple;
import Others.Tuple;
import Others.VectorTimestamp;
import RopeGame.Constants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an passive class that describes the contestants bench for each team
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class ContestantsBench implements InterfaceContestantsBench {
    // conditions for waiting
    private final Lock lock;
    private final Condition[] allPlayersSeated;
    private final Condition[] playersSelected;
    private final Condition[] waitForNextTrial;
    private final Condition[] waitForCoach;

    // structure that contains the players in the bench
    private final List<List<Triple<Integer, ContestantState, Integer>>> bench;

    // selected contestants to play the trial
    private final List<List<Integer>> selectedContestants;

    private boolean[] coachWaiting; // sets if the coach is waiting
    
    private static int shutdownVotes; // counts if everyone's ready to shutdown

    // referee site implementation to be used
    private final InterfaceRefereeSite refereeSite;

    // general Information repository implementation to be used
    private final InterfaceGeneralInformationRepository informationRepository;

    /**
     * Private constructor to be used in the doubleton
     * @param refSiteInt
     * @param girInt
     */
    public ContestantsBench(InterfaceRefereeSite refSiteInt, InterfaceGeneralInformationRepository girInt) {
        lock = new ReentrantLock();
        
        allPlayersSeated = new Condition[2];
        playersSelected = new Condition[2];
        waitForNextTrial = new Condition[2];
        waitForCoach = new Condition[2];
        coachWaiting = new boolean[2];
        
        bench = new ArrayList<>();
        selectedContestants = new ArrayList<>();
        
        for(int i = 0; i < 2; i++) {
            allPlayersSeated[i] = lock.newCondition();
            playersSelected[i] = lock.newCondition();
            waitForNextTrial[i] = lock.newCondition();
            waitForCoach[i] = lock.newCondition();
            coachWaiting[i] = false;
            
            bench.add(new ArrayList<>());
            selectedContestants.add(new ArrayList<>());
        }
        
        refereeSite = refSiteInt;
        informationRepository = girInt;
        
        shutdownVotes = 0;
    }

    @Override
    public Triple<VectorTimestamp, Integer, Integer> addContestant(int id, int team, int state, int strength, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        bench.get(team-1).add(new Triple<>(id, ContestantState.getById(state), strength));

        if (contestant.getContestantState() != ContestantState.SEAT_AT_THE_BENCH) {
            contestant.setContestantState(ContestantState.SEAT_AT_THE_BENCH);
            informationRepository.updateContestant();
            informationRepository.printLineUpdate();
        }

        if (checkAllPlayersSeated()) {
            allPlayersSeated.signalAll();
        }

        try {
            do {
                playersSelected.await();
            } while (!isContestantSelected() && !refereeSite.hasMatchEnded());
        } catch (InterruptedException ex) {
            Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
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
        Set<Integer> selected;

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
            Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (InterruptedException ex) {
            Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    @Override
    public void interrupt() {
        lock.lock();

        while (!checkAllPlayersSeated()) {
            try {
                allPlayersSeated.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        playersSelected.signalAll();

        while (!coachWaiting) {
            try {
                waitForCoach.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        waitForNextTrial.signal();

        lock.unlock();
    }

    @Override
    public void waitForEveryoneToStart() {
        lock.lock();

        while (!checkAllPlayersSeated()) {
            try {
                allPlayersSeated.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        while (!coachWaiting) {
            try {
                waitForCoach.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(ContestantsBench.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        lock.unlock();
    }

    @Override
    public boolean shutdown() {
        boolean result;

        lock.lock();

        shutdownVotes++;

        result = shutdownVotes == (1 + 2 * (1 + Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH));

        lock.unlock();

        return result;
    }

    /**
     * Gets the selected contestants array.
     *
     * @return integer array of the selected contestants for the round
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
     * @return true if all players seated
     */
    private boolean checkAllPlayersSeated() {
        return bench.size() == Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH;
    }
}
