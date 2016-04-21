package ServerSide;

import ClientSide.Referee;
import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class RefereeSiteInterface {

    /**
     *
     */
    private final RefereeSite rs;

    /**
     *
     * @param rf
     */
    public RefereeSiteInterface(RefereeSite rs) {
        this.rs = rs;
    }

    /**
     *
     * @param inMessage
     * @return
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case RS_addGamePoint: {
                rs.addGamePoint(inMessage.getGamePoint());
                outMessage = new Message(OK);
                break;
            }
            case RS_addTrialPoint: {
                rs.addTrialPoint(inMessage.getTrialPoint());
                outMessage = new Message(OK);
                break;
            }
            case RS_bothTeamsReady: {
                Referee referee = (Referee) Thread.currentThread();
                rs.bothTeamsReady();
                outMessage = new Message(REFEREE_STATE_CHANGE);
                outMessage.setRefereeState(referee.getRefereeState());
                break;
            }
            case RS_getGamePoints: {
                outMessage = new Message(GAMEPOINTS);
                outMessage.setGamePoints(rs.getGamePoints());
                break;
            }
            case RS_getRemainingGames: {
                outMessage = new Message(REMAININGGAMES);
                outMessage.setRemainingGames(rs.getRemainingGames());
                break;
            }
            case RS_getRemainingTrials: {
                outMessage = new Message(REMAININGTRIALS);
                outMessage.setRemainingTrials(rs.getRemainingTrials());
                break;
            }
            case RS_getTrialPoints: {
                outMessage = new Message(TRIALPOINTS);
                outMessage.setTrialPoints(rs.getTrialPoints());
                break;
            }
            case RS_hasMatchEnded: {
                outMessage = new Message(BOOLEAN);
                outMessage.setHasMatchEnded(rs.hasMatchEnded());
                break;
            }
            case RS_informReferee: {
                rs.informReferee();
                outMessage = new Message(OK);
                break;
            }
            case RS_resetTrialPoints: {
                rs.resetTrialPoints();
                outMessage = new Message(OK);
                break;
            }
            case RS_setHasMatchEnded: {
                rs.setHasMatchEnded(inMessage.getHasMatchEnded());
                outMessage = new Message(OK);
                break;
            }
            default:
                throw new MessageException("Method in RS not found", outMessage);

        }

        return outMessage;
    }
}