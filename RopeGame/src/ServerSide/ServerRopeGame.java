package ServerSide;

import RopeGame.ServerConfigs;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class ServerRopeGame {
    public static void main(String[] args) {
        ServerCom scon = null, sconi;
        ServiceProviderAgent sps;
        ServerInterface servInterface = null;
        
        if(args.length == 1) {
            if(args[0].equals("CB")) {
                scon = new ServerCom(ServerConfigs.CONTESTANTS_BENCH_PORT);
                servInterface = new ContestantsBenchInterface();
            } else if(args[0].equals("PG")) {
                scon = new ServerCom(ServerConfigs.PLAYGROUND_PORT);
                servInterface = new PlaygroundInterface();
            } else if(args[0].equals("RS")) {
                scon = new ServerCom(ServerConfigs.REFEREE_SITE_PORT);
                servInterface = new RefereeSiteInterface();
            } else if(args[0].equals("GR")) {
                scon = new ServerCom(ServerConfigs.GENERAL_INFORMATION_REPOSITORY_PORT);
                servInterface = new GeneralInformationRepositoryInterface();
            }
        }
        
        if(servInterface == null || scon == null) {
            System.out.println("Server didn't start - bad argument - [CB | PG | RS | GR]");
            System.exit(1);
        }
        
        scon.start();

        while (true) {
            sconi = scon.accept();
            sps = new ServiceProviderAgent(sconi, servInterface);
            sps.start();
        }
    }

}
