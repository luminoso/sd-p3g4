package ServerSide;

import Communication.Message;
import Communication.MessageException;

/**
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
interface InterfaceServer {

    Message processAndReply(Message inMessage) throws MessageException;
    
    boolean goingToShutdown();
}
