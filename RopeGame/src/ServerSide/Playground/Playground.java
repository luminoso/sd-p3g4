package ServerSide.Playground;

import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfacePlayground;
import Others.InterfaceCoach.CoachState;
import Others.InterfaceContestant;
import Others.InterfaceContestant.ContestantState;
import Others.InterfaceReferee.RefereeState;
import Others.Triple;
import Others.Tuple;
import Others.VectorTimestamp;
import RopeGame.Constants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private VectorTimestamp vt;

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

        //TODO: initialise vt
        vt = null;
    }

    @Override
    public Tuple<VectorTimestamp, Integer> addContestant(int id, int team, int state, int strength, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        try {
            teams[team - 1].add(new Triple<>(id, ContestantState.STAND_IN_POSITION, strength));

            //informationRepository.updateContestant();
            //informationRepository.setTeamPlacement();
            //informationRepository.printLineUpdate();
            if (isTeamInPlace(team)) {
                teamsInPosition.signalAll();
            }

            startTrial.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        vt.update(vt);
        vt.increment();

        lock.unlock();

        // Contestant changes state
        return new Tuple<VectorTimestamp, Integer>(vt.clone(), ContestantState.STAND_IN_POSITION.getId());
    }

    @Override
    public Tuple<VectorTimestamp, Integer> checkTeamPlacement(int team, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        //coach.setCoachState(CoachState.ASSEMBLE_TEAM);
        //informationRepository.updateCoach();
        //informationRepository.printLineUpdate();
        try {
            while (!isTeamInPlace(team)) {
                teamsInPosition.await();
            }
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        vt.update(vt);
        vt.increment();

        lock.unlock();

        return new Tuple<>(vt.clone(), CoachState.ASSEMBLE_TEAM.getId());
    }

    @Override
    public Tuple<VectorTimestamp, Integer> watchTrial(VectorTimestamp vt) throws RemoteException {
        lock.lock();

        //coach.setCoachState(CoachState.WATCH_TRIAL);
        //informationRepository.updateCoach();
        //informationRepository.printLineUpdate();
        try {
            resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        vt.update(vt);
        vt.increment();

        lock.unlock();

        return new Tuple<>(vt.clone(), CoachState.WATCH_TRIAL.getId());
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

        startTrial.signalAll();

        //referee.setRefereeState(RefereeState.WAIT_FOR_TRIAL_CONCLUSION);
        //informationRepository.updateReferee();
        //informationRepository.printLineUpdate();
        if (pullCounter != 2 * Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
            try {
                finishedPulling.await();
            } catch (InterruptedException ex) {
                lock.unlock();
                return null;
            }
        }

        vt.update(vt);
        vt.increment();

        lock.unlock();

        return new Tuple<>(vt.clone(), RefereeState.WAIT_FOR_TRIAL_CONCLUSION.getId());
    }

    @Override
    public VectorTimestamp getContestant(int id, int team, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        Iterator<Triple<Integer, ContestantState, Integer>> it = teams[team - 1].iterator();

        while (it.hasNext()) {
            Triple<Integer, ContestantState, Integer> temp = it.next();

            if (temp.getFirst() == id) {
                it.remove();
                break;
            }
        }

        //informationRepository.resetTeamPlacement();
        //informationRepository.printLineUpdate();
        vt.update(vt);
        vt.increment();

        lock.unlock();

        return vt.clone();
    }

    @Override
    public Tuple<VectorTimestamp, Integer> getFlagPosition(VectorTimestamp vt) throws RemoteException {
        int result;

        lock.lock();

        result = flagPosition;

        vt.increment();
        vt.update(vt);

        lock.unlock();

        return new Tuple<>(vt.clone(), result);
    }

    @Override
    public Tuple<VectorTimestamp, Integer> getLastFlagPosition(VectorTimestamp vt) {
        int result;

        lock.lock();

        result = lastFlagPosition;

        vt.increment();
        vt.update(vt);
        lock.unlock();

        return new Tuple<>(vt.clone(), result);
    }

    @Override
    public VectorTimestamp setFlagPosition(int flagPosition, VectorTimestamp vt) throws RemoteException {
        lock.lock();

        this.lastFlagPosition = flagPosition;
        this.flagPosition = flagPosition;

        vt.update(vt);
        vt.increment();

        lock.unlock();

        return vt.clone();
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
    public VectorTimestamp haveAllPulled(VectorTimestamp vt) throws RemoteException {

        //TODO: dead function? no one uses this.
        lock.lock();

        try {
            finishedPulling.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return null;
        }

        vt.update(vt);
        vt.increment();

        lock.unlock();

        return vt.clone();
    }

    @Override
    public boolean checkAllContestantsReady() {
        boolean result;

        lock.lock();

        result = (teams[0].size() + teams[1].size()) == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND * 2;

        lock.unlock();

        return result;
    }

    @Override
    public Tuple<VectorTimestamp, List<InterfaceContestant>[]> getTeams(VectorTimestamp vt) {
        List<InterfaceContestant>[] teamslist = new List[2];

        //TODO: dead function? no one uses this.
        lock.lock();

        //teamslist[0] = new ArrayList<>(this.teams[0]);
        //teamslist[1] = new ArrayList<>(this.teams[1]);
        vt.update(vt);
        vt.increment();

        lock.unlock();

        return new Tuple<>(vt.clone(), teamslist);
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
