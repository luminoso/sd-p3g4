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
        ContestantsBench.getInstances();
        RefereeSite.getInstance();
        Playground.getInstance();
        GeneralInformationRepository informationRepository = GeneralInformationRepository.getInstance();

        ServerCom scon, sconi;                               // canais de comunicação
       // CustomerProxy custProxy;                             // thread procurador do cliente

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público

        //bShop = new BarberShop ();  
        //bShopInter = new BarberShopInterface(bShop); // activação do interface com o serviço
        
        
        out.println("O serviço foi estabelecido!");
        out.println("O servidor esta em escuta.");
        informationRepository.printHeader();

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
          //  custProxy = new CustomerProxy(sconi, bShopInter); // lançamento do procurador do cliente
           // custProxy.start();
        }
    }

}
