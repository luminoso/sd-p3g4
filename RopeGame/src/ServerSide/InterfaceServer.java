package ServerSide;

import Communication.Message;
import Communication.MessageException;

/**
 * Interface that defines the operations available passive classes that are
 * being served through messages
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
interface InterfaceServer {

    /**
     * Process the message and reply accordingly to the operation done in the
     * passive class
     *
     * @param inMessage message to be decoded and forwarded to the right method
     * @return message that will be answered
     * @throws message exception if message doesn't fit in the initialised
     * interface
     */
    Message processAndReply(Message inMessage) throws MessageException;

    /**
     * Checks if the interface has ended operations and is going to shutdown
     *
     * @return true if going to shutdown
     */
    boolean goingToShutdown();
}
