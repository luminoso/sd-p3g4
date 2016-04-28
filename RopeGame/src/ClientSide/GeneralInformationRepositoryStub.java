package ClientSide;

import Communication.Message;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfaceGeneralInformationRepository;
import Others.InterfaceReferee;
import RopeGame.ServerConfigs;
import ServerSide.RefereeSite;
import static java.lang.System.out;

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
    public void updateCoach() {
        InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
        
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        // Problably defining a variable coach was better?
        // handled in GeneralRepositoryInterface in the same way
        outMessage = new Message(Message.MessageType.GIR_UPDATE_COACH,
                coach.getCoachState(),
                coach.getCoachTeam());

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    /**
     *
     * @param contestant
     */
    @Override
    public void updateContestant() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
        
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_UPDATE_CONTESTANT,
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

        con.close();
    }

    /**
     * 
     * @param team
     * @param id
     * @param strength 
     */
    public void updateContestantStrength(int team, int id, int strength) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_UPDATE_CONTESTANT_STRENGTH,
                null,
                team,
                id,
                strength);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }
    
    /**
     *
     * @param referee
     */
    @Override
    public void updateReferee() {
        InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();
        
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_UPDATE_REFEREE,
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    /**
     *
     */
    @Override
    public void resetTeamPlacement() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
        
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_RESET_TEAM_PLACEMENT,
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
            out.println("Returned message with wrong type");
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
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }

    /**
     *
     */
    @Override
    public void setTeamPlacement() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_SET_TEAM_PLACEMENT,
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
            out.println("Returned message with wrong type");
            System.exit(1);
        }

        con.close();
    }
}
