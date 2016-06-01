package ServerSide.ContestantsBench;

import Interfaces.InterfaceContestantsBench;
import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfaceRefereeSite;
import Others.CoachState;
import Others.ContestantState;
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

/**
 * This is an passive class that describes the contestants bench for each team
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class ContestantsBench implements InterfaceContestantsBench {
    // conditions for waiting
    private final Lock lock;
    private final Condition[] allPlayersSeated;
    private final Condition[] playersSelected;
    private final Condition[] waitForNextTrial;
    private final Condition[] waitForCoach;

    // structure that contains the players in the bench
    private final List<Triple<Integer, ContestantState, Integer>>[] bench;

    // selected contestants to play the trial
    private final List<Integer>[] selectedContestants;

    private boolean[] coachWaiting; // sets if the coach is waiting
    
    private static int shutdownVotes; // counts if everyone's ready to shutdown

    // referee site implementation to be used
    private final InterfaceRefereeSite refereeSite;

    // general Information repository implementation to be used
    private final InterfaceGeneralInformationRepository informationRepository;
    
    private VectorTimestamp vt;

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
        
        bench = new List[2];
        selectedContestants = new List[2];
        
        for(int i = 0; i < 2; i++) {
            allPlayersSeated[i] = lock.newCondition();
            playersSelected[i] = lock.newCondition();
            waitForNextTrial[i] = lock.newCondition();
            waitForCoach[i] = lock.newCondition();
            coachWaiting[i] = false;
            
            bench[i] = new ArrayList<>();
            selectedContestants[i] = new ArrayList<>();
        }
        
        refereeSite = refSiteInt;
        informationRepository = girInt;
        
        shutdownVotes = 0;
        
        vt = new VectorTimestamp(Constants.VECTOR_TIMESTAMP_SIZE, 0);
    }

    @Override
    public Triple<VectorTimestamp, Integer, Integer> addContestant(int id, int team, int state, int strength, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        // (id, state, strength) defines the contestant
        bench[team-1].add(new Triple<>(id, ContestantState.SEAT_AT_THE_BENCH, strength));

        if (ContestantState.getStateById(state) != ContestantState.SEAT_AT_THE_BENCH) {
            // informationRepository.updateContestant();
            // informationRepository.printLineUpdate();
        }

        if (checkAllPlayersSeated(team)) {
            allPlayersSeated[team-1].signalAll();
        }

        try {
            do {
                playersSelected[team-1].await();
            } while (!isContestantSelected(id, team) && !refereeSite.hasMatchEnded());
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        // returns vt, state Id and strength
        Triple<VectorTimestamp,Integer,Integer> tmp = null;
        
        vt.update(vt);
        for(Triple<Integer,ContestantState,Integer> ct : bench[team-1]){
            if(ct.getFirst() == id)
                tmp = new Triple<>(vt.clone(), ct.getSecond().getId(), ct.getThird());
        }
        
        lock.unlock();
        
        return tmp;
    }

    @Override
    public void getContestant(int id, int team) throws RemoteException {
        lock.lock();
        
        Iterator<Triple<Integer, ContestantState, Integer>> it = bench[team-1].iterator();
        
        while (it.hasNext()) {
            Triple<Integer, ContestantState, Integer> temp = it.next();

            if (temp.getFirst() == id) {
                it.remove();
                break;
            }
        }
        
        lock.unlock();
    }

    @Override
    public Tuple<VectorTimestamp,Set<Tuple<Integer, Integer>>> getBench(int team, VectorTimestamp vt) throws RemoteException {
        
        // id, strenght. State is for sure SEAT_AT_THE_BENCH
        Set<Tuple<Integer, Integer>> temp;

        lock.lock();

        try {
            while (!checkAllPlayersSeated(team)) {
                allPlayersSeated[team-1].await();
            }
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        temp = new HashSet<>();

        // id, state, strenght
        for (Triple<Integer, ContestantState, Integer> contestant : bench[team-1])
            temp.add(new Tuple<>(contestant.getFirst(), contestant.getThird()));
        
        vt.update(vt);
        
        lock.unlock();

        return new Tuple<>(vt.clone(), temp);
    }

    @Override
    public VectorTimestamp setSelectedContestants(int team, Set<Integer> selected, VectorTimestamp vt) throws RemoteException  {
        lock.lock();

        selectedContestants[team-1].clear();
        selectedContestants[team-1].addAll(selected);

        playersSelected[team-1].signalAll();

        vt.update(vt);
       
        lock.unlock();
        
        return vt.clone();
    }

    @Override
    public Tuple<VectorTimestamp,Set<Integer>> getSelectedContestants(int team, VectorTimestamp vt) throws RemoteException  {
        Set<Integer> selected;

        lock.lock();

        selected = new TreeSet<>(selectedContestants[team-1]);

        vt.update(vt);
        
        lock.unlock();

        return new Tuple<>(vt.clone(),selected);
    }

    @Override
    public void pickYourTeam(int team) throws RemoteException {
        lock.lock();

        try {
            while (!coachWaiting[team-1]) {
                waitForCoach[team-1].await();
            }
        } catch (InterruptedException ex) {
            lock.unlock();
        }

        waitForNextTrial[team-1].signal();

        lock.unlock();
        
    }

    @Override
    public Tuple<VectorTimestamp,Integer> waitForNextTrial(int team, VectorTimestamp vt) throws RemoteException{
        lock.lock();

        // informationRepository.updateCoach();
        // informationRepository.printLineUpdate();

        coachWaiting[team-1] = true;
        waitForCoach[team-1].signal();

        try {
            waitForNextTrial[team-1].await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        coachWaiting[team-1] = false;

        vt.update(vt);
        
        lock.unlock();
        
        return new Tuple<>(vt.clone(), CoachState.WAIT_FOR_REFEREE_COMMAND.getId());
    }

    @Override
    public VectorTimestamp updateContestantStrength(int id, int team, int delta, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        Triple<Integer, ContestantState, Integer> sub = null;
        Iterator<Triple<Integer, ContestantState, Integer>> it = bench[team-1].iterator();

        while (it.hasNext()) {
            Triple<Integer, ContestantState, Integer> temp = it.next();

            if (temp.getFirst() == id) {
                it.remove();
                sub = new Triple<>(id, temp.getSecond(), temp.getThird() + delta);
                //informationRepository.updateContestantStrength(contestant.getContestantTeam(),
                //        contestant.getContestantId(), contestant.getContestantStrength());
                //informationRepository.printLineUpdate();
                bench[team-1].add(sub);
                break;
            }
        }
        
        vt.update(vt);

        lock.unlock();
        
        return vt.clone();
    }

    @Override
    public void interrupt(int team) throws RemoteException {
        lock.lock();

        while (!checkAllPlayersSeated(team)) {
            try {
                allPlayersSeated[team-1].await();
            } catch (InterruptedException ex) {
                lock.unlock();
                return;
            }
        }
        
        playersSelected[team-1].signalAll();

        while (!coachWaiting[team-1]) {
            try {
                waitForCoach[team-1].await();
            } catch (InterruptedException ex) {
                lock.unlock();
                return;
            }
        }
        
        waitForNextTrial[team-1].signal();

        lock.unlock();
    }

    @Override
    public VectorTimestamp waitForEveryoneToStart(int team, VectorTimestamp vt) {
        
        //TODO possible useless function in RMI
        
        lock.lock();

        vt.update(vt);
        
        while (!checkAllPlayersSeated(team)) {
            try {
                allPlayersSeated[team-1].await();
            } catch (InterruptedException ex) {
                lock.unlock();
            }
        }

        while (!coachWaiting[team-1]) {
            try {
                waitForCoach[team-1].await();
            } catch (InterruptedException ex) {
                lock.unlock();
            }
        }

        vt.update(vt);
        
        lock.unlock();
        
        return vt.clone();
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
    private boolean isContestantSelected(int id, int team) {
        boolean result = false;

        Iterator<Integer> it = selectedContestants[team-1].iterator();

        while (it.hasNext()) {
            if (id == it.next()) {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * Checks if all players are seated on bench.
     *
     * @return true if all players seated
     */
    private boolean checkAllPlayersSeated(int team) {
        return bench[team-1].size() == Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH;
    }
}
