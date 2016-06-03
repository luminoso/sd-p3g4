package serverSide;

import interfaces.InterfaceGeneralInformationRepository;
import interfaces.InterfacePlayground;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import others.CoachState;
import others.Constants;
import others.ContestantState;
import others.InterfaceContestant;
import others.RefereeState;
import others.Triple;
import others.Tuple;
import others.VectorTimestamp;

/**
 * General Description: This is an passive class that describes the Playground
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class Playground implements InterfacePlayground {

    // locking and waiting condtions
    private final Lock lock;
    private final Condition startTrial;         // condition for waiting the trial start
    private final Condition teamsInPosition;    // condition for waiting to the teams to be in position
    private final Condition finishedPulling;    // condition for waiting the contestants finished pulling the rope
    private final Condition resultAssert;       // condition for waiting for the result to be asserted

    private int pullCounter;                    // how many pulls the contestants made
    private int flagPosition;                   // current flag position
    private int lastFlagPosition;               // last flag position
    private int shutdownVotes;                  // count if all votes are met to shutdown

    private final List<Triple<Integer, ContestantState, Integer>>[] teams;  // list containing the Contestant in both teams
    private final InterfaceGeneralInformationRepository informationRepository;

    private VectorTimestamp local;

    /**
     * Private constructor to be used in the singleton
     *
     * @param girInt
     */
    public Playground(InterfaceGeneralInformationRepository girInt) {
        lock = new ReentrantLock();
        startTrial = lock.newCondition();
        teamsInPosition = lock.newCondition();
        finishedPulling = lock.newCondition();
        resultAssert = lock.newCondition();

        flagPosition = 0;
        lastFlagPosition = 0;
        pullCounter = 0;
        teams = new List[2];

        for (int i = 0; i < 2; i++) {
            teams[i] = new ArrayList<>();
        }

        informationRepository = girInt;

        shutdownVotes = 0;

        local = new VectorTimestamp(Constants.VECTOR_TIMESTAMP_SIZE, 0);
    }

    @Override
    public Tuple<VectorTimestamp, Integer> addContestant(int id, int team, int state, int strength, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        local.update(vt);
        
        try {
            teams[team - 1].add(new Triple<>(id, ContestantState.STAND_IN_POSITION, strength));

            informationRepository.updateContestant(id, team, ContestantState.STAND_IN_POSITION.getId(), strength, local.clone());
            informationRepository.setTeamPlacement(id, team, local.clone());

            if (isTeamInPlace(team)) {
                teamsInPosition.signalAll();
            }

            startTrial.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        lock.unlock();

        // Contestant changes state
        return new Tuple<>(local.clone(), ContestantState.STAND_IN_POSITION.getId());
    }

    @Override
    public Tuple<VectorTimestamp, Integer> checkTeamPlacement(int team, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        local.update(vt);
        
        informationRepository.updateCoach(team, CoachState.ASSEMBLE_TEAM.getId(), local.clone());
        
        try {
            while (!isTeamInPlace(team)) {
                teamsInPosition.await();
            }
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        vt.update(vt);

        lock.unlock();

        return new Tuple<>(local.clone(), CoachState.ASSEMBLE_TEAM.getId());
    }

    @Override
    public Tuple<VectorTimestamp, Integer> watchTrial(int team, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        local.update(vt);
        
        informationRepository.updateCoach(team, CoachState.WATCH_TRIAL.getId(), local.clone());
        
        try {
            resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        lock.unlock();

        return new Tuple<>(local.clone(), CoachState.WATCH_TRIAL.getId());
    }

    @Override
    public void pullRope() throws RemoteException {
        lock.lock();

        try {
            long waitTime = (long) (Constants.MINIMUM_WAIT_TIME + Math.random() * (Constants.MAXIMUM_WAIT_TIME - Constants.MINIMUM_WAIT_TIME));
            Thread.currentThread().sleep(waitTime);

            this.pullCounter++;

            if (pullCounter == 2 * Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                updateFlagPosition();
                finishedPulling.signal();
            }

            resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
        }

        lock.unlock();
    }

    @Override
    public void resultAsserted() throws RemoteException {
        lock.lock();

        pullCounter = 0;

        resultAssert.signalAll();

        lock.unlock();

    }

    @Override
    public Tuple<VectorTimestamp, Integer> startPulling(VectorTimestamp vt) throws RemoteException {
        lock.lock();

        local.update(vt);
        
        startTrial.signalAll();

        informationRepository.updateReferee(RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getId(), local.clone());
        
        if (pullCounter != 2 * Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
            try {
                finishedPulling.await();
            } catch (InterruptedException ex) {
                lock.unlock();
                return null;
            }
        }

        lock.unlock();

        return new Tuple<>(local.clone(), RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getId());
    }

    @Override
    public VectorTimestamp getContestant(int id, int team, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        local.update(vt);
        
        Iterator<Triple<Integer, ContestantState, Integer>> it = teams[team - 1].iterator();

        while (it.hasNext()) {
            Triple<Integer, ContestantState, Integer> temp = it.next();

            if (temp.getFirst() == id) {
                it.remove();
                break;
            }
        }

        informationRepository.resetTeamPlacement(id, team, local.clone());

        lock.unlock();

        return local.clone();
    }

    @Override
    public Tuple<VectorTimestamp, Integer> getFlagPosition(VectorTimestamp vt) throws RemoteException {
        int result;

        lock.lock();

        local.update(vt);
        
        result = flagPosition;

        lock.unlock();

        return new Tuple<>(local.clone(), result);
    }

    @Override
    public Tuple<VectorTimestamp, Integer> getLastFlagPosition(VectorTimestamp vt) {
        int result;

        lock.lock();

        local.update(local);
        
        result = lastFlagPosition;

        lock.unlock();

        return new Tuple<>(local.clone(), result);
    }

    @Override
    public VectorTimestamp setFlagPosition(int flagPosition, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        local.update(vt);
        
        this.lastFlagPosition = flagPosition;
        this.flagPosition = flagPosition;

        lock.unlock();

        return local.clone();
    }

    /**
     * Checks if the team is in place
     *
     * @param teamId team id to check if the team is in place
     * @return true if team in place and ready.
     */
    private boolean isTeamInPlace(int team) {
        return teams[team - 1].size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND;
    }

    @Override
    public boolean checkAllContestantsReady() throws RemoteException {
        boolean result;

        lock.lock();

        result = (teams[0].size() + teams[1].size()) == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND * 2;

        lock.unlock();

        return result;
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
     * Updates the flag position accordingly with the teams joint forces
     */
    private void updateFlagPosition() {
        int team1 = 0;
        int team2 = 0;

        // id, state, strength
        for (Triple<Integer, ContestantState, Integer> contestant : this.teams[0]) {
            team1 += contestant.getThird();
        }

        for (Triple<Integer, ContestantState, Integer> contestant : this.teams[1]) {
            team2 += contestant.getThird();
        }

        lastFlagPosition = flagPosition;

        if (team1 > team2) {
            this.flagPosition--;
        } else if (team1 < team2) {
            this.flagPosition++;
        }
    }
}
