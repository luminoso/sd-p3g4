package ClientSide;

import Communication.Message;
import Others.InterfaceRefereeSite;
import Others.Site;
import ServerSide.RefereeSite;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RefereeSiteStub extends Site implements InterfaceRefereeSite {

    /**
     *
     */
    private static RefereeSiteStub instance;

    /**
     * The method returns the RefereeSite object. The method is thread-safe and
     * uses the implicit monitor of the class.
     *
     * @return RefereeSite object to be used.
     */
    public static synchronized RefereeSiteStub getInstance() {
        if (instance == null) {
            instance = new RefereeSiteStub();
        }

        return instance;
    }

    /**
     * Private constructor to be used in singleton.
     */
    private RefereeSiteStub() {

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
     * @param score
     */
    @Override
    public void addGamePoint(RefereeSite.GameScore score) {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_addGamePoint,
                referee.getName(),
                referee.getRefereeState());

        outMessage.setGamePoint(score);

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
     * @param score
     */
    @Override
    public void addTrialPoint(RefereeSite.TrialScore score) {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_addTrialPoint,
                referee.getName(),
                referee.getRefereeState());

        outMessage.setTrialScore(score);

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
    public void bothTeamsReady() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_bothTeamsReady,
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
     * @return
     */
    @Override
    public List<RefereeSite.GameScore> getGamePoints() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_getGamePoints,
                referee.getName(),
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.GAMEPOINTS) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        List gamePoints = inMessage.getGamePoints();

        return gamePoints;
    }

    /**
     *
     * @return
     */
    @Override
    public int getRemainingGames() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_getRemainingGames,
                referee.getName(),
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.REMAININGGAMES) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        return inMessage.getRemainingGames();
    }

    /**
     *
     * @return
     */
    @Override
    public int getRemainingTrials() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_getRemainingTrials,
                referee.getName(),
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.REMAININGTRIALS) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        return inMessage.getRemainingtTrials();
    }

    /**
     *
     * @return
     */
    @Override
    public List<RefereeSite.TrialScore> getTrialPoints() {
        Referee referee = (Referee) Thread.currentThread();
        //TODO: OU KEEPWINNINGTEAM

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_getTrialPoints,
                referee.getName(),
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.TRIALPOINTS) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        List<RefereeSite.TrialScore> trialPoints = inMessage.getTrialPoints();

        return trialPoints;

    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasMatchEnded() {
        // coach
        // contestant
        // contestantBench 

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_hasMatchEnded);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.BOOLEAN) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();

        return inMessage.getHasMatchEnded();
    }

    /**
     *
     */
    @Override
    public void informReferee() {
        Coach coach = (Coach) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_informReferee,
                coach.getName(),
                coach.getCoachState(),
                coach.getTeam(),
                coach.getStrategy());

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
    public void resetTrialPoints() {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_resetTrialPoints,
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
     * @param hasMatchEnded
     */
    @Override
    public void setHasMatchEnded(boolean hasMatchEnded) {
        Referee referee = (Referee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_setHasMatchEnded,
                referee.getName(),
                referee.getRefereeState());

        outMessage.setHasMatchEnded(hasMatchEnded);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }
}
