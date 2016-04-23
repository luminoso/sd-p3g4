package ServerSide;

import ClientSide.Coach;
import ClientSide.Contestant;
import ClientSide.Referee;
import Communication.Message;
import static Communication.Message.MessageType.OK;
import Communication.MessageException;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class GeneralInformationRepositoryInterface implements ServerInterface {

    /**
     *
     */
    private final GeneralInformationRepository ir;

    /**
     *
     * @param informationRepository
     */
    public GeneralInformationRepositoryInterface() {
        ir = GeneralInformationRepository.getInstance();
    }

    /**
     *
     * @param inMessage
     * @return
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case GIR_addCoach: {
                Coach coach = new Coach(inMessage.getName(), inMessage.getTeam(), inMessage.getStrategy());
                ir.addCoach(coach);
                outMessage = new Message(OK);
                break;
            }
            case GIR_addContestant: {
                Contestant contestant = new Contestant(inMessage.getName(),
                        inMessage.getTeam(),
                        inMessage.getId(),
                        inMessage.getStrength());
                ir.addContestant(contestant);
                outMessage = new Message(OK);
                break;
            }
            case GIR_addReferee: {
                Referee rf = new Referee(inMessage.getName());
                ir.addReferee(rf);
                outMessage = new Message(OK);
                break;
            }
            case GIR_close:
                ir.close();
                outMessage = new Message(OK);
                break;
            case GIR_printGameHeader:
                ir.printGameHeader();
                outMessage = new Message(OK);
                break;
            case GIR_printGameResult:
                ir.printGameResult(inMessage.getGameResult());
                outMessage = new Message(OK);
                break;
            case GIR_printHeader:
                ir.printHeader();
                outMessage = new Message(OK);
                break;
            case GIR_printLegend:
                ir.printLegend();
                outMessage = new Message(OK);
                break;
            case GIR_printLineUpdate:
                ir.printLineUpdate();
                outMessage = new Message(OK);
                break;
            case GIR_printMatchDraw:
                ir.printMatchDraw();
                outMessage = new Message(OK);
                break;
            case GIR_printMatchWinner:
                int[] numbers = inMessage.getNumbers();
                ir.printMatchWinner(numbers[0], numbers[1], numbers[2]);
                outMessage = new Message(OK);
                break;
            case GIR_resetTeamPlacement:
                ir.resetTeamPlacement();
                outMessage = new Message(OK);
                break;
            case GIR_setFlagPosition:
                ir.setFlagPosition(inMessage.getFlagPostion());
                outMessage = new Message(OK);
                break;
            case GIR_setGameNumber:
                ir.setGameNumber(inMessage.getGameNumber());
                outMessage = new Message(OK);
                break;
            case GIR_setTeamPlacement:
                ir.setTeamPlacement();
                outMessage = new Message(OK);
                break;
            case GIR_setTrialNumber:
                ir.setTrialNumber(inMessage.getTrialNumber());
                outMessage = new Message(OK);
                break;
            default:
                throw new MessageException("Method in IR Interface not found", outMessage);
        }

        return outMessage;
    }
}
