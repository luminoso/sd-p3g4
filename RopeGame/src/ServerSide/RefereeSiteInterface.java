package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;
import Others.InterfaceReferee;

/**
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
class RefereeSiteInterface implements InterfaceServer {

    /**
     *
     */
    private final RefereeSite rs;

    /**
     *
     * @param rf
     */
    public RefereeSiteInterface() {
        this.rs = RefereeSite.getInstance();
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case RS_ADD_GAME_POINT: {
                rs.addGamePoint(inMessage.getGamePoint());
                outMessage = new Message(OK);
                break;
            }
            case RS_ADD_TRIAL_POINT: {
                rs.addTrialPoint(inMessage.getTrialPoint());
                outMessage = new Message(OK);
                break;
            }
            case RS_BOTH_TEAMS_READY: {
                InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();
                rs.bothTeamsReady();
                outMessage = new Message(REFEREE_STATE_CHANGE);
                outMessage.setRefereeState(referee.getRefereeState());
                break;
            }
            case RS_GET_GAME_POINTS: {
                outMessage = new Message(GAME_POINTS);
                outMessage.setGamePoints(rs.getGamePoints());
                break;
            }
            case RS_GET_REMAINING_GAMES: {
                outMessage = new Message(REMAINING_GAMES);
                outMessage.setRemainingGames(rs.getRemainingGames());
                break;
            }
            case RS_GET_REMAINING_TRIALS: {
                outMessage = new Message(REMAINING_TRIALS);
                outMessage.setRemainingTrials(rs.getRemainingTrials());
                break;
            }
            case RS_GET_TRIAL_POINTS: {
                outMessage = new Message(TRIAL_POINTS);
                outMessage.setTrialPoints(rs.getTrialPoints());
                break;
            }
            case RS_HAS_MATCH_ENDED: {
                outMessage = new Message(BOOLEAN);
                outMessage.setHasMatchEnded(rs.hasMatchEnded());
                break;
            }
            case RS_INFORM_REFEREE: {
                rs.informReferee();
                outMessage = new Message(OK);
                break;
            }
            case RS_RESET_TRIAL_POINTS: {
                rs.resetTrialPoints();
                outMessage = new Message(OK);
                break;
            }
            case RS_SET_HAS_MATCH_ENDED: {
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
