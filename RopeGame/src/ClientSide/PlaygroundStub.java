package ClientSide;

import Communication.Message;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfacePlayground;
import Others.InterfaceReferee;
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
    public PlaygroundStub() {}

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
    public boolean checkAllContestantsReady() {
        //TODO: not used? delete from interface, etc
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    @Override
    public void checkTeamPlacement() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_CHECK_TEAM_PLACEMENT,
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

    /**
     *
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
            // TODO: handle error
            System.exit(1);
        }

    }

    /**
     *
     * @return
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
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        return inMessage.getFlagPostion();
    }

    /**
     *
     * @return
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
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        return inMessage.getLastFlagPostion();
    }

    /**
     *
     * @return
     */
    @Override
    public List<InterfaceContestant>[] getTeams() {
        // unused function?
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    @Override
    public void haveAllPulled() {
        // unused function?
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
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
     *
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
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    /**
     *
     * @param flagPosition
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
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    /**
     *
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
            // TODO: handle error
            System.exit(1);
        }

        referee.setRefereeState(inMessage.getRefereeState());

        con.close();
    }

    /**
     *
     */
    @Override
    public void watchTrial() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_WATCH_TRIAL,
                coach.getCoachState(),
                coach.getCoachTeam(),
                coach.getCoachStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.COACH_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        coach.setCoachState(inMessage.getCoachState());
    }
}
