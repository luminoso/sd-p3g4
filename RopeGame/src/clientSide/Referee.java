package clientSide;

import interfaces.InterfaceContestantsBench;
import interfaces.InterfaceGeneralInformationRepository;
import interfaces.InterfacePlayground;
import interfaces.InterfaceRefereeSite;
import others.GameScore;
import others.InterfaceReferee;
import others.RefereeState;
import others.TrialScore;
import others.Tuple;
import others.VectorTimestamp;
import others.Constants;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an active class implements the Referee and his interactions in the
 * passive classes
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class Referee extends Thread implements InterfaceReferee {

    private final InterfaceRefereeSite refereeSite; // referee site interface to be used
    private final InterfacePlayground playground; // playground interface to be used
    private final InterfaceGeneralInformationRepository informationRepository; // general Information Repository interface to be used
    private final InterfaceContestantsBench bench; // list of benches to be used

    // referee definition
    private RefereeState state;
    private VectorTimestamp vt;

    /**
     * Referee initialisation
     *
     * @param name of the referee
     * @param bench interface
     * @param playground interface
     * @param refereeSite interface
     * @param informationRepository interface
     */
    public Referee(String name,
            InterfaceContestantsBench bench,
            InterfacePlayground playground,
            InterfaceRefereeSite refereeSite,
            InterfaceGeneralInformationRepository informationRepository) {

        super(name);

        state = RefereeState.START_OF_THE_MATCH;

        this.bench = bench;

        this.playground = playground;
        this.refereeSite = refereeSite;
        this.informationRepository = informationRepository;
        this.vt = new VectorTimestamp(Constants.VECTOR_TIMESTAMP_SIZE, 0);
    }

    @Override
    public RefereeState getRefereeState() {
        return state;
    }

    @Override
    public void setRefereeState(RefereeState state) {
        this.state = state;
    }

    @Override
    public void run() {
        try {
            vt.increment();
            informationRepository.updateReferee(state.getId(), vt.clone());

            vt.increment();
            informationRepository.printHeader(vt.clone());

            while (state != RefereeState.END_OF_THE_MATCH) {
                switch (state) {
                    case START_OF_THE_MATCH:
                        announceNewGame();
                        break;
                    case START_OF_A_GAME:
                        callTrial();
                        break;
                    case TEAMS_READY:
                        startTrial();
                        break;
                    case WAIT_FOR_TRIAL_CONCLUSION:
                        assertTrialDecision();

                        if (checkForGameEnd()) {
                            declareGameWinner();
                        } else {
                            callTrial();
                        }
                        break;
                    case END_OF_A_GAME:
                        if (checkForMatchEnd()) {
                            declareMatchWinner();
                        } else {
                            announceNewGame();
                        }
                        break;
                    case END_OF_THE_MATCH:
                        break;
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    /**
     * Announces a new game. It also sets trial points, flag, etc to original
     * positions for that a new game takes place.
     */
    private void announceNewGame() throws RemoteException {

        /*
        

        informationRepository.setGameNumber(refereeSite.getGamePoints().size() + 1);
        informationRepository.printGameHeader();
        setRefereeState(RefereeState.START_OF_A_GAME);
        informationRepository.updateReferee();
        informationRepository.printLineUpdate();
         */
        vt.increment();
        vt.update(refereeSite.resetTrialPoints(vt.clone()));

        vt.increment();
        vt.update(playground.setFlagPosition(0, vt.clone()));

        vt.increment();
        informationRepository.setFlagPosition(0, vt.clone());

        vt.increment();
        informationRepository.setTrialNumber(1, vt.clone());

        informationRepository.setGameNumber(
                ((Supplier<Integer>) () -> {
                    Tuple<VectorTimestamp, List<GameScore>> gamePoints = null;
                    try {
                        vt.increment();
                        gamePoints = refereeSite.getGamePoints(vt.clone());
                    } catch (RemoteException ex) {
                        Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    vt.update(gamePoints.getFirst());
                    return gamePoints.getSecond().size() + 1;
                }).get(), ((Supplier<VectorTimestamp>) () -> {
                    vt.increment();
                    return vt.clone();
                }).get());

        vt.increment();
        informationRepository.printGameHeader(vt.clone());

        setRefereeState(RefereeState.START_OF_A_GAME);

        vt.increment();
        informationRepository.updateReferee(state.getId(), vt.clone());
    }

    /**
     * Wakes up both coaches, so they can select their teams. Changes the state
     * to TEAMS_READY and blocks waiting for the coaches to wake him.
     */
    private void callTrial() throws RemoteException {

        informationRepository.setTrialNumber(((Supplier<Integer>) () -> {
            vt.increment();
            Tuple<VectorTimestamp, List<TrialScore>> trialPoints = null;
            try {
                trialPoints = refereeSite.getTrialPoints(vt.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
            }
            vt.update(trialPoints.getFirst());
            return trialPoints.getSecond().size();
        }).get() + 1, ((Supplier<VectorTimestamp>) () -> {
            vt.increment();
            return vt.clone();
        }).get());

        // TODO: this can look better
        bench.pickYourTeam(1);
        bench.pickYourTeam(2);

        vt.increment();
        Tuple<VectorTimestamp, Integer> bothTeamsReady = refereeSite.bothTeamsReady(vt.clone());
        vt.update(bothTeamsReady.getFirst());
        state = InterfaceReferee.getState(bothTeamsReady.getSecond());
    }

    /**
     * Is waked up by the coaches and starts the trial. Changes his state to
     * WAIT_FOR_TRIAL_CONCLUSION and blocks waiting for all the players to have
     * pulled the rope.
     */
    private void startTrial() throws RemoteException {
        vt.increment();
        Tuple<VectorTimestamp, Integer> startPulling = playground.startPulling(vt.clone());
        vt.update(startPulling.getFirst());

        state = InterfaceReferee.getState(startPulling.getSecond());
    }

    /**
     * Decides the trial winner and steps the flag accordingly
     */
    private void assertTrialDecision() throws RemoteException {
        vt.increment();
        Tuple<VectorTimestamp, Integer> lastFlagPositionTuple = playground.getLastFlagPosition(vt.clone());
        vt.update(lastFlagPositionTuple.getFirst());
        int lastFlagPosition = lastFlagPositionTuple.getSecond();

        vt.increment();
        Tuple<VectorTimestamp, Integer> flagPositionT = playground.getFlagPosition(vt.clone());
        vt.update(flagPositionT.getFirst());
        int flagPosition = flagPositionT.getSecond();

        if (flagPosition - lastFlagPosition == 0) {
            vt.increment();
            vt.update(refereeSite.addTrialPoint(TrialScore.DRAW, vt.clone()));
        } else if (flagPosition - lastFlagPosition < 0) {
            vt.increment();
            vt.update(refereeSite.addTrialPoint(TrialScore.VICTORY_TEAM_1, vt.clone()));
        } else {
            vt.increment();
            vt.update(refereeSite.addTrialPoint(TrialScore.VICTORY_TEAM_2, vt.clone()));
        }

        vt.increment();
        informationRepository.setFlagPosition(flagPosition, vt.clone());

        playground.resultAsserted();
    }

    /**
     * Decides the Game winner and sets the gamePoints accordingly
     */
    private void declareGameWinner() throws RemoteException {
        //List<TrialScore> trialPoints = refereeSite.getTrialPoints();
        vt.increment();
        Tuple<VectorTimestamp, List<TrialScore>> trialPointsT = refereeSite.getTrialPoints(vt.clone());
        vt.update(trialPointsT.getFirst());
        List<TrialScore> trialPoints = trialPointsT.getSecond();

        vt.increment();
        Tuple<VectorTimestamp, Integer> flagPositionT = playground.getFlagPosition(vt.clone());
        vt.update(flagPositionT.getFirst());

        int flagPosition = flagPositionT.getSecond();

        switch (flagPosition) {
            // To the left
            case -Constants.KNOCK_OUT_FLAG_POSITION:
                vt.increment();
                vt.update(refereeSite.addGamePoint(GameScore.VICTORY_TEAM_1_BY_KNOCKOUT, vt.clone()));
                break;
            // To the right
            case Constants.KNOCK_OUT_FLAG_POSITION:
                vt.increment();
                vt.update(refereeSite.addGamePoint(GameScore.VICTORY_TEAM_2_BY_KNOCKOUT, vt.clone()));
                break;
            default:
                int team1 = 0;
                int team2 = 0;

                for (TrialScore score : trialPoints) {
                    if (score == TrialScore.VICTORY_TEAM_1) {
                        team1++;
                    } else if (score == TrialScore.VICTORY_TEAM_2) {
                        team2++;
                    }
                }

                if (team1 == team2) {
                    vt.increment();
                    vt.update(refereeSite.addGamePoint(GameScore.DRAW, vt.clone()));
                } else if (team1 > team2) {
                    vt.increment();
                    vt.update(refereeSite.addGamePoint(GameScore.VICTORY_TEAM_1_BY_POINTS, vt.clone()));
                } else {
                    vt.increment();
                    vt.update(refereeSite.addGamePoint(GameScore.VICTORY_TEAM_2_BY_POINTS, vt.clone()));
                }
                break;
        }

        setRefereeState(RefereeState.END_OF_A_GAME);

        vt.increment();
        informationRepository.updateReferee(state.getId(), vt.clone());

        informationRepository.printGameResult(((Supplier<GameScore>) () -> {
            vt.increment();
            Tuple<VectorTimestamp, List<GameScore>> gamePoints = null;
            try {
                gamePoints = refereeSite.getGamePoints(vt.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
            }
            vt.update(gamePoints.getFirst());
            return gamePoints.getSecond().get(gamePoints.getSecond().size() - 1);

        }).get(), ((Supplier<VectorTimestamp>) () -> {
            vt.increment();
            return vt.clone();
        }).get());
    }

    /**
     * Declares the match winner and sets the game score accordingly. Wakes up
     * all other active entities and sends them home.
     */
    private void declareMatchWinner() throws RemoteException {
        int score1 = 0;
        int score2 = 0;

        //for (GameScore score : refereeSite.getGamePoints()
        for (GameScore score
                : ((Supplier<List<GameScore>>) () -> {
                    vt.increment();
                    Tuple<VectorTimestamp, List<GameScore>> gamePoints = null;
                    try {
                        gamePoints = refereeSite.getGamePoints(vt.clone());
                    } catch (RemoteException ex) {
                        Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    vt.update(gamePoints.getFirst());
                    return gamePoints.getSecond();
                }).get()) {
            if (score == GameScore.VICTORY_TEAM_1_BY_KNOCKOUT || score == GameScore.VICTORY_TEAM_1_BY_POINTS) {
                score1++;
            } else if (score == GameScore.VICTORY_TEAM_2_BY_KNOCKOUT || score == GameScore.VICTORY_TEAM_2_BY_POINTS) {
                score2++;
            }
        }

        setRefereeState(RefereeState.END_OF_THE_MATCH);

        vt.increment();
        informationRepository.updateReferee(state.getId(), vt.clone());

        if (score1 > score2) {
            vt.increment();
            informationRepository.printMatchWinner(1, score1, score2, vt.clone());
        } else if (score2 > score1) {
            vt.increment();
            informationRepository.printMatchWinner(2, score1, score2, vt.clone());
        } else {
            vt.increment();
            informationRepository.printMatchDraw(vt.clone());
        }

        vt.increment();
        vt.update(refereeSite.setHasMatchEnded(true, vt.clone()));
    }

    /**
     * Checks if the game has ended
     *
     * @return true if game has ended false if more games to play
     */
    private boolean checkForGameEnd() {
        if (Math.abs(((Supplier<Integer>) () -> {
            vt.increment();
            Tuple<VectorTimestamp, Integer> flagPosition = null;
            try {
                flagPosition = playground.getFlagPosition(vt.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
            }
            vt.update(flagPosition.getFirst());
            return flagPosition.getSecond();

        }).get()) >= Constants.KNOCK_OUT_FLAG_POSITION) {
            return true;
        } else if (((Supplier<Integer>) () -> {
            vt.increment();
            Tuple<VectorTimestamp, Integer> remainingTrials = null;
            try {
                remainingTrials = refereeSite.getRemainingTrials(vt.clone());
            } catch (RemoteException ex) {
                Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
            }
            vt.update(remainingTrials.getFirst());
            return remainingTrials.getSecond();

        }).get() == 0) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the match has ended
     *
     * @return true if match as ended
     */
    private boolean checkForMatchEnd() throws RemoteException {
        vt.increment();
        Tuple<VectorTimestamp, List<GameScore>> gamePointsT = refereeSite.getGamePoints(vt.clone());
        vt.update(gamePointsT.getFirst());
        List<GameScore> gamePoints = gamePointsT.getSecond();

        int team1 = 0;
        int team2 = 0;

        for (GameScore score : gamePoints) {
            if (score == GameScore.VICTORY_TEAM_1_BY_POINTS
                    || score == GameScore.VICTORY_TEAM_1_BY_KNOCKOUT) {
                team1++;
            } else if (score == GameScore.VICTORY_TEAM_2_BY_POINTS
                    || score == GameScore.VICTORY_TEAM_2_BY_KNOCKOUT) {
                team2++;
            }
        }

        return team1 == (Math.floor(Constants.NUMBER_OF_GAMES / 2) + 1)
                || team2 == (Math.floor(Constants.NUMBER_OF_GAMES / 2) + 1)
                || ((Supplier<Integer>) () -> {
                    vt.increment();
                    Tuple<VectorTimestamp, Integer> remainingTrials = null;
                    try {
                        remainingTrials = refereeSite.getRemainingTrials(vt.clone());
                    } catch (RemoteException ex) {
                        Logger.getLogger(Referee.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    vt.update(remainingTrials.getFirst());
                    return remainingTrials.getSecond();

                }).get() == 0;
    }

}
