package ServerSide.Playground;

import Interfaces.InterfaceGeneralInformationRepository;
import Interfaces.InterfacePlayground;
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
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class PlaygroundServer {

    public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = RegistryConfig.RMI_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_PORTNUMBER;

        /* localização por nome do objecto remoto no serviço de registos RMI */
        InterfaceGeneralInformationRepository girInterface = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            girInterface = (InterfaceGeneralInformationRepository) registry.lookup(RegistryConfig.REGISTRY_GIR_NAME);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do General Information Repository: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O General Information Repository não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        Playground playground = null;
        InterfacePlayground playgroundInterface = null;

        playground = new Playground(girInterface);
        try {
            playgroundInterface = (InterfacePlayground) UnicastRemoteObject.exportObject(playground, RegistryConfig.REGISTRY_PLAYGROUND_PORT);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o Playground: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o Playground foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.RMI_NAME;
        String nameEntryObject = RegistryConfig.REGISTRY_PLAYGROUND_NAME;
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
            reg.bind(nameEntryObject, playgroundInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do Playground: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O Playground já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("O Playground foi registado!");
    }
}
