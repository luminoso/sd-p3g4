package ServerSide;

import ClientSide.GeneralInformationRepositoryStub;
import Others.InterfaceReferee;
import Others.InterfaceReferee.RefereeState;
import Others.InterfaceRefereeSite;
import Others.InterfaceRefereeSite.GameScore;
import Others.InterfaceRefereeSite.TrialScore;
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
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
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

    @Override
    public List<GameScore> getGamePoints() {
        List<GameScore> gamePoints;

        lock.lock();

        gamePoints = new LinkedList<>(this.gameStatus);

        lock.unlock();

        return gamePoints;
    }

    @Override
    public List<TrialScore> getTrialPoints() {
        List<TrialScore> trialPoints;

        lock.lock();

        trialPoints = new LinkedList<>(this.trialStatus);

        lock.unlock();

        return trialPoints;
    }

    @Override
    public void resetTrialPoints() {
        lock.lock();

        this.trialStatus = new LinkedList<>();

        lock.unlock();
    }

    @Override
    public int getRemainingTrials() {
        int remaining;

        lock.lock();

        remaining = Constants.NUMBER_OF_TRIALS - this.trialStatus.size();

        lock.unlock();

        return remaining;
    }

    @Override
    public int getRemainingGames() {
        int remaining;

        lock.lock();

        remaining = Constants.NUMBER_OF_GAMES - this.gameStatus.size();

        lock.unlock();

        return remaining;
    }

    @Override
    public void addGamePoint(GameScore score) {
        lock.lock();

        this.gameStatus.add(score);
        this.trialStatus.clear();

        lock.unlock();
    }

    @Override
    public void addTrialPoint(TrialScore score) {
        lock.lock();

        this.trialStatus.add(score);

        lock.unlock();
    }

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

    @Override
    public void informReferee() {
        lock.lock();

        informRefereeCounter++;

        if (informRefereeCounter == 2) {
            informReferee.signal();
        }

        lock.unlock();
    }

    @Override
    public boolean hasMatchEnded() {
        boolean hasEnded;
        lock.lock();
        hasEnded = hasMatchEnded;
        lock.unlock();
        return hasEnded;
    }

    @Override
    public void setHasMatchEnded(boolean hasMatchEnded) {
        lock.lock();
        out.println("setting has match ended");
        this.hasMatchEnded = hasMatchEnded;
        lock.unlock();
    }

}
