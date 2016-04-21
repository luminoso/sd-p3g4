package Communication;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee;
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
    private ContestantState contestantState;
    
    /**
     * 
     */
    private int strength;
    
    // additional variables required to define the Coach
    /**
     * 
     */
    private CoachState coachState;
    
    /**
     * 
     */
    private CoachStrategy strategy;
    
    // additional variables required to define the Referee
    /**
     * 
     */
    private RefereeState refereeState;
    
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
    private RefereeSite.GameScore gamescore;
    
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
    public Message(MessageType type, ContestantState state, int team, int id,  int strength) {
        this.type = type;
        this.contestantState = state;
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
    public Message(MessageType type,CoachState state, int team,  CoachStrategy strategy) {
        this.type = type;
        this.coachState = state;
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
    public Message(MessageType type, Referee.RefereeState state, String name ) {
        this.type = type;
        this.refereeState = refereeState;
        this.name = name;
    }

    /**
     * 
     * @param messageType 
     */
    public Message(MessageType messageType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @return 
     */
    public MessageType getType(){
        return this.type;
    }
    
    /**
     * 
     * @return 
     */
    public Set getBench(){
        return this.set;
    }
    
    /**
     * 
     * @return 
     */
    public Set getSelectedContestants(){
        return this.set;
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
        return coachState;
    }
    
    /**
     * 
     * @return 
     */
    public int getFlagPostion(){
        return this.number;
    }

    /**
     * 
     * @return 
     */
    public int getLastFlagPostion() {
        return this.number;
    }
    
    /**
     * 
     * @param position 
     */
    public void setFlagPosition(int position){
        this.number = position;
    }

    /**
     * 
     * @return 
     */
    public Referee.RefereeState getRefereeState() {
        return this.refereeState;
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
    public void setGameScore(RefereeSite.GameScore score) {
        this.gamescore = score;
    }

    /**
     * 
     * @param gameNumber 
     */
    public void setGameNumber(int gameNumber) {
        this.number = gameNumber;
    }

    /**
     * 
     * @param trialNumber 
     */
    public void setTrialNumber(int trialNumber) {
        this.number = trialNumber;
    }

    /**
     * 
     * @param score 
     */
    public void setTrialScore(RefereeSite.TrialScore score) {
        this.trialscore = score;
    }

    /**
     * 
     * @return 
     */
    public List getGamePoints() {
        return this.list;
    }

    /**
     * 
     * @return 
     */
    public int getRemainingGames() {
        return this.number;
    }

    /**
     * 
     * @return 
     */
    public int getRemainingtTrials() {
        return this.number;
    }

    /**
     * 
     * @return 
     */
    public List<RefereeSite.TrialScore> getTrialPoints() {
        return this.list;
    }

    /**
     * 
     * @return 
     */
    public boolean getHasMatchEnded() {
        return this.bool;
    }

    /**
     * 
     * @return 
     */
    public ContestantState getContestantState() {
        return this.contestantState;
    }
    
    /**
     * 
     * @return 
     */
    public MessageCategory getMessageCategory() {
        return this.type.getCategory();
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
        MessageType(){
            this.category = null;
        }
        
        /**
         * 
         * @param category 
         */
        MessageType(MessageCategory category){
            this.category = category;
        }
        
        /**
         * 
         * @return 
         */
        public MessageCategory getCategory(){
            return category;
        }
    }
    
    /**
     * 
     */
    public enum MessageCategory{
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
