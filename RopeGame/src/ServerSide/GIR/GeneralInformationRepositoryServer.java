package ServerSide.GIR;

import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.Register;
import Registry.RegistryConfig;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author luminoso
 */
public class GeneralInformationRepositoryServer {
    
        public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = RegistryConfig.RMI_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_PORTNUMBER;


        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // Instanciçãoo do RefereeSite
        GeneralInformationRepository gir = new GeneralInformationRepository();
        InterfaceGeneralInformationRepository girInterface = null;

        try {
            girInterface = (InterfaceGeneralInformationRepository) UnicastRemoteObject.exportObject(gir, RegistryConfig.REGISTRY_GIR_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o GeneralInformationRepository: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("O stub para o GeneralInformationRepository foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_PLAYGROUND_NAME;
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
            reg.bind(nameEntryObject, girInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do GeneralInformationRepository: " + e.getMessage());
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O GeneralInformationRepository já está bound: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("O GeneralInformationRepository foi registado!");
    }
    
}
