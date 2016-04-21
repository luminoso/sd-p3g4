package ClientSide;

import Communication.Message;
import Others.Ground;
import Others.InterfacePlayground;
import static java.lang.System.out;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class PlaygroundStub extends Ground implements InterfacePlayground {

    /**
     *
     */
    private static PlaygroundStub instance;

    /**
     * The method returns the Playground object. This method is thread-safe and
     * uses the implicit monitor of the class.
     *
     * @return Playground object to be used.
     */
    public static synchronized PlaygroundStub getInstance() {
        if (instance == null) {
            instance = new PlaygroundStub();
        }

        return instance;
    }

    /**
     * Private constructor to be used in the singleton.
     */
    private PlaygroundStub() {
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
    @Override
    public void addContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_addContestant,
                contestant.getName(),
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
    public boolean checkAllContestantsReady() {
        //TODO: not used? delete from interface, etc
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    @Override
    public void checkTeamPlacement() {
        Coach coach = (Coach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_checkTeamPlacement,
                coach.getName(),
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
     */
    @Override
    public void getContestant() {
        Contestant contestant = (Contestant) Thread.currentThread();

        // contestant team
        // contestant id
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_getContestant,
                contestant.getName(),
                contestant.getContestantState(),
                contestant.getTeam(),
                contestant.getContestatId(),
                contestant.getStrength());

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
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_getFlagPosition,
                referee.getName(),
                referee.getRefereeState());;

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.FLAGPOSITION) {
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
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_getLastFlagPosition,
                referee.getName(),
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.LASTFLAGPOSITION) {
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
    public List<Contestant>[] getTeams() {
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
        Contestant contestant = (Contestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_pullRope,
                contestant.getName(),
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
     */
    @Override
    public void resultAsserted() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_resultAsserted,
                referee.getName(),
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
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_setFlagPosition,
                referee.getName(),
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
        Referee referee = (Referee) Thread.currentThread();

        // Referee state changes
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_startPulling,
                referee.getName(),
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.REFEREE_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        referee.setState(inMessage.getRefereeState());

        con.close();
    }

    /**
     *
     */
    @Override
    public void watchTrial() {
        Coach coach = (Coach) Thread.currentThread();

        // coach team
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.PG_watchTrial,
                coach.getName(),
                coach.getCoachState(),
                coach.getTeam(),
                coach.getStrategy());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.COACH_STATE_CHANGE) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        coach.setState(inMessage.getCoachState());
    }
}
