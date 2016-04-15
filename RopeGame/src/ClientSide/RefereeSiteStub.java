/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import Others.InterfaceRefereeSite;
import Others.Site;
import ServerSide.RefereeSite;
import java.util.List;

/**
 *
 * @author luminoso
 */
public class RefereeSiteStub extends Site implements InterfaceRefereeSite {
    
    private static RefereeSiteStub instance;

    /**
     * The method returns the RefereeSite object. The method is thread-safe and
     * uses the implicit monitor of the class.
     * 
     * @return RefereeSite object to be used.
     */
    public static synchronized RefereeSiteStub getInstance() {
        if(instance == null) {
            instance = new RefereeSiteStub();
        }
        
        return instance;
    }
    
    /**
     * Private constructor to be used in singleton.
     */
    private RefereeSiteStub() {
        
    }
    
    
    @Override
    public void addGamePoint(RefereeSite.GameScore score) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTrialPoint(RefereeSite.TrialScore score) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bothTeamsReady() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RefereeSite.GameScore> getGamePoints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRemainingGames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRemainingTrials() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RefereeSite.TrialScore> getTrialPoints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasMatchEnded() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void informReferee() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetTrialPoints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHasMatchEnded(boolean hasMatchEnded) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
