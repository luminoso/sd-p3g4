package ServerSide;

import ClientSide.GeneralInformationRepositoryStub;
import ClientSide.Referee.RefereeState;
import Others.InterfaceReferee;
import Others.InterfaceRefereeSite;
import RopeGame.Constants;
import static java.lang.System.out;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General Description: This is an passive class that describes the Referee
 * site.
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RefereeSite implements InterfaceRefereeSite {

    /**
     *
     */
    private static RefereeSite instance;

    /**
     *
     */
    private final Lock lock;

    /**
     *
     */
    private final Condition informReferee;              // condition for referee wait for the coaches

    /**
     *
     */
    private int informRefereeCounter;                   // counter of how many coaches informed the referee

    /**
     *
     */
    private boolean hasMatchEnded;

    /**
     *
     */
    private List<TrialScore> trialStatus;               // current trial status

    /**
     *
     */
    private final List<GameScore> gameStatus;           // current game status

    private final GeneralInformationRepositoryStub informationRepository;
    
    /**
     * The method returns the RefereeSite object. The method is thread-safe and
     * uses the implicit monitor of the class.
     *
     * @return RefereeSite object to be used.
     */
    public static synchronized RefereeSite getInstance() {
        if (instance == null) {
            instance = new RefereeSite();
        }

        return instance;
    }

    /**
     * Private constructor to be used in singleton.
     */
    private RefereeSite() {
        lock = new ReentrantLock();

        trialStatus = new LinkedList<>();
        gameStatus = new LinkedList<>();

        informReferee = lock.newCondition();
        informRefereeCounter = 0;
        hasMatchEnded = false;
        informationRepository = new GeneralInformationRepositoryStub();
    }

    /**
     * The method returns the game points in the form of an array.
     *
     * @return Game points.
     */
    @Override
    public List<GameScore> getGamePoints() {
        List<GameScore> gamePoints;

        lock.lock();

        gamePoints = new LinkedList<>(this.gameStatus);

        lock.unlock();

        return gamePoints;
    }

    /**
     * The method returns the trial points in the form of an array.
     *
     * @return Trial points.
     */
    @Override
    public List<TrialScore> getTrialPoints() {
        List<TrialScore> trialPoints;

        lock.lock();

        trialPoints = new LinkedList<>(this.trialStatus);

        lock.unlock();

        return trialPoints;
    }

    /**
     * Resets the trial points
     */
    @Override
    public void resetTrialPoints() {
        lock.lock();

        this.trialStatus = new LinkedList<>();

        lock.unlock();
    }

    /**
     * Gets how many trials are remaining to play
     *
     * @return number of remaining trials left
     */
    @Override
    public int getRemainingTrials() {
        int remaining;

        lock.lock();

        remaining = Constants.NUMBER_OF_TRIALS - this.trialStatus.size();

        lock.unlock();

        return remaining;
    }

    /**
     * Gets how many games are remaining to play
     *
     * @return number of remaining games left
     */
    @Override
    public int getRemainingGames() {
        int remaining;

        lock.lock();

        remaining = Constants.NUMBER_OF_GAMES - this.gameStatus.size();

        lock.unlock();

        return remaining;
    }

    /**
     * The method allows to set the game points for both team.
     *
     * @param score Game points of both teams.
     */
    @Override
    public void addGamePoint(GameScore score) {
        lock.lock();

        this.gameStatus.add(score);
        this.trialStatus.clear();

        lock.unlock();
    }

    /**
     * The method allows to set the trial points for both team.
     *
     * @param score Trial points of both teams.
     */
    @Override
    public void addTrialPoint(TrialScore score) {
        lock.lock();

        this.trialStatus.add(score);

        lock.unlock();
    }

    /**
     * Synchronization point where the Referee waits for both teams to be ready
     */
    @Override
    public void bothTeamsReady() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        lock.lock();
        try {
            referee.setRefereeState(RefereeState.TEAMS_READY);
            informationRepository.updateReferee();
            informationRepository.printLineUpdate();

            if (informRefereeCounter != 2) {
                informReferee.await();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Logger.getLogger(RefereeSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        informRefereeCounter = 0;

        lock.unlock();
    }

    /**
     * Synchronisation point where the Coaches inform the Referee that they're
     * ready
     */
    @Override
    public void informReferee() {
        lock.lock();

        informRefereeCounter++;

        if (informRefereeCounter == 2) {
            informReferee.signal();
        }

        lock.unlock();
    }

    /**
     * Checks if the match has ended
     *
     * @return True if no more matches to play. False if otherwise.
     */
    @Override
    public boolean hasMatchEnded() {
        boolean hasEnded;
        lock.lock();
        hasEnded = hasMatchEnded;
        lock.unlock();
        return hasEnded;
    }

    /**
     * Changes the information at RefereeSite if the match as ended
     *
     * @param hasMatchEnded true if match ended
     */
    @Override
    public void setHasMatchEnded(boolean hasMatchEnded) {
        lock.lock();
        out.println("setting has match ended");
        this.hasMatchEnded = hasMatchEnded;
        lock.unlock();
    }

    /**
     *
     */
    public enum TrialScore {
        /**
         *
         */
        DRAW(0, "D"),
        /**
         *
         */
        VICTORY_TEAM_1(1, "VT1"),
        /**
         *
         */
        VICTORY_TEAM_2(2, "VT2");

        /**
         *
         */
        private final int id;

        /**
         *
         */
        private final String status;

        /**
         *
         * @param id
         * @param status
         */
        private TrialScore(int id, String status) {
            this.id = id;
            this.status = status;
        }

        /**
         *
         * @return
         */
        public int getId() {
            return this.id;
        }

        /**
         *
         * @return
         */
        public String getStatus() {
            return this.status;
        }
    }

    /**
     *
     */
    public enum GameScore {
        /**
         *
         */
        DRAW(0, "D"),
        /**
         *
         */
        VICTORY_TEAM_1_BY_POINTS(1, "VT1PT"),
        /**
         *
         */
        VICTORY_TEAM_1_BY_KNOCKOUT(2, "VT1KO"),
        /**
         *
         */
        VICTORY_TEAM_2_BY_POINTS(3, "VT2PT"),
        /**
         *
         */
        VICTORY_TEAM_2_BY_KNOCKOUT(4, "VT2KO");

        /**
         *
         */
        private final int id;

        /**
         *
         */
        private final String status;

        /**
         *
         * @param id
         * @param status
         */
        private GameScore(int id, String status) {
            this.id = id;
            this.status = status;
        }

        /**
         *
         * @return
         */
        public int getId() {
            return this.id;
        }

        /**
         *
         * @return
         */
        public String getStatus() {
            return this.status;
        }
    }
}
