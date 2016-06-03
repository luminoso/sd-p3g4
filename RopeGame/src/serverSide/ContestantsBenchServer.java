package serverSide;

import interfaces.InterfaceContestantsBench;
import interfaces.InterfaceGeneralInformationRepository;
import interfaces.InterfaceRefereeSite;
import interfaces.Register;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import registry.RegistryConfig;

/**
 *
 * @author ed1000
 */
public class ContestantsBenchServer {
    public static void main(String[] args) throws AlreadyBoundException {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        
        /* localização por nome do objecto remoto no serviço de registos RMI */
        InterfaceGeneralInformationRepository girInterface = null;
        InterfaceRefereeSite refSiteInterface = null;

        try { 
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            girInterface = (InterfaceGeneralInformationRepository) registry.lookup (RegistryConfig.REGISTRY_GIR_NAME);
        } catch (RemoteException e) { 
            System.out.println("Excepção na localização do General Information Repository: " + e.getMessage () + "!");
            e.printStackTrace();
            System.exit (1);
        } catch (NotBoundException e) { 
            System.out.println("O General Information Repository não está registado: " + e.getMessage () + "!");
            e.printStackTrace();
            System.exit(1);
        }
        
        try { 
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            refSiteInterface = (InterfaceRefereeSite) registry.lookup(RegistryConfig.REGISTRY_REFEREE_SITE_NAME);
        } catch (RemoteException e) { 
            System.out.println("Excepção na localização do Referee Site: " + e.getMessage () + "!");
            e.printStackTrace();
            System.exit (1);
        } catch (NotBoundException e) { 
            System.out.println("O Referee Site não está registado: " + e.getMessage () + "!");
            e.printStackTrace();
            System.exit(1);
        }
        
        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        ContestantsBench bench = null;
        InterfaceContestantsBench benchInterface = null;
        
        bench = new ContestantsBench(refSiteInterface, girInterface);
        
        try {
            benchInterface = (InterfaceContestantsBench) UnicastRemoteObject.exportObject(bench, RegistryConfig.REGISTRY_CONTESTANTS_BENCH_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para os Contestant Benchs: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para os Contestant Benchs foram gerados!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_CONTESTANTS_BENCH_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, benchInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo dos Contestant Benchs: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Os Contestant Benchs já estão registados: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        System.out.println("Os Contestant Benchs foram registados!");
    }
}
