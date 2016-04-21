package Communication;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee.RefereeState;
import Others.CoachStrategy;
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

    /**
     *
     */
    private CoachStrategy strategy;

    // additional variables required to define the Referee
    /**
     *
     */
    private Enum state;

    /**
     *
     */
    private String name;

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
    public Message(MessageType type, String name, ContestantState state, int team, int id, int strength) {
        this.type = type;
        this.name = name;
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
    public Message(MessageType type, String name, CoachState state, int team, CoachStrategy strategy) {
        this.type = type;
        this.name = name;
        this.state = state;
        this.team = team;
        this.strategy = strategy;
    }

    // initialization for Referee
    /**
     *
     * @param type
     * @param state
     * @param name
     */
    public Message(MessageType type, String name, RefereeState state) {
        this.type = type;
        this.name = name;
        this.state = state;
    }

    /**
     *
     * @param messageType
     */
    public Message(MessageType messageType) {
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CoachStrategy getStrategy() {
        return strategy;
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
        SELECTEDCONTESTANTS,
        /**
         *
         */
        FLAGPOSITION,
        /**
         *
         */
        LASTFLAGPOSITION,
        /**
         *
         */
        GAMEPOINTS,
        /**
         *
         */
        TRIALPOINTS,
        /**
         *
         */
        REMAININGGAMES,
        /**
         *
         */
        REMAININGTRIALS,
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
        CB_addContestant(MessageCategory.CB),
        /**
         *
         */
        CB_getBench(MessageCategory.CB),
        /**
         *
         */
        CB_getContestant(MessageCategory.CB),
        /**
         *
         */
        CB_getSelectedContestants(MessageCategory.CB),
        /**
         *
         */
        CB_pickYourTeam(MessageCategory.CB),
        /**
         *
         */
        CB_setSelectedContestants(MessageCategory.CB),
        /**
         *
         */
        CB_waitForNextTrial(MessageCategory.CB),
        /**
         *
         */
        PG_addContestant(MessageCategory.PG),
        /**
         *
         */
        PG_checkTeamPlacement(MessageCategory.PG),
        /**
         *
         */
        PG_getContestant(MessageCategory.PG),
        /**
         *
         */
        PG_getFlagPosition(MessageCategory.PG),
        /**
         *
         */
        PG_getLastFlagPosition(MessageCategory.PG),
        /**
         *
         */
        PG_pullRope(MessageCategory.PG),
        /**
         *
         */
        PG_resultAsserted(MessageCategory.PG),
        /**
         *
         */
        PG_setFlagPosition(MessageCategory.PG),
        /**
         *
         */
        PG_startPulling(MessageCategory.PG),
        /**
         *
         */
        PG_watchTrial(MessageCategory.PG),
        /**
         *
         */
        GIR_addCoach(MessageCategory.GIR),
        /**
         *
         */
        GIR_addContestant(MessageCategory.GIR),
        /**
         *
         */
        GIR_addReferee(MessageCategory.GIR),
        /**
         *
         */
        GIR_close(MessageCategory.GIR),
        /**
         *
         */
        GIR_printGameHeader(MessageCategory.GIR),
        /**
         *
         */
        GIR_printGameResult(MessageCategory.GIR),
        /**
         *
         */
        GIR_printHeader(MessageCategory.GIR),
        /**
         *
         */
        GIR_printLegend(MessageCategory.GIR),
        /**
         *
         */
        GIR_printLineUpdate(MessageCategory.GIR),
        /**
         *
         */
        GIR_printMatchDraw(MessageCategory.GIR),
        /**
         *
         */
        GIR_printMatchWinner(MessageCategory.GIR),
        /**
         *
         */
        GIR_resetTeamPlacement(MessageCategory.GIR),
        /**
         *
         */
        GIR_setFlagPosition(MessageCategory.GIR),
        /**
         *
         */
        GIR_setGameNumber(MessageCategory.GIR),
        /**
         *
         */
        GIR_setTeamPlacement(MessageCategory.GIR),
        /**
         *
         */
        GIR_setTrialNumber(MessageCategory.GIR),
        /**
         *
         */
        RS_addGamePoint(MessageCategory.RS),
        /**
         *
         */
        RS_addTrialPoint(MessageCategory.RS),
        /**
         *
         */
        RS_bothTeamsReady(MessageCategory.RS),
        /**
         *
         */
        RS_getGamePoints(MessageCategory.RS),
        /**
         *
         */
        RS_getRemainingGames(MessageCategory.RS),
        /**
         *
         */
        RS_getRemainingTrials(MessageCategory.RS),
        /**
         *
         */
        RS_getTrialPoints(MessageCategory.RS),
        /**
         *
         */
        RS_hasMatchEnded(MessageCategory.RS),
        /**
         *
         */
        RS_informReferee(MessageCategory.RS),
        /**
         *
         */
        RS_resetTrialPoints(MessageCategory.RS),
        /**
         *
         */
        RS_setHasMatchEnded(MessageCategory.RS);

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
