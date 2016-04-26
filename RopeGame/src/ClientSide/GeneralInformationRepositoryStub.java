package ClientSide;

import ClientSide.Coach.CoachState;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee.RefereeState;
import Communication.Message;
import Others.InterfaceContestant;
import Others.InterfaceGeneralInformationRepository;
import RopeGame.ServerConfigs;
import ServerSide.RefereeSite;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class GeneralInformationRepositoryStub implements InterfaceGeneralInformationRepository {

    /**
     *
     */
    private static GeneralInformationRepositoryStub instance;

    /**
     *
     * @return
     */
    public static synchronized GeneralInformationRepositoryStub getInstance() {
        if (instance == null) {
            instance = new GeneralInformationRepositoryStub();
        }

        return instance;
    }

    /**
     *
     * @return
     */
    private ClientCom initiateConnection() {
        ClientCom con = new ClientCom(ServerConfigs.GENERAL_INFORMATION_REPOSITORY_ADDRESS,
                ServerConfigs.GENERAL_INFORMATION_REPOSITORY_PORT);

        if (!con.open()) {
            // TODO: handle later
            //return false; // server doesn't accept more connections
        }

        return con;
    }

    /**
     *
     * @param coach
     */
    @Override
    public void updateCoach(int team, CoachState state) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        // Problably defining a variable coach was better?
        // handled in GeneralRepositoryInterface in the same way
        outMessage = new Message(Message.MessageType.GIR_UPDATE_COACH,
                state,
                team);

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
     * @param contestant
     */
    @Override
    public void updateContestant(int team, int id, ContestantState state, int strength) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_UPDATE_CONTESTANT,
                state,
                team,
                id,
                strength);

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
     * @param referee
     */
    @Override
    public void updateReferee(RefereeState state) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_UPDATE_REFEREE,
                state);

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
    public void close() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_CLOSE);

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
    public void printGameHeader() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_GAME_HEADER);

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
    public void printGameResult(RefereeSite.GameScore score) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_GAME_RESULT);

        outMessage.setGameResult(score);

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
    public void printHeader() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_HEADER);

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
    public void printLegend() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_LEGEND);

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
    public void printLineUpdate() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_LINE_UPDATE);

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
    public void printMatchDraw() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_MATCH_DRAW);

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
     * @param team
     * @param score1
     * @param score2
     */
    @Override
    public void printMatchWinner(int team, int score1, int score2) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_PRINT_MATCH_WINNER);

        int numbers[] = {team, score1, score2};

        outMessage.setNumbers(numbers);

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
    public void resetTeamPlacement(int team, int id) {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
        
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_RESET_TEAM_PLACEMENT,
                                    contestant.getContestantState(),
                                    team,
                                    id,
                                    contestant.getContestantStrength());

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
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_SET_FLAG_POSITION);

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
     * @param gameNumber
     */
    @Override
    public void setGameNumber(int gameNumber) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_SET_GAME_NUMBER);

        outMessage.setGameNumber(gameNumber);

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
    public void setTeamPlacement(int team, int id) {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_SET_TEAM_PLACEMENT,
                contestant.getContestantState(),
                team,
                id,
                contestant.getContestantStrength());

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
     * @param trialNumber
     */
    @Override
    public void setTrialNumber(int trialNumber) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_SET_TRIAL_NUMBER);

        outMessage.setTrialNumber(trialNumber);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }
}
