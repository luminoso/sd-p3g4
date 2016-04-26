package ClientSide;

import Communication.Message;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfaceContestantsBench;
import Others.InterfaceReferee;
import RopeGame.ServerConfigs;
import static java.lang.System.out;
import java.util.Set;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ContestantsBenchStub implements InterfaceContestantsBench {
    /**
     *
     */
    private final int team;

    /**
     * Private constructor to be used in the doubleton.
     *
     * @param team Team identifier.
     */
    public ContestantsBenchStub(int team) {
        this.team = team;
    }

    /**
     *
     * @return
     */
    private ClientCom initiateConnection() {
        ClientCom con = new ClientCom(ServerConfigs.CONTESTANTS_BENCH_ADDRESS,
                ServerConfigs.CONTESTANTS_BENCH_PORT);

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
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_ADD_CONTESTANT,
                contestant.getContestantState(),
                contestant.getContestantTeam(),
                contestant.getContestantId(),
                contestant.getContestantStrength());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.CONTESTANT_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        contestant.setContestantState(inMessage.getContestantState());

        con.close();
    }

    /**
     *
     * @return
     */
    @Override
    public Set<InterfaceContestant> getBench() {
        //TODO: Coach strategies also access this function
        // instruction below may crash ? 
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_GET_BENCH,
                coach.getCoachState(),
                coach.getCoachTeam(),
                coach.getCoachStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.BENCH) {
            out.println("Message type error");
        }

        Set<InterfaceContestant> bench = inMessage.getBench();

        return bench;
    }

    /**
     *
     */
    @Override
    public void getContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_GET_CONTESTANT,
                contestant.getContestantState(),
                contestant.getContestantTeam(),
                contestant.getContestantId(),
                contestant.getContestantStrength());

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
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_GET_SELECTED_CONTESTANTS,
                coach.getCoachState(),
                coach.getCoachTeam(),
                coach.getCoachStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.SELECTED_CONTESTANTS) {
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_PICK_YOUR_TEAM,
                referee.getRefereeState());

        // TODO: melhor metodo de lidar com este caso muito particular?
        outMessage.setTeam(team);

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
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_SET_SELECTED_CONTESTANTS,
                coach.getCoachState(),
                coach.getCoachTeam(),
                coach.getCoachStrategy());

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
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.CB_WAIT_FOR_NEXT_TRIAL,
                coach.getCoachState(),
                coach.getCoachTeam(),
                coach.getCoachStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.COACH_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        coach.setCoachState(inMessage.getCoachState());

        con.close();

    }
}
