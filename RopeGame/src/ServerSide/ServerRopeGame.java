package ServerSide;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ServerRopeGame {

    /**
     * 
     */
    private static final int portNumb = 4000;

    public static void main(String[] args) {
        ServerInterface servInterface = null;
        
        if(args.length == 1) {
            if(args[0].equals("CB")) {
                servInterface = new ContestantsBenchInterface();
            } else if(args[0].equals("PG")) {
                servInterface = new PlaygroundInterface();
            } else if(args[0].equals("RS")) {
                servInterface = new RefereeSiteInterface();
            } else if(args[0].equals("GR")) {
                servInterface = new GeneralInformationRepositoryInterface();
            }
        }
        
        if(servInterface == null) {
            System.out.println("Server didn't start - bad argument - [CB | PG | RS | GR]");
            System.exit(1);
        }
        
        // initiates the APS
        ServiceProviderAgent sps;
        // start
        ServerCom scon, sconi;                               // canais de comunicação

        scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público

        System.out.println("Service started");
        System.out.println("Server is listining for connections at :" + portNumb);
        
        // wait for everyone connected?!
        // informationRepository.printHeader();

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            sps = new ServiceProviderAgent(sconi, servInterface);
            sps.start();
        }
    }

}
