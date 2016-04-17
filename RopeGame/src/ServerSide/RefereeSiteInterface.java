/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.*;

/**
 *
 * @author luminoso
 */
class RefereeSiteInterface {

    private final RefereeSite rf;

    public RefereeSiteInterface(RefereeSite rf) {
        this.rf = rf;
    }

    public Message processAndReply(Message inMessage) {
        Message outMessage = null;

        switch (inMessage.getType()) {

            case RS_addGamePoint:
                outMessage = new Message(OK);
            case RS_addTrialPoint:
                outMessage = new Message(OK);
            case RS_bothTeamsReady:
                outMessage = new Message(REFEREE_STATE_CHANGE);
            case RS_getGamePoints:
                outMessage = new Message(GAMEPOINTS);
            case RS_getRemainingGames:
                outMessage = new Message(REMAININGGAMES);
            case RS_getRemainingTrials:
                outMessage = new Message(REMAININGTRIALS);
            case RS_getTrialPoints:
                outMessage = new Message(TRIALPOINTS);
            case RS_hasMatchEnded:
                outMessage = new Message(BOOLEAN);
            case RS_informReferee:
                outMessage = new Message(OK);
            case RS_resetTrialPoints:
                outMessage = new Message(OK);
            case RS_setHasMatchEnded:
                outMessage = new Message(OK);
            default:

        }

        return outMessage;

    }

}
