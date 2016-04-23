package ClientSide;

import static ClientSide.Referee.RefereeState.END_OF_THE_MATCH;
import Others.InterfaceContestantsBench;
import Others.InterfaceGeneralInformationRepository;
import Others.InterfacePlayground;
import Others.InterfaceReferee;
import Others.InterfaceRefereeSite;
import RopeGame.Constants;
import ServerSide.RefereeSite.GameScore;
import ServerSide.RefereeSite.TrialScore;
import java.util.ArrayList;
import java.util.List;

/**
 * General Description: This is an active class implements the Referee and his
 * interactions in the passive classes
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Referee extends Thread implements InterfaceReferee {

    /**
     * 
     */
    private RefereeState state;     // Referee state

    /**
     * 
     */
    private final List<InterfaceContestantsBench> benchs;
    
    /**
     * 
     */
    private final InterfaceRefereeSite refereeSite;
    
    /**
     * 
     */
    private final InterfacePlayground playground;
    
    /**
     * 
     */
    private final InterfaceGeneralInformationRepository generalInformationRepository;

    /**
     * 
     * @param name 
     */
    public Referee(String name) {
        this(name, true);
    }

    /**
     * 
     * @param name
     * @param runlocal 
     */
    public Referee(String name, boolean runlocal) {
        super(name);

        state = RefereeState.START_OF_THE_MATCH;

        this.benchs = new ArrayList<>();
        
        for(int i = 1; i <= 2; i++)
            this.benchs.add(new ContestantsBenchStub(i));
        
        this.playground = new PlaygroundStub();
        this.refereeSite = new RefereeSiteStub();
        this.generalInformationRepository = new GeneralInformationRepositoryStub();
    }

    /**
     * Get the current Referee state
     *
     * @return Referee state
     */
    @Override
    public RefereeState getRefereeState() {
        return state;
    }

    /**
     * Sets the current Referee state
     *
     * @param state RefereeState
     */
    @Override
    public void setRefereeState(RefereeState state) {
        this.state = state;
    }

    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        while (state != END_OF_THE_MATCH) {
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
    }

    /**
     * Reset trial points and flag position when starting a new game. Updates
     * state to START_OF_A_GAME.
     */
    private void announceNewGame() {
        refereeSite.resetTrialPoints();
        playground.setFlagPosition(0);

        generalInformationRepository.setFlagPosition(0);
        generalInformationRepository.setTrialNumber(1);
        generalInformationRepository.setGameNumber(refereeSite.getGamePoints().size() + 1);
        generalInformationRepository.printGameHeader();
        this.setRefereeState(RefereeState.START_OF_A_GAME);
        generalInformationRepository.printLineUpdate();
    }

    /**
     * Wakes up both coaches, so they can select their teams. Changes the state
     * to TEAMS_READY and blocks waiting for the coaches to wake him.
     */
    private void callTrial() {
        generalInformationRepository.setTrialNumber(refereeSite.getTrialPoints().size() + 1);

        for (InterfaceContestantsBench bench : benchs) {
            bench.pickYourTeam();
        }

        refereeSite.bothTeamsReady();
    }

    /**
     * Is waked up by the coaches and starts the trial. Changes his state to
     * WAIT_FOR_TRIAL_CONCLUSION and blocks waiting for all the players to have
     * pulled the rope.
     */
    private void startTrial() {
        playground.startPulling();
    }

    /**
     * Decides the trial winner and steps the flag accordingly
     *
     * @return true if all trials over, false if more trials to play
     */
    private void assertTrialDecision() {
        int lastFlagPosition = playground.getLastFlagPosition();
        int flagPosition = playground.getFlagPosition();

        if (flagPosition - lastFlagPosition == 0) {
            refereeSite.addTrialPoint(TrialScore.DRAW);
        } else if (flagPosition - lastFlagPosition < 0) {
            refereeSite.addTrialPoint(TrialScore.VICTORY_TEAM_1);
        } else {
            refereeSite.addTrialPoint(TrialScore.VICTORY_TEAM_2);
        }

        generalInformationRepository.setFlagPosition(flagPosition);

        generalInformationRepository.printLineUpdate();
        generalInformationRepository.resetTeamPlacement();

        playground.resultAsserted();
    }

    /**
     * Decides the Game winner and sets the gamePoints accordingly
     *
     * @return true if more games to play, false if all games ended
     */
    private void declareGameWinner() {
        List<TrialScore> trialPoints = refereeSite.getTrialPoints();
        int flagPosition = playground.getFlagPosition();

        switch (flagPosition) {
            // To the left
            case -Constants.KNOCK_OUT_FLAG_POSITION:
                refereeSite.addGamePoint(GameScore.VICTORY_TEAM_1_BY_KNOCKOUT);
                break;
            // To the right
            case Constants.KNOCK_OUT_FLAG_POSITION:
                refereeSite.addGamePoint(GameScore.VICTORY_TEAM_2_BY_KNOCKOUT);
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
                    refereeSite.addGamePoint(GameScore.DRAW);
                } else if (team1 > team2) {
                    refereeSite.addGamePoint(GameScore.VICTORY_TEAM_1_BY_POINTS);
                } else {
                    refereeSite.addGamePoint(GameScore.VICTORY_TEAM_2_BY_POINTS);
                }
                break;
        }

        this.setRefereeState(RefereeState.END_OF_A_GAME);
        generalInformationRepository.printLineUpdate();
        generalInformationRepository.printGameResult(refereeSite.getGamePoints().get(refereeSite.getGamePoints().size() - 1));
    }

    /**
     * Declares the match winner and sets the game score accordingly. Wakes up
     * all other active entities and sends them home.
     */
    private void declareMatchWinner() {

        int score1 = 0;
        int score2 = 0;

        for (GameScore score : refereeSite.getGamePoints()) {
            if (score == GameScore.VICTORY_TEAM_1_BY_KNOCKOUT || score == GameScore.VICTORY_TEAM_1_BY_POINTS) {
                score1++;
            } else if (score == GameScore.VICTORY_TEAM_2_BY_KNOCKOUT || score == GameScore.VICTORY_TEAM_2_BY_POINTS) {
                score2++;
            }
        }

        this.setRefereeState(RefereeState.END_OF_THE_MATCH);
        generalInformationRepository.printLineUpdate();

        if (score1 > score2) {
            generalInformationRepository.printMatchWinner(1, score1, score2);
        } else if (score2 > score1) {
            generalInformationRepository.printMatchWinner(2, score1, score2);
        } else {
            generalInformationRepository.printMatchDraw();
        }

        refereeSite.setHasMatchEnded(true);
    }

    /**
     * Checks if the game has ended
     *
     * @return true if game has ended false if more games to play
     */
    private boolean checkForGameEnd() {
        if (Math.abs(playground.getFlagPosition()) >= Constants.KNOCK_OUT_FLAG_POSITION) {
            return true;
        } else if (refereeSite.getRemainingTrials() == 0) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the match has ended
     *
     * @return true if match as ended.
     */
    private boolean checkForMatchEnd() {
        List<GameScore> gamePoints = refereeSite.getGamePoints();

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
                || refereeSite.getRemainingGames() == 0;
    }

    /**
     * Enums of possible Referee states
     */
    public enum RefereeState {
        START_OF_THE_MATCH(1, "SOM"),
        START_OF_A_GAME(2, "SOG"),
        TEAMS_READY(3, "TRD"),
        WAIT_FOR_TRIAL_CONCLUSION(4, "WTC"),
        END_OF_A_GAME(5, "EOG"),
        END_OF_THE_MATCH(6, "EOM");

        private final int id;
        private final String state;

        /**
         * Create a RefereeState enum
         *
         * @param id of the enum Referee state
         * @param state Initials of the Referee state
         */
        RefereeState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * Gets the ID of the RefereeState enum
         *
         * @return id of the Referee state
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the enum Referee state
         *
         * @return Referee state enum string
         */
        public String getState() {
            return state;
        }

        /**
         * Converts current Referee state to String
         *
         * @return String describing Referee sate
         */
        @Override
        public String toString() {
            return state;
        }
    }
}
