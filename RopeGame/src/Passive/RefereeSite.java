package Passive;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RefereeSite {
    private static RefereeSite instance;
    
    private int[] gamePoints, trialPoints;
    private int gameRound, trialRound;

    public static RefereeSite getInstance() {
        if(instance == null) {
            instance = new RefereeSite();
        }
        
        return instance;
    }
    
    private RefereeSite() {
        this.gameRound = 0;
        this.trialRound = 0;
    
        this.trialPoints = new int[2];
        this.gamePoints = new int[2];
    }

    public int getGameRound() {
        return gameRound;
    }

    public int getTrialRound() {
        return trialRound;
    }

    public int[] getGamePoints() {
        return gamePoints;
    }

    public int[] getTrialPoints() {
        return trialPoints;
    }

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    public void setTrialRound(int trialRound) {
        this.trialRound = trialRound;
    }

    public void setGamePoints(int[] gamePoints) {
        this.gamePoints = gamePoints;
    }

    public void setTrialPoints(int[] trialPoints) {
        this.trialPoints = trialPoints;
    }
}
