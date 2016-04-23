package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfaceReferee;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class PlaygroundInterface implements ServerInterface {

    /**
     *
     */
    private final Playground pg;

    /**
     *
     * @param pg
     */
    public PlaygroundInterface() {
        this.pg = Playground.getInstance();
    }

    /**
     *
     * @param inMessage
     * @return
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case PG_addContestant: {
                InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
                pg.addContestant();
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
                outMessage.setContestantState(contestant.getContestantState());
                break;
            }
            case PG_checkTeamPlacement: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                pg.checkTeamPlacement();
                outMessage = new Message(COACH_STATE_CHANGE);
                outMessage.setContestantState(coach.getCoachState());
                break;
            }
            case PG_getContestant: {
                pg.getContestant();
                outMessage = new Message(OK);
                break;
            }
            case PG_getFlagPosition: {
                outMessage = new Message(FLAG_POSITION);
                outMessage.setFlagPosition(pg.getFlagPosition());
                break;
            }
            case PG_getLastFlagPosition: {
                outMessage = new Message(LAST_FLAG_POSITION);
                outMessage.setLastFlagPosition(pg.getLastFlagPosition());
                break;
            }
            case PG_pullRope: {
                pg.pullRope();
                outMessage = new Message(OK);
                break;
            }
            case PG_resultAsserted: {
                pg.resultAsserted();
                outMessage = new Message(OK);
                break;
            }
            case PG_setFlagPosition: {
                pg.setFlagPosition(inMessage.getFlagPostion());
                outMessage = new Message(OK);
                break;
            }
            case PG_startPulling: {
                InterfaceReferee referee = (InterfaceReferee) Thread.currentThread();
                pg.startPulling();
                outMessage = new Message(REFEREE_STATE_CHANGE);
                outMessage.setRefereeState(referee.getRefereeState());
                break;
            }
            case PG_watchTrial: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                pg.watchTrial();
                outMessage = new Message(COACH_STATE_CHANGE);
                outMessage.setCoachState(coach.getCoachState());
                break;
            }
            default:
                throw new MessageException("Method in PG not found", outMessage);
        }

        return outMessage;
    }
}
