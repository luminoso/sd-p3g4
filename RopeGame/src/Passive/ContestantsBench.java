package Passive;

import Active.Contestant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 */
public class ContestantsBench {
    
    private List<Contestant> bench = new ArrayList<>();
    private int team;
  
    // create and initialize(?)
    public ContestantsBench(int benchSize, int team, int strengthFactor) {
        this.team = team;
        
        for(int i = 0; i < benchSize; i++)
            bench.add(new Contestant(GenerateName(), team, i, (int) (Math.random()*strengthFactor)));
        
    }
    
    public void addContestant(Contestant contestant){
        bench.add(contestant);
    }
    
    public Contestant getContestant(int id){
        
        for(Contestant contestant : bench){
            if(contestant.getId() == id){
                bench.remove(contestant);
                return contestant;
            }
        }
        //throw new ContestantNotFoundException();
        return null;
    }
    
    public Contestant getContestant(){
        if(!bench.isEmpty()){
            Contestant contestant = bench.get(bench.size() - 1);
            bench.remove(bench.size() - 1);
            return contestant;
        }
        return null;
    }

    private String GenerateName() {
        String[] FirstName = {"James", "John", "Robert", "Michael", "William"};
        String[] LastName = {"Smith", "Johnson", "Williams", "Brown", "Jones"};
        
        String name = FirstName[(int)(Math.random() * FirstName.length)] + " " + LastName[(int)(Math.random() * LastName.length)];
        
        return name;
    }

}
