package ClientSide;

import Communication.Message;
import Others.InterfaceCoach;
import Others.InterfaceReferee;
import Others.InterfaceRefereeSite;
import ServerSide.RefereeSite;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RefereeSiteStub implements InterfaceRefereeSite {
    /**
     * Private constructor to be used in singleton.
     */
    public RefereeSiteStub() {}

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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_ADD_GAME_POINT,
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_ADD_TRIAL_POINT,
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_BOTH_TEAMS_READY,
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
     * @return
     */
    @Override
    public List<RefereeSite.GameScore> getGamePoints() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_GET_GAME_POINTS,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.GAME_POINTS) {
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_GET_REMAINING_GAMES,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.REMAINING_GAMES) {
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_GET_REMAINING_TRIALS,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.REMAINING_TRIALS) {
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();
        //TODO: OU KEEPWINNINGTEAM

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_GET_TRIAL_POINTS,
                referee.getRefereeState());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.TRIAL_POINTS) {
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
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_HAS_MATCH_ENDED);

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
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_INFORM_REFEREE,
                coach.getCoachState(),
                coach.getCoachTeam(),
                coach.getCoachStrategy());

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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_RESET_TRIAL_POINTS,
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
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.RS_SET_HAS_MATCH_ENDED,
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
