package Communication;

import Others.InterfaceCoach.CoachState;
import Others.InterfaceContestant.ContestantState;
import Others.InterfaceReferee.RefereeState;
import ServerSide.RefereeSite;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Message is a Serializable class that is used in RopeGame Client/Server
 * implementation. It stores the needed methods and data types for running the
 * simulation.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 20160412;

    // Variable to define Referee
    private Enum state;

    // Adicional variables to define the Coach
    private int team;

    // Adicional variables to define Contestant
    private int id;
    private int strength;

    private MessageType type;

    /**
     * An set that this message may contain
     */
    private Set set;

    /**
     * An number that this message may contain
     */
    private int number;

    /**
     * Array of numbers that this message may contain
     */
    private int numbers[];

    /**
     * GameScore that this message may contain
     */
    private RefereeSite.GameScore gameScore;

    /**
     * TrialScore that this message may contain
     */
    private RefereeSite.TrialScore trialscore;

    /**
     * A list that this message may contain
     */
    private List list;

    /**
     * A boolean that this message may contain
     */
    private boolean bool;

    /**
     * Initialisation for Contestant
     *
     * @param type of the message
     * @param state of the contestant
     * @param team of the contestant
     * @param id of the contestant
     * @param strength of the contestant
     */
    public Message(MessageType type, ContestantState state, int team, int id, int strength) {
        this.type = type;
        this.state = state;
        this.team = team;
        this.id = id;
        this.strength = strength;
    }

    /**
     * Initialisation for the Coach
     *
     * @param type of the message
     * @param state of the Coach
     * @param team of the Coach
     */
    public Message(MessageType type, CoachState state, int team) {
        this.type = type;
        this.state = state;
        this.team = team;
    }

    /**
     * Initialisation of the Referee
     *
     * @param type of the message
     * @param state of the Referee
     */
    public Message(MessageType type, RefereeState state) {
        this.type = type;
        this.state = state;
    }

    /**
     * Initialisation of a generic Message
     *
     * @param type of the message
     */
    public Message(MessageType type) {
        this.type = type;
    }

    /**
     * Gets MessageType of the Message
     *
     * @return MessageType
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Gets the bench stored in this message
     *
     * @return Set of the bench
     */
    public Set getBench() {
        return set;
    }

    /**
     * Gets selected contestants stored in this message
     *
     * @return Set of the selected contestants
     */
    public Set getSelectedContestants() {
        return set;
    }

    /**
     * Set the selected contestants embed in the message
     *
     * @param Set of the selected contestants
     */
    public void setSelectedContestants(Set set) {
        this.set = set;
    }

    /**
     * Gets the Coach state stored in this message
     *
     * @return CoachState
     */
    public CoachState getCoachState() {
        return (CoachState) state;
    }

    /**
     * Gets the FlagPosition stored in this message
     *
     * @return FlagPosition
     */
    public int getFlagPostion() {
        return number;
    }

    /**
     * Gets the LastFlagPosition stored in this message
     *
     * @return LastFlagPosition
     */
    public int getLastFlagPostion() {
        return number;
    }

    /**
     * Sets FlagPosition embed in this message
     *
     * @param position of the flag
     */
    public void setFlagPosition(int position) {
        number = position;
    }

    /**
     * Gets the Referee stored in this message
     *
     * @return RefereeState
     */
    public RefereeState getRefereeState() {
        return (RefereeState) state;
    }

    /**
     * Gets the Array of Integers stored in this message
     *
     * @return Array of Integers
     */
    public int[] getNumbers() {
        return numbers;
    }

    /**
     * Sets the Array of Integers embed in this message
     *
     * @param numbers in an Array
     */
    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    /**
     * Gets the GamePoint embed in this message
     *
     * @param score
     */
    public void setGamePoint(RefereeSite.GameScore score) {
        gameScore = score;
    }

    /**
     * Gets the GamePoint stored in this message
     *
     * @return gamePoint
     */
    public RefereeSite.GameScore getGamePoint() {
        return gameScore;
    }

    /**
     * Gets the GameNumber embed in this message
     *
     * @param gameNumber
     */
    public void setGameNumber(int gameNumber) {
        number = gameNumber;
    }

    /**
     * Gets the TrialNumber embed in this message
     *
     * @param trialNumber
     */
    public void setTrialNumber(int trialNumber) {
        number = trialNumber;
    }

    /**
     * Gets the TrialScore embed in this message
     *
     * @param score
     */
    public void setTrialScore(RefereeSite.TrialScore score) {
        trialscore = score;
    }

    /**
     * Gets the game points stored in this message
     *
     * @return list of GamePoints
     */
    public List getGamePoints() {
        return list;
    }

    /**
     * Gets the remaining games stored in this message
     *
     * @return remainingGames
     */
    public int getRemainingGames() {
        return number;
    }

    /**
     * Gets the remaining trials stored in this message
     *
     * @return remainingTrials
     */
    public int getRemainingtTrials() {
        return number;
    }

    /**
     * Gets the trial points stored in this message
     *
     * @return list of the trial points
     */
    public List<RefereeSite.TrialScore> getTrialPoints() {
        return list;
    }

    /**
     * Gets if the match has ended stored in this message
     *
     * @return boolean true if match has ended. False otherwise.
     */
    public boolean getHasMatchEnded() {
        return bool;
    }

    /**
     * Gets the contestant state stored in this message
     *
     * @return ContestantState
     */
    public ContestantState getContestantState() {
        return (ContestantState) state;
    }

    /**
     * Gets this message category
     *
     * @return MessageCategory
     */
    public MessageCategory getMessageCategory() {
        return type.getCategory();
    }

    /**
     * Gets the team stored in this message
     *
     * @return team number
     */
    public int getTeam() {
        return team;
    }

    /**
     * Gets the state stored in this message
     *
     * @return Enum with the state
     */
    public Enum getState() {
        return state;
    }

    /**
     * Gets the contestant state stored in this message
     *
     * @param state of the contestant
     */
    public void setContestantState(Enum state) {
        this.state = state;
    }

    /**
     * Sets coach state embed in this message
     *
     * @param state of the coach
     */
    public void setStateCoachState(Enum state) {
        this.state = state;
    }

    /**
     * Sets an Set embed in this message
     *
     * @param set to be stored
     */
    public void setSet(Set set) {
        this.set = set;
    }

    /**
     * Gets the contestant id embed in this message
     *
     * @return id of the contestant
     */
    public int getContestantId() {
        return id;
    }

    /**
     * Sets the team embed in this message
     *
     * @param team number
     */
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * Sets the coach state embed in this message
     *
     * @param coachState to be stored
     */
    public void setCoachState(CoachState coachState) {
        state = coachState;
    }

    /**
     * Sets the last flag position embed in this message
     *
     * @param lastFlagPosition to be stored
     */
    public void setLastFlagPosition(int lastFlagPosition) {
        number = lastFlagPosition;
    }

    /**
     * Sets the referee state embed in this message
     *
     * @param refereeState to be stored
     */
    public void setRefereeState(RefereeState refereeState) {
        state = refereeState;
    }

    /**
     * Gets the trial point stored in this message
     *
     * @return TrialScore trialPoint
     */
    public RefereeSite.TrialScore getTrialPoint() {
        return trialscore;
    }

    /**
     * Sets game points embed in this message
     *
     * @param gamePoints to be stored
     */
    public void setGamePoints(List<RefereeSite.GameScore> gamePoints) {
        list = gamePoints;
    }

    /**
     * Sets remaining games embed in this message
     *
     * @param remainingGames to be stored
     */
    public void setRemainingGames(int remainingGames) {
        number = remainingGames;
    }

    /**
     * Sets remaining trials embed in this message
     *
     * @param remainingTrials to be stored
     */
    public void setRemainingTrials(int remainingTrials) {
        number = remainingTrials;
    }

    /**
     * Set trial points embed in this message
     *
     * @param trialPoints to be stored
     */
    public void setTrialPoints(List<RefereeSite.TrialScore> trialPoints) {
        list = trialPoints;
    }

    /**
     * Set if the match as ended embed in this message
     *
     * @param hasMatchEnded
     */
    public void setHasMatchEnded(boolean hasMatchEnded) {
        bool = hasMatchEnded;
    }

    /**
     * Sets game result embed in this message
     *
     * @param score of the game
     */
    public void setGameResult(RefereeSite.GameScore score) {
        gameScore = score;
    }

    /**
     * Gets the contestant strength stored in this message
     *
     * @return strength of the contestant
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Gets the game result stored in this message
     *
     * @return gamescore
     */
    public RefereeSite.GameScore getGameResult() {
        return gameScore;
    }

    /**
     * Gets the game number stored in this message
     *
     * @return gamenumber
     */
    public int getGameNumber() {
        return number;
    }

    /**
     * Gets the trial number stored in this message
     *
     * @return trialnumber
     */
    public int getTrialNumber() {
        return number;
    }

    /**
     * Sets the contestant strength embed in this message
     *
     * @param strength of the contested to be stored
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Enums that describe the message type according to its
     * function/destination in a Client/Server message implementation
     */
    public enum MessageType {
        /**
         * Type of return of messages.
         */
        OK(MessageCategory.GENERIC),
        INTERRUPT(MessageCategory.GENERIC),
        SHUTDOWN(MessageCategory.GENERIC),
        /**
         * Type of embed that in the message.
         */
        BENCH(MessageCategory.GENERIC),
        SELECTED_CONTESTANTS(MessageCategory.GENERIC),
        FLAG_POSITION(MessageCategory.GENERIC),
        LAST_FLAG_POSITION(MessageCategory.GENERIC),
        GAME_POINTS(MessageCategory.GENERIC),
        TRIAL_POINTS(MessageCategory.GENERIC),
        REMAINING_GAMES(MessageCategory.GENERIC),
        REMAINING_TRIALS(MessageCategory.GENERIC),
        BOOLEAN(MessageCategory.GENERIC),
        COACH_STATE_CHANGE(MessageCategory.GENERIC),
        REFEREE_STATE_CHANGE(MessageCategory.GENERIC),
        CONTESTANT_STATE_CHANGE(MessageCategory.GENERIC),
        /**
         * Methods that can be called in the ContestantsBench.
         */
        CB_ADD_CONTESTANT(MessageCategory.CB),
        CB_GET_BENCH(MessageCategory.CB),
        CB_GET_CONTESTANT(MessageCategory.CB),
        CB_GET_SELECTED_CONTESTANTS(MessageCategory.CB),
        CB_PICK_YOUR_TEAM(MessageCategory.CB),
        CB_SET_SELECTED_CONTESTANTS(MessageCategory.CB),
        CB_WAIT_FOR_NEXT_TRIAL(MessageCategory.CB),
        CB_UPDATE_CONTESTANT_STRENGTH(MessageCategory.CB),
        /**
         * Methods that can be called in the PlaygGround.
         */
        PG_ADD_CONTESTANT(MessageCategory.PG),
        PG_CHECK_TEAM_PLACEMENT(MessageCategory.PG),
        PG_GET_CONTESTANT(MessageCategory.PG),
        PG_GET_FLAG_POSITION(MessageCategory.PG),
        PG_GET_LAST_FLAG_POSITION(MessageCategory.PG),
        PG_PULL_ROPE(MessageCategory.PG),
        PG_RESULT_ASSERTED(MessageCategory.PG),
        PG_SET_FLAG_POSITION(MessageCategory.PG),
        PG_START_PULLING(MessageCategory.PG),
        PG_WATCH_TRIAL(MessageCategory.PG),
        /**
         * Methods that can be called in the GeneralInformationRepository.
         */
        GIR_UPDATE_COACH(MessageCategory.GIR),
        GIR_UPDATE_CONTESTANT(MessageCategory.GIR),
        GIR_UPDATE_CONTESTANT_STRENGTH(MessageCategory.GIR),
        GIR_UPDATE_REFEREE(MessageCategory.GIR),
        GIR_CLOSE(MessageCategory.GIR),
        GIR_PRINT_GAME_HEADER(MessageCategory.GIR),
        GIR_PRINT_GAME_RESULT(MessageCategory.GIR),
        GIR_PRINT_HEADER(MessageCategory.GIR),
        GIR_PRINT_LEGEND(MessageCategory.GIR),
        GIR_PRINT_LINE_UPDATE(MessageCategory.GIR),
        GIR_PRINT_MATCH_DRAW(MessageCategory.GIR),
        GIR_PRINT_MATCH_WINNER(MessageCategory.GIR),
        GIR_RESET_TEAM_PLACEMENT(MessageCategory.GIR),
        GIR_SET_FLAG_POSITION(MessageCategory.GIR),
        GIR_SET_GAME_NUMBER(MessageCategory.GIR),
        GIR_SET_TEAM_PLACEMENT(MessageCategory.GIR),
        GIR_SET_TRIAL_NUMBER(MessageCategory.GIR),
        /**
         * Methods that can be called in the RefereeSite.
         */
        RS_ADD_GAME_POINT(MessageCategory.RS),
        RS_ADD_TRIAL_POINT(MessageCategory.RS),
        RS_BOTH_TEAMS_READY(MessageCategory.RS),
        RS_GET_GAME_POINTS(MessageCategory.RS),
        RS_GET_REMAINING_GAMES(MessageCategory.RS),
        RS_GET_REMAINING_TRIALS(MessageCategory.RS),
        RS_GET_TRIAL_POINTS(MessageCategory.RS),
        RS_HAS_MATCH_ENDED(MessageCategory.RS),
        RS_INFORM_REFEREE(MessageCategory.RS),
        RS_RESET_TRIAL_POINTS(MessageCategory.RS),
        RS_SET_HAS_MATCH_ENDED(MessageCategory.RS);

        /**
         * Stores the message category
         */
        private final MessageCategory category;

        /**
         * Constructor for the MessageType
         *
         * @param category
         */
        MessageType(MessageCategory category) {
            this.category = category;
        }

        /**
         * Gets the MessageCategory
         *
         * @return MessageCategory
         */
        public MessageCategory getCategory() {
            return category;
        }
    }

    /**
     * Enums for the MessageCategory
     */
    public enum MessageCategory {
        /**
         * Message with Generic means no specific shared memory method.
         */
        GENERIC,
        /**
         * Message with ContestantsBench access methods.
         */
        CB,
        /**
         * Message with Playground access methods.
         */
        PG,
        /**
         * Message with General Repository Information access methods.
         */
        GIR,
        /**
         * Message with RefereSite access methods.
         */
        RS;
    }
}
