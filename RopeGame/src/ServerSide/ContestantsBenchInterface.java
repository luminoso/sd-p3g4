package ServerSide;

import ClientSide.Coach;
import ClientSide.Contestant;
import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;
import java.util.Set;

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

    private ContestantsBench getBench(int team) {
        return team == 1 ? cb1 : cb2;
    }

    /**
     *
     * @param inMessage
     * @return
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case CB_addContestant: {
                Contestant contestant = (Contestant) Thread.currentThread();
                getBench(contestant.getTeam()).addContestant();
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
                outMessage.setContestantState(contestant.getContestantState());
                break;
            }
            case CB_getBench: {
                Coach coach = (Coach) Thread.currentThread();
                Set<Contestant> bench = getBench(coach.getTeam()).getBench();
                outMessage = new Message(BENCH);
                outMessage.setSet(bench);
                break;
            }
            case CB_getContestant: {
                Contestant contestant = (Contestant) Thread.currentThread();
                getBench(contestant.getTeam()).getContestant();
                outMessage = new Message(OK);
                break;
            }
            case CB_getSelectedContestants: {
                Coach coach = (Coach) Thread.currentThread();
                Set selectedContestants = getBench(coach.getTeam()).getSelectedContestants();
                outMessage = new Message(SELECTEDCONTESTANTS);
                outMessage.setSelectedContestants(selectedContestants);
                break;
            }
            case CB_pickYourTeam: {
                getBench(inMessage.getTeam()).pickYourTeam();
                outMessage = new Message(OK);
                break;
            }
            case CB_setSelectedContestants: {
                Coach coach = (Coach) Thread.currentThread();
                getBench(coach.getTeam()).setSelectedContestants(inMessage.getSelectedContestants());
                outMessage = new Message(OK);
                break;
            }
            case CB_waitForNextTrial: {
                Coach coach = (Coach) Thread.currentThread();
                getBench(coach.getTeam()).waitForNextTrial();
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
