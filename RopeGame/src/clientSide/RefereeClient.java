/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import interfaces.InterfaceContestantsBench;
import interfaces.InterfaceGeneralInformationRepository;
import interfaces.InterfacePlayground;
import interfaces.InterfaceRefereeSite;
import static java.lang.System.out;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.RegistryConfig;

/**
 *
 * @author luminoso
 */
public class RefereeClient {

    public static void main(String[] args) {

        // Initialise RMI configurations
        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        Registry registry = null;

        // Initialise Coach configurations from args
        String refereeName = "Referee";

        // Initialise RMI invocations
        InterfaceGeneralInformationRepository girInt = null;
        InterfacePlayground playgroundInt = null;
        InterfaceContestantsBench benchInt = null;
        InterfaceRefereeSite refsiteInt = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(CoachClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            girInt = (InterfaceGeneralInformationRepository) registry.lookup(RegistryConfig.REGISTRY_GIR_NAME);
            playgroundInt = (InterfacePlayground) registry.lookup(RegistryConfig.REGISTRY_PLAYGROUND_NAME);
            benchInt = (InterfaceContestantsBench) registry.lookup(RegistryConfig.REGISTRY_CONTESTANTS_BENCH_NAME);
            refsiteInt = (InterfaceRefereeSite) registry.lookup(RegistryConfig.REGISTRY_REFEREE_SITE_NAME);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(CoachClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        Referee referee = new Referee(refereeName, benchInt, playgroundInt,
                refsiteInt, girInt);

        out.println(refereeName + " started");

        referee.start();

        try {
            referee.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(CoachClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.println(refereeName + " finished his job.");
    }
}
