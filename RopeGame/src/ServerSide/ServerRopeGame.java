/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import static java.lang.System.out;

/**
 *
 * @author luminoso
 */
public class ServerRopeGame {

    private static final int portNumb = 4000;

    public static void main(String[] args) {

        // initiates all areas for now
        ContestantsBench cb1 = ContestantsBench.getInstance(1);
        ContestantsBench cb2 = ContestantsBench.getInstance(2);
        Playground pg = Playground.getInstance();
        RefereeSite rf = RefereeSite.getInstance();
        GeneralInformationRepository informationRepository = GeneralInformationRepository.getInstance();

        // initiates the interfaces
        ContestantsBenchInterface cbi = new ContestantsBenchInterface(cb1, cb2);
        PlaygroundInterface pgi = new PlaygroundInterface(pg);
        RefereeSiteInterface rsi = new RefereeSiteInterface(rf);
        GeneralInformationRepositoryInterface giri = new GeneralInformationRepositoryInterface(informationRepository);

        // initiates the APS
        ServiceProviderAgent sps;
        // start
        ServerCom scon, sconi;                               // canais de comunicação

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público

        out.println("Service started");
        out.println("Server is listining for connections at :" + portNumb);
        informationRepository.printHeader();

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            sps = new ServiceProviderAgent(sconi, cbi, pgi, rsi, giri);
            sps.start();
        }
    }

}
