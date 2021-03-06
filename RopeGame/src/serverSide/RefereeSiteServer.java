package serverSide;

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
 * @author luminoso
 */
public class RefereeSiteServer {
    
    public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;

        /* localização por nome do objecto remoto no serviço de registos RMI */
        InterfaceGeneralInformationRepository girInterface = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            girInterface = (InterfaceGeneralInformationRepository) registry.lookup(RegistryConfig.REGISTRY_GIR_NAME);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do General Information Repository: " + e.getMessage() + "!");
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O General Information Repository não está registado: " + e.getMessage() + "!");
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // Instanciçãoo do RefereeSite
        RefereeSite refereesite = RefereeSite.getInstance(girInterface);
        InterfaceRefereeSite refereesiteInterface = null;

        try {
            refereesiteInterface = (InterfaceRefereeSite) UnicastRemoteObject.exportObject(refereesite, RegistryConfig.REGISTRY_REFEREE_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o RefereeSite: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("O stub para o RefereSite foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_REFEREE_SITE_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, refereesiteInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do RefereeSite: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O RefereeSite já está bounded: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O RefereeSite foi registado!");
    }
    
}
