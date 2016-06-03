package ServerSide.RefereeSite;

import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfaceRefereeSite;
import Others.GameScore;
import Others.RefereeState;
import Others.TrialScore;
import Others.Tuple;
import Others.VectorTimestamp;
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
 * This is an passive class that describes the Referee Site
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class RefereeSite implements InterfaceRefereeSite {

    private static RefereeSite instance;        // singleton

    // locking and waiting condtions
    private final Lock lock;
    private final Condition informReferee;      // condition for referee wait for the coaches

    private int informRefereeCounter;           // counter of how many coaches informed the referee  
    private boolean hasMatchEnded;

    private List<TrialScore> trialStatus;       // current trial status
    private int shutdownVotes;                  // count if all votes are met to shutdown
    private final List<GameScore> gameStatus;   // current game status

    private final InterfaceGeneralInformationRepository informationRepository;

    private VectorTimestamp vt;

    /**
     * The method returns the RefereeSite object. The method is thread-safe and
     * uses the implicit monitor of the class.
     *
     * @param informationRepository interface to use
     * @return referee site object to be used
     */
    public static synchronized InterfaceRefereeSite getInstance(InterfaceGeneralInformationRepository informationRepository) {
        if (instance == null) {
            instance = new RefereeSite(informationRepository);
        }

        return instance;
    }

    /**
     * Private constructor to be used in singleton
     */
    private RefereeSite(InterfaceGeneralInformationRepository informationRepository) {
        lock = new ReentrantLock();

        trialStatus = new LinkedList<>();
        gameStatus = new LinkedList<>();

        informReferee = lock.newCondition();
        informRefereeCounter = 0;
        hasMatchEnded = false;
        this.informationRepository = informationRepository;
        shutdownVotes = 0;

        vt = new VectorTimestamp(Constants.VECTOR_TIMESTAMP_SIZE, 0);
    }

    @Override
    public Tuple<VectorTimestamp, List<GameScore>> getGamePoints(VectorTimestamp vt) {
        List<GameScore> gamePoints;

        lock.lock();

        gamePoints = new LinkedList<>(this.gameStatus);

        vt.update(vt);

        lock.unlock();

        return new Tuple<>(vt.clone(), gamePoints);
    }

    @Override
    public Tuple<VectorTimestamp, List<TrialScore>> getTrialPoints(VectorTimestamp vt) {
        List<TrialScore> trialPoints;

        lock.lock();

        trialPoints = new LinkedList<>(this.trialStatus);

        vt.update(vt);

        lock.unlock();

        return new Tuple<>(vt.clone(), trialPoints);
    }

    @Override
    public VectorTimestamp resetTrialPoints(VectorTimestamp vt) {
        lock.lock();

        this.trialStatus = new LinkedList<>();

        vt.update(vt);

        lock.unlock();

        return vt.clone();
    }

    @Override
    public Tuple<VectorTimestamp, Integer> getRemainingTrials(VectorTimestamp vt) {
        int remaining;

        lock.lock();

        remaining = Constants.NUMBER_OF_TRIALS - this.trialStatus.size();

        vt.update(vt);

        lock.unlock();

        return new Tuple<>(vt.clone(), remaining);
    }

    @Override
    public Tuple<VectorTimestamp, Integer> getRemainingGames(VectorTimestamp vt) {
        int remaining;

        lock.lock();

        remaining = Constants.NUMBER_OF_GAMES - this.gameStatus.size();

        vt.update(vt);

        lock.unlock();

        return new Tuple<>(vt.clone(), remaining);
    }

    @Override
    public VectorTimestamp addGamePoint(GameScore score, VectorTimestamp vt) {
        lock.lock();

        this.gameStatus.add(score);
        this.trialStatus.clear();

        vt.update(vt);

        lock.unlock();

        return vt.clone();
    }

    @Override
    public VectorTimestamp addTrialPoint(TrialScore score, VectorTimestamp vt) {
        lock.lock();

        this.trialStatus.add(score);

        vt.update(vt);

        lock.unlock();

        return vt.clone();
    }

    @Override
    public Tuple<VectorTimestamp, Integer> bothTeamsReady(VectorTimestamp vt) {
        //InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        lock.lock();
        try {
            //referee.setRefereeState(RefereeState.TEAMS_READY);
            //informationRepository.updateReferee();
            //informationRepository.printLineUpdate();

            if (informRefereeCounter != 2) {
                informReferee.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RefereeSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        informRefereeCounter = 0;

        lock.unlock();

        return new Tuple<>(vt.clone(), RefereeState.TEAMS_READY.getId());
    }

    @Override
    public VectorTimestamp informReferee(VectorTimestamp vt) {
        lock.lock();

        informRefereeCounter++;

        if (informRefereeCounter == 2) {
            informReferee.signal();
        }

        vt.update(vt);

        lock.unlock();

        return vt.clone();
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
    public VectorTimestamp setHasMatchEnded(boolean hasMatchEnded, VectorTimestamp vt) {
        lock.lock();

        out.println("Setting has match ended");
        this.hasMatchEnded = hasMatchEnded;

        vt.update(vt);

        lock.unlock();

        return vt.clone();
    }

    @Override
    public Tuple<VectorTimestamp, Boolean> shutdown() {
        boolean result;

        lock.lock();

        shutdownVotes++;

        result = shutdownVotes == (1 + 2 * (1 + Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH));

        lock.unlock();

        return new Tuple<>(vt.clone(), result);
    }

}
