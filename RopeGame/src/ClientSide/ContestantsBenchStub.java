/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import Others.Bench;
import Others.InterfaceContestantsBench;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author luminoso
 */
public class ContestantsBenchStub extends Bench implements InterfaceContestantsBench {

    private static final ContestantsBenchStub[] instances = new ContestantsBenchStub[2];    // Doubleton containing the two teams benches
    private final int team;

    /**
     * Method that returns a ContestantsBenchStub object. The method is
     * thread-safe and uses the implicit monitor of the class.
     *
     * @return ContestantsBenchStub object specified by the team identifier
     * passed.
     */
    public static synchronized ContestantsBenchStub getInstance(int team) {

        if (instances[team - 1] == null) {
            instances[team - 1] = new ContestantsBenchStub(team);
        }

        return instances[team - 1];
    }

    public static synchronized List<ContestantsBenchStub> getInstances() {
        List<ContestantsBenchStub> temp = new LinkedList<>();

        for (int i = 0; i < instances.length; i++) {
            if (instances[i] == null) {
                instances[i] = new ContestantsBenchStub(i);
            }

            temp.add(instances[i]);
        }

        return temp;
    }

    /**
     * Private constructor to be used in the doubleton.
     *
     * @param team Team identifier.
     */
    private ContestantsBenchStub(int team) {
        this.team = team;
    }

    public void addContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();
        
        // message:
        // contestat team
        // contestant id
        // contestant strengh
        // method: addContestant
    }

    @Override
    public Set<Contestant> getBench() {
        Set<Contestant> temp = null;
        
        // coach
        // teamid
        
        return temp;
    }

    @Override
    public void getContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();
        
        // contestant team
        // contestant id
        // contestant strength
        
    }

    @Override
    public Set<Integer> getSelectedContestants() {
        Coach coach = (Coach) Thread.currentThread();
        
        Set<Integer> selected = null;
        
        // coach team
        
        return selected;
    }

    @Override
    public void pickYourTeam() {
        // Referee
        
        // jus call the method
    }

    @Override
    public void setSelectedContestants(Set<Integer> selected) {
        Coach coach = (Coach) Thread.currentThread();
        // coach team
        // just call the method
    }

    @Override
    public void waitForNextTrial() {
        Coach coach = (Coach) Thread.currentThread();
        
        // coach state changes!
        // coach team
        
        
    }
    
    @Override
    public List<ContestantsBenchStub> getBenches() {

        List<ContestantsBenchStub> temp = new LinkedList<>();

        for (int i = 0; i < instances.length; i++) {
            if (instances[i] == null) {
                instances[i] = new ContestantsBenchStub(i);
            }

            temp.add(instances[i]);
        }

        return temp;

    }
    
    

}
