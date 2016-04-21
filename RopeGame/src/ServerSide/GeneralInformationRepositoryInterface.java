package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.OK;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class GeneralInformationRepositoryInterface {

    /**
     * 
     */
    private final GeneralInformationRepository informationRepository;

    /**
     * 
     * @param informationRepository 
     */
    public GeneralInformationRepositoryInterface(GeneralInformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    /**
     * 
     * @param inMessage
     * @return 
     */
    public Message processAndReply(Message inMessage) {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case GIR_addCoach:
                outMessage = new Message(OK);
            case GIR_addContestant:
                outMessage = new Message(OK);
            case GIR_addReferee:
                outMessage = new Message(OK);
            case GIR_close:
                outMessage = new Message(OK);
            case GIR_printGameHeader:
                outMessage = new Message(OK);
            case GIR_printGameResult:
                outMessage = new Message(OK);
            case GIR_printHeader:
                outMessage = new Message(OK);
            case GIR_printLegend:
                outMessage = new Message(OK);
            case GIR_printLineUpdate:
                outMessage = new Message(OK);
            case GIR_printMatchDraw:
                outMessage = new Message(OK);
            case GIR_printMatchWinner:
                outMessage = new Message(OK);
            case GIR_resetTeamPlacement:
                outMessage = new Message(OK);
            case GIR_setFlagPosition:
                outMessage = new Message(OK);
            case GIR_setGameNumber:
                outMessage = new Message(OK);
            case GIR_setTeamPlacement:
                outMessage = new Message(OK);
            case GIR_setTrialNumber:
                outMessage = new Message(OK);
            default:
        }

        return outMessage;
    }
}
