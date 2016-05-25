package ServerSide;

import Communication.Message;
import static Communication.Message.MessageType.*;
import Communication.MessageException;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.Tuple;
import java.util.List;
import java.util.Set;

/**
 * Interface server implementation for contestants bench access
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
class ContestantsBenchInterface implements InterfaceServer {

    private static final List<ContestantsBench> benchs = ContestantsBench.getInstances();

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage;

        switch (inMessage.getType()) {
            case CB_ADD_CONTESTANT: {
                InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
                benchs.get(contestant.getContestantTeam() - 1).addContestant();
                outMessage = new Message(CONTESTANT_STATE_CHANGE);
                outMessage.setContestantState(contestant.getContestantState());
                outMessage.setStrength(contestant.getContestantStrength());
                break;
            }
            case CB_GET_BENCH: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                Set<Tuple<Integer, Integer>> bench = benchs.get(coach.getCoachTeam() - 1).getBench();
                outMessage = new Message(BENCH);
                outMessage.setSet(bench);
                break;
            }
            case CB_GET_CONTESTANT: {
                InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();
                benchs.get(contestant.getContestantTeam() - 1).getContestant();
                outMessage = new Message(OK);
                break;
            }
            case CB_GET_SELECTED_CONTESTANTS: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                Set<Integer> selectedContestants = benchs.get(coach.getCoachTeam() - 1).getSelectedContestants();
                outMessage = new Message(SELECTED_CONTESTANTS);
                outMessage.setSelectedContestants(selectedContestants);
                break;
            }
            case CB_PICK_YOUR_TEAM: {
                benchs.get(inMessage.getTeam() - 1).pickYourTeam();
                outMessage = new Message(OK);
                break;
            }
            case CB_SET_SELECTED_CONTESTANTS: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                benchs.get(coach.getCoachTeam() - 1).setSelectedContestants(inMessage.getSelectedContestants());
                outMessage = new Message(OK);
                break;
            }
            case CB_WAIT_FOR_NEXT_TRIAL: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                benchs.get(coach.getCoachTeam() - 1).waitForNextTrial();
                outMessage = new Message(COACH_STATE_CHANGE);
                outMessage.setCoachState(coach.getCoachState());
                break;
            }
            case CB_UPDATE_CONTESTANT_STRENGTH: {
                InterfaceCoach coach = (InterfaceCoach) Thread.currentThread();
                int numbers[] = inMessage.getNumbers(); // {id, delta}
                benchs.get(coach.getCoachTeam() - 1).updateContestantStrength(numbers[0], numbers[1]);
                outMessage = new Message(OK);
                break;
            }
            case INTERRUPT: {
                benchs.get(0).interrupt();
                benchs.get(1).interrupt();
                outMessage = new Message(OK);
                break;
            }
            case WAIT_FOR_EVERYONE_TO_START:
                benchs.get(0).waitForEveryoneToStart();
                benchs.get(1).waitForEveryoneToStart();
                outMessage = new Message(OK);
                break;
            default:
                throw new MessageException("Method in PG not found", inMessage);
        }

        return outMessage;
    }

    @Override
    public boolean goingToShutdown() {
        return benchs.get(0).shutdown();
    }
}
