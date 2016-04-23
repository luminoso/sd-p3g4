package ServerSide;

import Communication.Message;
import Communication.MessageException;

/**
 *
 * @author ed1000
 */
interface ServerInterface {
    public Message processAndReply(Message inMessage) throws MessageException;
}
