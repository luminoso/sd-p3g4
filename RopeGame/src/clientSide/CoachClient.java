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
import others.CoachStrategy;
import others.KeepWinningTeam;
import others.MostStrengthStrategy;
import registry.RegistryConfig;

/**
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class CoachClient {

    public static void main(String[] args) {

        // Initialise RMI configurations
        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        Registry registry = null;

        // Initialise Coach configurations from args
        int coachTeam = Integer.parseInt(args[0]);

        CoachStrategy cStrategy = (Integer.parseInt(args[1]) == 1 ? new KeepWinningTeam() : new MostStrengthStrategy());

        String coachName = "Coach-" + Integer.toString(coachTeam);

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

        Coach coach = new Coach(coachName, coachTeam, cStrategy,
                benchInt, refsiteInt, playgroundInt, girInt);

        out.println("Starting coach: " + coachName);

        coach.start();

        try {
            coach.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(CoachClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.println(coachName + " finished his job.");
        
        try {
            girInt.shutdown();
            playgroundInt.shutdown();
            benchInt.shutdown();
            refsiteInt.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(RefereeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
