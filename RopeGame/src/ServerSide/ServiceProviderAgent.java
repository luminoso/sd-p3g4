/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Communication.Message;

/**
 *
 * @author luminoso
 */
public class ServiceProviderAgent extends Thread {

    private ServerCom sconi;
    private static int id = 0;
    private final ContestantsBenchInterface cbi;
    private final PlaygroundInterface pgi;
    private final RefereeSiteInterface rsi;
    private final GeneralInformationRepositoryInterface giri;

    ServiceProviderAgent(ServerCom sconi,
            ContestantsBenchInterface cbi,
            PlaygroundInterface pgi,
            RefereeSiteInterface rsi,
            GeneralInformationRepositoryInterface giri) {

        super(Integer.toString(id++));
        this.cbi = cbi;
        this.pgi = pgi;
        this.rsi = rsi;
        this.giri = giri;
    }

    @Override
    public void run() {
        Message inMessage, outMessage = null;

        inMessage = (Message) sconi.readObject();

        try {
            switch (inMessage.getMessageCategory()) {
                case CB:
                    outMessage = cbi.processAndReply(inMessage);
                case PG:
                    outMessage = pgi.processAndReply(inMessage);
                case GIR:
                    outMessage = giri.processAndReply(inMessage);
                case RS:
                    outMessage = rsi.processAndReply(inMessage);
                default:
            }
        } catch (Exception e) {
            //TODO deal with error;
        }

        sconi.writeObject(outMessage);
        sconi.close();

    }

}
