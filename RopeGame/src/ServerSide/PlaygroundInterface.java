package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.*;

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
    public Message processAndReply(Message inMessage) {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case PG_addContestant:
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
            case PG_checkTeamPlacement:
                outMessage = new Message(COACH_STATE_CHANGE);
            case PG_getContestant:
                outMessage = new Message(OK);
            case PG_getFlagPosition:
                outMessage = new Message(FLAGPOSITION);
            case PG_getLastFlagPosition:
                outMessage = new Message(LASTFLAGPOSITION);
            case PG_pullRope:
                outMessage = new Message(OK);
            case PG_resultAsserted:
                outMessage = new Message(OK);
            case PG_setFlagPosition:
                outMessage = new Message(OK);
            case PG_startPulling:
                outMessage = new Message(REFEREE_STATE_CHANGE);
            case PG_watchTrial:
                outMessage = new Message(COACH_STATE_CHANGE);
            default:
        }

        return outMessage;
    }
}
