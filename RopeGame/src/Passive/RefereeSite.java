package Passive;

/**
 *
 * @author Eduardo Sousa
 */
public class RefereeSite {
    private int matchRound, trialRound, matchPoints, trialPoints;

    public RefereeSite() {
        this.matchPoints = 0;
        this.matchRound = 0;
        this.trialPoints = 0;
        this.trialRound = 0;
    
    }

    public int getMatchRound() {
        return matchRound;
    }

    public int getTrialRound() {
        return trialRound;
    }

    public int getMatchPoints() {
        return matchPoints;
    }

    public int getTrialPoints() {
        return trialPoints;
    }
    
}
