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
            case GIR_UPDATE_COACH: {
                ir.updateCoach(inMessage.getTeam(), inMessage.getCoachState());
                outMessage = new Message(OK);
                break;
            }
            case GIR_UPDATE_CONTESTANT: {
                ir.updateContestant(inMessage.getTeam(), inMessage.getContestantId(), inMessage.getContestantState(), inMessage.getStrength());
                outMessage = new Message(OK);
                break;
            }
            case GIR_UPDATE_REFEREE: {
                ir.updateReferee(inMessage.getRefereeState());
                outMessage = new Message(OK);
                break;
            }
            case GIR_CLOSE:
                ir.close();
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_GAME_HEADER:
                ir.printGameHeader();
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_GAME_RESULT:
                ir.printGameResult(inMessage.getGameResult());
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_HEADER:
                ir.printHeader();
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_LEGEND:
                ir.printLegend();
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_LINE_UPDATE:
                ir.printLineUpdate();
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_MATCH_DRAW:
                ir.printMatchDraw();
                outMessage = new Message(OK);
                break;
            case GIR_PRINT_MATCH_WINNER:
                int[] numbers = inMessage.getNumbers();
                ir.printMatchWinner(numbers[0], numbers[1], numbers[2]);
                outMessage = new Message(OK);
                break;
            case GIR_RESET_TEAM_PLACEMENT:
                ir.resetTeamPlacement(inMessage.getTeam(), inMessage.getContestantId());
                outMessage = new Message(OK);
                break;
            case GIR_SET_FLAG_POSITION:
                ir.setFlagPosition(inMessage.getFlagPostion());
                outMessage = new Message(OK);
                break;
            case GIR_SET_GAME_NUMBER:
                ir.setGameNumber(inMessage.getGameNumber());
                outMessage = new Message(OK);
                break;
            case GIR_SET_TEAM_PLACEMENT:
                ir.setTeamPlacement(inMessage.getTeam(), inMessage.getContestantId());
                outMessage = new Message(OK);
                break;
            case GIR_SET_TRIAL_NUMBER:
                ir.setTrialNumber(inMessage.getTrialNumber());
                outMessage = new Message(OK);
                break;
            default:
                throw new MessageException("Method in IR Interface not found", outMessage);
        }

        return outMessage;
    }
}
