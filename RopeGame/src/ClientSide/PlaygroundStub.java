package ClientSide;

import Communication.Message;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfacePlayground;
import Others.InterfaceReferee;
import RopeGame.ServerConfigs;
import static java.lang.System.out;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class PlaygroundStub implements InterfacePlayground {

    /**
     * Private constructor to be used in the singleton.
     */
    public PlaygroundStub() {
    }

    /**
     * Initiates the connection to the Server according to the ServerConfigs
     *
     * @return ClientCom with the opened connection
     */
    private ClientCom initiateConnection() {
        ClientCom con = new ClientCom(ServerConfigs.PLAYGROUND_ADDRESS,
                ServerConfigs.PLAYGROUND_PORT);

        if (!con.open()) {
            out.println("Couldn't initiate connection to "
                    + ServerConfigs.PLAYGROUND_ADDRESS + ":"
                    + ServerConfigs.PLAYGROUND_PORT);
        }

        return con;
    }

    /**
     * The method adds a contestant to the playground.
     */
    @Override
    public void addContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_ADD_CONTESTANT,
                contestant.getContestantState(),
                contestant.getContestantTeam(),
                contestant.getContestantId(),
                contestant.getContestantStrength());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.CONTESTANT_STATE_CHANGE) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        contestant.setContestantState(inMessage.getContestantState());

        con.close();

    }

    /**
     * Checks if all contestants are ready to pull the rope
     *
     * @return true if every Contestant is in place to pull the rope
     */
    @Override
    public boolean checkAllContestantsReady() {
        //TODO: not used? delete from interface, etc
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Synchronisation point for waiting for the teams to be ready
     */
    @Override
    public void checkTeamPlacement() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_CHECK_TEAM_PLACEMENT,
                coach.getCoachState(),
                coach.getCoachTeam());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.COACH_STATE_CHANGE) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        coach.setCoachState(inMessage.getCoachState());

        con.close();
    }

    /**
     * The method removes the contestant from the playground.
     */
    @Override
    public void getContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_GET_CONTESTANT,
                contestant.getContestantState(),
                contestant.getContestantTeam(),
                contestant.getContestantId(),
                contestant.getContestantStrength());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

    }

    /**
     * The method returns the flag position in relation to the middle. Middle =
     * 0.
     *
     * @return Position of the flag.
     */
    @Override
    public int getFlagPosition() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_GET_FLAG_POSITION,
                referee.getRefereeState());;

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.FLAG_POSITION) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();

        return inMessage.getFlagPostion();
    }

    /**
     * Gets the last flag position
     *
     * @return the flag position before the current position
     */
    @Override
    public int getLastFlagPosition() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_GET_LAST_FLAG_POSITION,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.LAST_FLAG_POSITION) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();

        return inMessage.getLastFlagPostion();
    }

    /**
     * Gets the current teams in the playground
     *
     * @return List containing both teams Contestants in the playground
     */
    @Override
    public List<InterfaceContestant>[] getTeams() {
        // unused function?
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Checks if everyone pulled the rope
     */
    @Override
    public void haveAllPulled() {
        // unused function?
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Contestant pulls the rope
     */
    @Override
    public void pullRope() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_PULL_ROPE,
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
     * Synchronisation point for signalling the result is asserted
     */
    @Override
    public void resultAsserted() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_RESULT_ASSERTED,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    /**
     * Sets the flag position
     *
     * @param flagPosition position of the flag
     */
    @Override
    public void setFlagPosition(int flagPosition) {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_SET_FLAG_POSITION,
                referee.getRefereeState());

        outMessage.setFlagPosition(flagPosition);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    /**
     * Referee instructs the Contestants to start pulling the rope
     */
    @Override
    public void startPulling() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        // Referee state changes
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_START_PULLING,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.REFEREE_STATE_CHANGE) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        referee.setRefereeState(inMessage.getRefereeState());

        con.close();
    }

    /**
     * Synchronisation point for watching the trial in progress
     */
    @Override
    public void watchTrial() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_WATCH_TRIAL,
                coach.getCoachState(),
                coach.getCoachTeam());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.COACH_STATE_CHANGE) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();

        coach.setCoachState(inMessage.getCoachState());
    }
}
