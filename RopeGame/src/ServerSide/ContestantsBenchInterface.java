package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.*;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class ContestantsBenchInterface {

    /**
     * 
     */
    private final ContestantsBench cb1;
    
    /**
     * 
     */
    private final ContestantsBench cb2;

    /**
     * 
     * @param cb1
     * @param cb2 
     */
    public ContestantsBenchInterface(ContestantsBench cb1, ContestantsBench cb2) {

        this.cb1 = cb1;
        this.cb2 = cb2;

    }

    /**
     * 
     * @param inMessage
     * @return 
     */
    public Message processAndReply(Message inMessage) {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case CB_addContestant:
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
            case CB_getBench:
                outMessage = new Message(BENCH);
            case CB_getContestant:
                outMessage = new Message(OK);
            case CB_getSelectedContestants:
                outMessage = new Message(SELECTEDCONTESTANTS);
            case CB_pickYourTeam:
                outMessage = new Message(OK);
            case CB_setSelectedContestants:
                outMessage = new Message(OK);
            case CB_waitForNextTrial:
                outMessage = new Message(COACH_STATE_CHANGE);
            default:
        }

        return outMessage;
    }
}
