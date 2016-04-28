package Communication;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee.RefereeState;
import ServerSide.RefereeSite;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Message implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 20160412;

    // variables required to define the Contestant
    /**
     *
     */
    private int team;

    /**
     *
     */
    private int id;

    /**
     *
     */
    private int strength;

    // additional variables required to define the Referee
    /**
     *
     */
    private Enum state;

    // message variables
    /**
     *
     */
    private MessageType type;

    /**
     *
     */
    private Set set;

    /**
     *
     */
    private int number;

    /**
     *
     */
    private int numbers[];

    /**
     *
     */
    private RefereeSite.GameScore gameScore;

    /**
     *
     */
    private RefereeSite.TrialScore trialscore;

    /**
     *
     */
    private List list;

    /**
     *
     */
    private boolean bool;

    // initialization for Contestant
    /**
     *
     * @param type
     * @param state
     * @param team
     * @param id
     * @param strength
     */
    public Message(MessageType type, ContestantState state, int team, int id, int strength) {
        this.type = type;
        this.state = state;
        this.team = team;
        this.id = id;
        this.strength = strength;
    }

    // initialization for Coach
    /**
     *
     * @param type
     * @param state
     * @param team
     * @param strategy
     */
    public Message(MessageType type, CoachState state, int team) {
        this.type = type;
        this.state = state;
        this.team = team;
    }

    // initialization for Referee
    /**
     *
     * @param type
     * @param state
     * @param name
     */
    public Message(MessageType type, RefereeState state) {
        this.type = type;
        this.state = state;
    }

    /**
     *
     * @param messageType
     */
    public Message(MessageType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public MessageType getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public Set getBench() {
        return set;
    }

    /**
     *
     * @return
     */
    public Set getSelectedContestants() {
        return set;
    }

    /**
     *
     * @param set
     */
    public void setSelectedContestants(Set set) {
        this.set = set;
    }

    /**
     *
     * @return
     */
    public CoachState getCoachState() {
        return (CoachState) state;
    }

    /**
     *
     * @return
     */
    public int getFlagPostion() {
        return number;
    }

    /**
     *
     * @return
     */
    public int getLastFlagPostion() {
        return number;
    }

    /**
     *
     * @param position
     */
    public void setFlagPosition(int position) {
        number = position;
    }

    /**
     *
     * @return
     */
    public RefereeState getRefereeState() {
        return (RefereeState) state;
    }

    /**
     *
     * @return
     */
    public int[] getNumbers() {
        return numbers;
    }

    /**
     *
     * @param numbers
     */
    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    /**
     *
     * @param score
     */
    public void setGamePoint(RefereeSite.GameScore score) {
        gameScore = score;
    }

    public RefereeSite.GameScore getGamePoint() {
        return gameScore;
    }

    /**
     *
     * @param gameNumber
     */
    public void setGameNumber(int gameNumber) {
        number = gameNumber;
    }

    /**
     *
     * @param trialNumber
     */
    public void setTrialNumber(int trialNumber) {
        number = trialNumber;
    }

    /**
     *
     * @param score
     */
    public void setTrialScore(RefereeSite.TrialScore score) {
        trialscore = score;
    }

    /**
     *
     * @return
     */
    public List getGamePoints() {
        return list;
    }

    /**
     *
     * @return
     */
    public int getRemainingGames() {
        return number;
    }

    /**
     *
     * @return
     */
    public int getRemainingtTrials() {
        return number;
    }

    /**
     *
     * @return
     */
    public List<RefereeSite.TrialScore> getTrialPoints() {
        return list;
    }

    /**
     *
     * @return
     */
    public boolean getHasMatchEnded() {
        return bool;
    }

    /**
     *
     * @return
     */
    public ContestantState getContestantState() {
        return (ContestantState) state;
    }

    /**
     *
     * @return
     */
    public MessageCategory getMessageCategory() {
        return type.getCategory();
    }

    public int getTeam() {
        return team;
    }

    public Enum getState() {
        return state;
    }

    public void setContestantState(Enum state) {
        this.state = state;
    }

    public void setStateCoachState(Enum state) {
        this.state = state;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public int getContestantId() {
        return id;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setCoachState(CoachState coachState) {
        state = coachState;
    }

    public void setLastFlagPosition(int lastFlagPosition) {
        number = lastFlagPosition;
    }

    public void setRefereeState(RefereeState refereeState) {
        state = refereeState;
    }

    public RefereeSite.TrialScore getTrialPoint() {
        return trialscore;
    }

    public void setGamePoints(List<RefereeSite.GameScore> gamePoints) {
        list = gamePoints;
    }

    public void setRemainingGames(int remainingGames) {
        number = remainingGames;
    }

    public void setRemainingTrials(int remainingTrials) {
        number = remainingTrials;
    }

    public void setTrialPoints(List<RefereeSite.TrialScore> trialPoints) {
        list = trialPoints;
    }

    public void setHasMatchEnded(boolean hasMatchEnded) {
        bool = hasMatchEnded;
    }

    public void setGameResult(RefereeSite.GameScore score) {
        gameScore = score;
    }

    public int getStrength() {
        return strength;
    }

    public RefereeSite.GameScore getGameResult() {
        return gameScore;
    }

    public int getGameNumber() {
        return number;
    }

    public int getTrialNumber() {
        return number;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     *
     */
    public enum MessageType {
        /**
         *
         */
        OK,
        /**
         *
         */
        BENCH,
        /**
         *
         */
        SELECTED_CONTESTANTS,
        /**
         *
         */
        FLAG_POSITION,
        /**
         *
         */
        LAST_FLAG_POSITION,
        /**
         *
         */
        GAME_POINTS,
        /**
         *
         */
        TRIAL_POINTS,
        /**
         *
         */
        REMAINING_GAMES,
        /**
         *
         */
        REMAINING_TRIALS,
        /**
         *
         */
        BOOLEAN,
        /**
         *
         */
        COACH_STATE_CHANGE,
        /**
         *
         */
        REFEREE_STATE_CHANGE,
        /**
         *
         */
        CONTESTANT_STATE_CHANGE,
        /**
         *
         */
        CB_ADD_CONTESTANT(MessageCategory.CB),
        /**
         *
         */
        CB_GET_BENCH(MessageCategory.CB),
        /**
         *
         */
        CB_GET_CONTESTANT(MessageCategory.CB),
        /**
         *
         */
        CB_GET_SELECTED_CONTESTANTS(MessageCategory.CB),
        /**
         *
         */
        CB_PICK_YOUR_TEAM(MessageCategory.CB),
        /**
         *
         */
        CB_SET_SELECTED_CONTESTANTS(MessageCategory.CB),
        /**
         *
         */
        CB_WAIT_FOR_NEXT_TRIAL(MessageCategory.CB),
        /**
         *
         */
        CB_UPDATE_CONTESTANT_STRENGTH(MessageCategory.CB),
        /**
         *
         */
        PG_ADD_CONTESTANT(MessageCategory.PG),
        /**
         *
         */
        PG_CHECK_TEAM_PLACEMENT(MessageCategory.PG),
        /**
         *
         */
        PG_GET_CONTESTANT(MessageCategory.PG),
        /**
         *
         */
        PG_GET_FLAG_POSITION(MessageCategory.PG),
        /**
         *
         */
        PG_GET_LAST_FLAG_POSITION(MessageCategory.PG),
        /**
         *
         */
        PG_PULL_ROPE(MessageCategory.PG),
        /**
         *
         */
        PG_RESULT_ASSERTED(MessageCategory.PG),
        /**
         *
         */
        PG_SET_FLAG_POSITION(MessageCategory.PG),
        /**
         *
         */
        PG_START_PULLING(MessageCategory.PG),
        /**
         *
         */
        PG_WATCH_TRIAL(MessageCategory.PG),
        /**
         *
         */
        GIR_UPDATE_COACH(MessageCategory.GIR),
        /**
         *
         */
        GIR_UPDATE_CONTESTANT(MessageCategory.GIR),
        /**
         *
         */
        GIR_UPDATE_CONTESTANT_STRENGTH(MessageCategory.GIR),
        /**
         *
         */
        GIR_UPDATE_REFEREE(MessageCategory.GIR),
        /**
         *
         */
        GIR_CLOSE(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_GAME_HEADER(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_GAME_RESULT(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_HEADER(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_LEGEND(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_LINE_UPDATE(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_MATCH_DRAW(MessageCategory.GIR),
        /**
         *
         */
        GIR_PRINT_MATCH_WINNER(MessageCategory.GIR),
        /**
         *
         */
        GIR_RESET_TEAM_PLACEMENT(MessageCategory.GIR),
        /**
         *
         */
        GIR_SET_FLAG_POSITION(MessageCategory.GIR),
        /**
         *
         */
        GIR_SET_GAME_NUMBER(MessageCategory.GIR),
        /**
         *
         */
        GIR_SET_TEAM_PLACEMENT(MessageCategory.GIR),
        /**
         *
         */
        GIR_SET_TRIAL_NUMBER(MessageCategory.GIR),
        /**
         *
         */
        RS_ADD_GAME_POINT(MessageCategory.RS),
        /**
         *
         */
        RS_ADD_TRIAL_POINT(MessageCategory.RS),
        /**
         *
         */
        RS_BOTH_TEAMS_READY(MessageCategory.RS),
        /**
         *
         */
        RS_GET_GAME_POINTS(MessageCategory.RS),
        /**
         *
         */
        RS_GET_REMAINING_GAMES(MessageCategory.RS),
        /**
         *
         */
        RS_GET_REMAINING_TRIALS(MessageCategory.RS),
        /**
         *
         */
        RS_GET_TRIAL_POINTS(MessageCategory.RS),
        /**
         *
         */
        RS_HAS_MATCH_ENDED(MessageCategory.RS),
        /**
         *
         */
        RS_INFORM_REFEREE(MessageCategory.RS),
        /**
         *
         */
        RS_RESET_TRIAL_POINTS(MessageCategory.RS),
        /**
         *
         */
        RS_SET_HAS_MATCH_ENDED(MessageCategory.RS);

        /**
         *
         */
        private final MessageCategory category;

        /**
         *
         */
        MessageType() {
            this.category = null;
        }

        /**
         *
         * @param category
         */
        MessageType(MessageCategory category) {
            this.category = category;
        }

        /**
         *
         * @return
         */
        public MessageCategory getCategory() {
            return category;
        }
    }

    /**
     *
     */
    public enum MessageCategory {
        /**
         *
         */
        CB, // contestantBench

        /**
         *
         */
        PG, // playground

        /**
         *
         */
        GIR, // generalinformationrepository

        /**
         *
         */
        RS; // refereesite
    }
}
