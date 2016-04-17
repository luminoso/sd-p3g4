/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import Communication.Message;
import Others.GIR;
import Others.InterfaceGeneralInformationRepository;
import ServerSide.RefereeSite;

/**
 *
 * @author luminoso
 */
public class GeneralInformationRepositoryStub extends GIR implements InterfaceGeneralInformationRepository {

    private static GeneralInformationRepositoryStub instance;

    public static synchronized GeneralInformationRepositoryStub getInstance() {
        if (instance == null) {
            instance = new GeneralInformationRepositoryStub();
        }

        return instance;
    }

    private ClientCom initiateConnection() {
        ClientCom con = new ClientCom(ClientRopeGame.getServerHostName(),
                ClientRopeGame.getServerPortNumb());

        if (!con.open()) {
            // TODO: handle later
            //return false; // server doesn't accept more connections
        }

        return con;
    }

    @Override
    public void addCoach(Coach coach) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_addCoach,
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

    @Override
    public void addContestant(Contestant contestant) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_addContestant,
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

        con.close();
    }

    @Override
    public void addReferee(Referee referee) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_addReferee,
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

    @Override
    public void close() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_close);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printGameHeader() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_printGameHeader);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printGameResult(RefereeSite.GameScore score) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_printGameResult);

        outMessage.setGameScore(score);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printHeader() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_printHeader);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printLegend() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_printLegend);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printLineUpdate() {
        Thread thread = Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        if (thread.getClass() == Contestant.class) {
            Contestant contestant = (Contestant) thread;
            outMessage = new Message(Message.MessageType.GIR_printLegend,
                    contestant.getContestantState(),
                    contestant.getTeam(),
                    contestant.getContestatId(),
                    contestant.getStrength());
        } else if (thread.getClass() == Coach.class) {
            Coach coach = (Coach) thread;
            outMessage = new Message(Message.MessageType.GIR_printLegend,
                    coach.getCoachState(),
                    coach.getTeam(),
                    coach.getStrategy());
        } else {
            Referee referee = (Referee) thread;
            outMessage = new Message(Message.MessageType.GIR_printLegend,
                    referee.getRefereeState(), referee.getName());
        }

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printMatchDraw() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_printMatchDraw);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void printMatchWinner(int team, int score1, int score2) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_printMatchWinner);

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

    @Override
    public void resetTeamPlacement() {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_resetTeamPlacement);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void setFlagPosition(int flagPosition) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_setFlagPosition);

        outMessage.setFlagPosition(flagPosition);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void setGameNumber(int gameNumber) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_setGameNumber);

        outMessage.setGameNumber(gameNumber);

        con.writeObject(outMessage);

        inMessage = (Message) con.readObject();

        if (inMessage.getType() != Message.MessageType.OK) {
            // TODO: handle error
            System.exit(1);
        }

        con.close();
    }

    @Override
    public void setTeamPlacement() {
        Contestant contestant = (Contestant) Thread.currentThread();

        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_setTeamPlacement,
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

        con.close();
    }

    @Override
    public void setTrialNumber(int trialNumber) {
        ClientCom con = initiateConnection();

        Message inMessage, outMessage;

        outMessage = new Message(Message.MessageType.GIR_setTrialNumber);

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
