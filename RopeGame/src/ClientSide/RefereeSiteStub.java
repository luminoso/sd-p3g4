package ClientSide;

import Communication.Message;
import Others.InterfaceCoach;
import Others.InterfaceReferee;
import Others.InterfaceRefereeSite;
import RopeGame.ServerConfigs;
import ServerSide.RefereeSite;
import static java.lang.System.out;
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
    public RefereeSiteStub() {
    }

    /**
     * Initiates the connection to the Server according to the ServerConfigs
     *
     * @return ClientCom with the opened connection
     */
    private ClientCom initiateConnection() {
        ClientCom con = new ClientCom(ServerConfigs.REFEREE_SITE_ADDRESS,
                ServerConfigs.REFEREE_SITE_PORT);

        if (!con.open()) {
            out.println("Couldn't initiate connection to "
                    + ServerConfigs.REFEREE_SITE_ADDRESS + ":"
                    + ServerConfigs.REFEREE_SITE_PORT);
        }

        return con;
    }

    /**
     * The method allows to set the game points for both team.
     *
     * @param score Game points of both teams.
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
     * The method allows to set the trial points for both team.
     *
     * @param score Trial points of both teams.
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
     * Synchronization point where the Referee waits for both teams to be ready
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
     * The method returns the game points in the form of an array.
     *
     * @return Game points.
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
     * Gets how many games are remaining to play
     *
     * @return number of remaining games left
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
     * Gets how many trials are remaining to play
     *
     * @return number of remaining trials left
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
     * The method returns the trial points in the form of an array.
     *
     * @return Trial points.
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
     * Checks if the match has ended
     *
     * @return True if no more matches to play. False if otherwise.
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
     * Synchronisation point where the Coaches inform the Referee that they're
     * ready
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
     * Resets the trial points
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
     * Changes the information at RefereeSite if the match as ended
     *
     * @param hasMatchEnded true if match ended
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
