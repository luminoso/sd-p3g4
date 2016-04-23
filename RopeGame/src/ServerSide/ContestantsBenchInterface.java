package ServerSide;

import ClientSide.Contestant;
import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
class ContestantsBenchInterface implements ServerInterface {
    private static List<ContestantsBench> benchs = ContestantsBench.getInstances();
    
    /**
     *
     * @param inMessage
     * @return
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        switch (inMessage.getType()) {
            case CB_addContestant: {
                InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
                benchs.get(contestant.getContestantTeam()-1).addContestant();
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
                outMessage.setContestantState(contestant.getContestantState());
                break;
            }
            case CB_getBench: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                Set<InterfaceContestant> bench = benchs.get(coach.getCoachTeam()-1).getBench();
                outMessage = new Message(BENCH);
                outMessage.setSet(bench);
                break;
            }
            case CB_getContestant: {
                InterfaceContestant contestant = (Contestant) Thread.currentThread();
                benchs.get(contestant.getContestantTeam()-1).getContestant();
                outMessage = new Message(OK);
                break;
            }
            case CB_getSelectedContestants: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                Set<Integer> selectedContestants = benchs.get(coach.getCoachTeam()-1).getSelectedContestants();
                outMessage = new Message(SELECTED_CONTESTANTS);
                outMessage.setSelectedContestants(selectedContestants);
                break;
            }
            case CB_pickYourTeam: {
                benchs.get(inMessage.getTeam()-1).pickYourTeam();
                outMessage = new Message(OK);
                break;
            }
            case CB_setSelectedContestants: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                benchs.get(coach.getCoachTeam()-1).setSelectedContestants(inMessage.getSelectedContestants());
                outMessage = new Message(OK);
                break;
            }
            case CB_waitForNextTrial: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                benchs.get(coach.getCoachTeam()-1).waitForNextTrial();
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
