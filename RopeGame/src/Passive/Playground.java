package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 */
public class Playground {

    private int flagPosition;
    private List<Contestant> teamA = new ArrayList<>();
    private List<Contestant> teamB = new ArrayList<>();
    
   
    public void addContestantToTeamA(Contestant contestant){
        teamA.add(contestant);
    }
    
    public void addContestanToTeamB(Contestant contestant){
        teamB.add(contestant);
    }
    
    public Contestant getContestantTeamA(int id){
        
        for(Contestant contestant : teamA){
            if(contestant.getId() == id){
                teamA.remove(contestant);
                return contestant;
            }
        }
        //throw new ContestantNotFoundException();
        return null;
    }
    
    public Contestant getContestantTeamB(int id){
        
        for(Contestant contestant : teamB){
            if(contestant.getId() == id){
                teamB.remove(contestant);
                return contestant;
            }
        }
        return null;
    }
    
    public Contestant getContestantTeamA(){
        if(!teamA.isEmpty()){
            Contestant contestant = teamA.get(teamA.size());
            teamA.remove(teamA.size());
            return contestant;
        }
        return null;
    }
    
    public Contestant getContestantTeamB(){
        if(!teamB.isEmpty()){
            Contestant contestant = teamB.get(teamB.size() - 1);
            teamB.remove(teamB.size() - 1);
            return contestant;
        }
        return null;
    }
    
    public int getFlagPosition(){
        return this.flagPosition;
    }
    
}
