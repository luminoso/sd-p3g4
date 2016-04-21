package ServerSide;

import ClientSide.Coach;
import ClientSide.Contestant;
import ClientSide.Referee;
import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class PlaygroundInterface {

    /**
     *
     */
    private final Playground pg;

    /**
     *
     * @param pg
     */
    public PlaygroundInterface(Playground pg) {
        this.pg = pg;
    }

    /**
     *
     * @param inMessage
     * @return
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case PG_addContestant: {
                Contestant contestant = (Contestant) Thread.currentThread();
                pg.addContestant();
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
                outMessage.setContestantState(contestant.getContestantState());
                break;
            }
            case PG_checkTeamPlacement: {
                Coach coach = (Coach) Thread.currentThread();
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
                outMessage = new Message(FLAGPOSITION);
                outMessage.setFlagPosition(pg.getFlagPosition());
                break;
            }
            case PG_getLastFlagPosition: {
                outMessage = new Message(LASTFLAGPOSITION);
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
                Referee referee = (Referee) Thread.currentThread();
                pg.startPulling();
                outMessage = new Message(REFEREE_STATE_CHANGE);
                outMessage.setRefereeState(referee.getRefereeState());
                break;
            }
            case PG_watchTrial: {
                Coach coach = (Coach) Thread.currentThread();
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
