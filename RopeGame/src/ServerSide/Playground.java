package ServerSide;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee.RefereeState;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfacePlayground;
import Others.InterfaceReferee;
import RopeGame.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General Description: This is an passive class that describes the Playground.
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Playground implements InterfacePlayground {

    /**
     *
     */
    private static Playground instance;

    /**
     *
     */
    private final Lock lock;

    /**
     *
     */
    private final Condition startTrial;                   // condition for waiting the trial start

    /**
     *
     */
    private final Condition teamsInPosition;              // condition for waiting to the teams to be in position

    /**
     *
     */
    private final Condition finishedPulling;              // condition for waiting the contestants finished pulling the rope

    /**
     *
     */
    private final Condition resultAssert;                 // condition for waiting for the result to be asserted

    /**
     *
     */
    private int pullCounter;                              // how many pulls the contestants made

    /**
     *
     */
    private int flagPosition;                             // current flag position

    /**
     *
     */
    private int lastFlagPosition;                         // last flag position

    /**
     *
     */
    private final List<InterfaceContestant>[] teams;               // list containing the Contestant in both teams

    /**
     * The method returns the Playground object. This method is thread-safe and
     * uses the implicit monitor of the class.
     *
     * @return Playground object to be used.
     */
    public static synchronized Playground getInstance() {
        if (instance == null) {
            instance = new Playground();
        }

        return instance;
    }

    /**
     * Private constructor to be used in the singleton.
     */
    private Playground() {
        this.flagPosition = 0;
        this.lastFlagPosition = 0;
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
     */
    @Override
    public void addContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        lock.lock();

        try {
            this.teams[contestant.getContestantTeam() - 1].add(contestant);

            contestant.setContestantState(ContestantState.STAND_IN_POSITION);
            GeneralInformationRepository.getInstance().setTeamPlacement();
            GeneralInformationRepository.getInstance().printLineUpdate();

            if (isTeamInPlace(contestant.getContestantTeam())) {
                this.teamsInPosition.signalAll();
            }

            startTrial.await();
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        }

        lock.unlock();
    }

    /**
     * Synchronisation point for waiting for the teams to be ready
     */
    @Override
    public void checkTeamPlacement() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        lock.lock();

        coach.setCoachState(CoachState.ASSEMBLE_TEAM);
        GeneralInformationRepository.getInstance().printLineUpdate();

        try {
            while (!isTeamInPlace(coach.getCoachTeam())) {
                this.teamsInPosition.await();
            }
        } catch (InterruptedException ex) {
            lock.unlock();
            return;
        }

        lock.unlock();
    }

    /**
     * Synchronisation point for watching the trial in progress
     */
    @Override
    public void watchTrial() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        lock.lock();

        coach.setCoachState(CoachState.WATCH_TRIAL);
        GeneralInformationRepository.getInstance().printLineUpdate();

        try {
            this.resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return;
        }

        lock.unlock();
    }

    /**
     * Contestant pulls the rope
     */
    @Override
    public void pullRope() {
        lock.lock();

        try {
            long waitTime = (long) (Constants.MINIMUM_WAIT_TIME + Math.random() * (Constants.MAXIMUM_WAIT_TIME - Constants.MINIMUM_WAIT_TIME));

            Thread.currentThread().sleep(waitTime);

            this.pullCounter++;

            if (this.pullCounter == 2 * Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
                updateFlagPosition();
                this.finishedPulling.signal();
            }

            this.resultAssert.await();
        } catch (InterruptedException ex) {
            lock.unlock();
            return;
        }

        lock.unlock();
    }

    /**
     * Synchronisation point for signalling the result is asserted
     */
    @Override
    public void resultAsserted() {
        lock.lock();

        this.pullCounter = 0;

        this.resultAssert.signalAll();

        lock.unlock();
    }

    /**
     * Referee instructs the Contestants to start pulling the rope
     */
    @Override
    public void startPulling() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        lock.lock();

        this.startTrial.signalAll();

        referee.setRefereeState(RefereeState.WAIT_FOR_TRIAL_CONCLUSION);
        GeneralInformationRepository.getInstance().printLineUpdate();

        if (pullCounter != 2 * Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND) {
            try {
                finishedPulling.await();
            } catch (InterruptedException ex) {
                lock.unlock();
                return;
            }
        }

        lock.unlock();
    }

    /**
     * The method removes the contestant from the playground.
     */
    @Override
    public void getContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        lock.lock();

        teams[contestant.getContestantTeam() - 1].remove(contestant);

        lock.unlock();
    }

    /**
     * The method returns the flag position in relation to the middle. Middle =
     * 0.
     *
     * @return Position of the flag.
     */
    @Override
    public int getFlagPosition() {
        int result;

        lock.lock();

        result = this.flagPosition;

        lock.unlock();

        return result;
    }

    /**
     * Gets the last flag position
     *
     * @return the flag position before the current position
     */
    @Override
    public int getLastFlagPosition() {
        int result;

        lock.lock();

        result = this.lastFlagPosition;

        lock.unlock();

        return result;
    }

    /**
     * Sets the flag position
     *
     * @param flagPosition position of the flag
     */
    @Override
    public void setFlagPosition(int flagPosition) {
        this.lastFlagPosition = flagPosition;
        this.flagPosition = flagPosition;
    }

    /**
     * Checks if the team is in place
     *
     * @param teamId team id to check if the team is in place
     * @return true if team in place and ready.
     */
    private boolean isTeamInPlace(int teamId) {
        return this.teams[teamId - 1].size() == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND;
    }

    /**
     * Checks if everyone pulled the rope
     */
    @Override
    public void haveAllPulled() {
        lock.lock();
        try {
            this.finishedPulling.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Playground.class.getName()).log(Level.SEVERE, null, ex);
        }
        lock.unlock();
    }

    /**
     * Checks if all contestants are ready to pull the rope
     *
     * @return true if every Contestant is in place to pull the rope
     */
    @Override
    public boolean checkAllContestantsReady() {
        return (teams[0].size() + teams[1].size()) == Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND * 2;
    }

    /**
     * Gets the current teams in the playground
     *
     * @return List containing both teams Contestants in the playground
     */
    @Override
    public List<InterfaceContestant>[] getTeams() {
        List<InterfaceContestant>[] teamslist = new List[2];

        lock.lock();

        teamslist[0] = new ArrayList<>(this.teams[0]);
        teamslist[1] = new ArrayList<>(this.teams[1]);

        lock.unlock();

        return teamslist;
    }

    /**
     * Updates the flag position accordingly with the teams joint forces
     */
    private void updateFlagPosition() {
        int team1 = 0;
        int team2 = 0;

        for (InterfaceContestant contestant : this.teams[0]) {
            team1 += contestant.getContestantStrength();
        }

        for (InterfaceContestant contestant : this.teams[1]) {
            team2 += contestant.getContestantStrength();
        }

        lastFlagPosition = flagPosition;

        if (team1 > team2) {
            this.flagPosition--;
        } else if (team1 < team2) {
            this.flagPosition++;
        }
    }
}
