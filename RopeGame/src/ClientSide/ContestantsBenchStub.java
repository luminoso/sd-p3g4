package ClientSide;

import Communication.Message;
import Others.Bench;
import Others.InterfaceContestantsBench;
import static java.lang.System.out;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBenchStub extends Bench implements InterfaceContestantsBench {

    /**
     * 
     */
    private static final ContestantsBenchStub[] instances = new ContestantsBenchStub[2];    // Doubleton containing the two teams benches
    
    /**
     * 
     */
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

    /**
     * 
     * @return 
     */
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

    /**
     * 
     * @return 
     */
    private ClientCom initiateConnection() {
        ClientCom con = new ClientCom(ClientRopeGame.getServerHostName(),
                ClientRopeGame.getServerPortNumb());

        if (!con.open()) {
            // TODO: handle later
            //return false; // server doesn't accept more connections
        }

        return con;
    }

    /**
     * 
     */
    public void addContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_addContestant,
                contestant.getContestantState(),
                contestant.getTeam(),
                contestant.getContestatId(),
                contestant.getStrength());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.CONTESTANT_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        contestant.setState(inMessage.getContestantState());

        con.close();
    }

    /**
     * 
     * @return 
     */
    @Override
    public Set<Contestant> getBench() {
        //TODO: Coach strategies also access this function
        // instruction below may crash ? 
        Coach coach = (Coach) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_getBench,
                coach.getCoachState(),
                coach.getTeam(),
                coach.getStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.BENCH) {
            out.println("Message type error");
        }

        Set bench = inMessage.getBench();

        return bench;
    }

    /**
     * 
     */
    @Override
    public void getContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_getContestant,
                contestant.getContestantState(),
                contestant.getTeam(),
                contestant.getContestatId(),
                contestant.getStrength());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            out.print("Message type error");
        }

    }

    /**
     * 
     * @return 
     */
    @Override
    public Set<Integer> getSelectedContestants() {
        //TODO: KeepWinningTeam also access this function
        // instruction below may crash ?
        Coach coach = (Coach) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_getSelectedContestants,
                coach.getCoachState(),
                coach.getTeam(),
                coach.getStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.SELECTEDCONTESTANTS) {
            out.println("Message type error");
        }

        Set selectedContestants = inMessage.getSelectedContestants();

        return selectedContestants;
    }

    /**
     * 
     */
    @Override
    public void pickYourTeam() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_pickYourTeam,
                referee.getRefereeState(),
                referee.getName());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    /**
     * 
     * @param selected 
     */
    @Override
    public void setSelectedContestants(Set<Integer> selected) {
        Coach coach = (Coach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_setSelectedContestants,
                coach.getCoachState(),
                coach.getTeam(),
                coach.getStrategy());

        outMessage.setSelectedContestants(selected);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

    }

    /**
     * 
     */
    @Override
    public void waitForNextTrial() {

        // coach state changes!
        // GeneralInformationRepository.getInstance().printLineUpdate();
        Coach coach = (Coach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_waitForNextTrial,
                coach.getCoachState(),
                coach.getTeam(),
                coach.getStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.COACH_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        coach.setState(inMessage.getCoachState());

        con.close();

    }

    /**
     * 
     * @return 
     */
    @Override
    public synchronized List<ContestantsBenchStub> getBenches() {

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
