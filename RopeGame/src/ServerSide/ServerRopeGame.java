package ServerSide;

import RopeGame.ServerConfigs;

/**
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class ServerRopeGame {

    public static void main(String[] args) {
        ServerCom scon = null, sconi;
        ServiceProviderAgent sps;
        InterfaceServer servInterface = null;

        if (args.length == 1) {
            switch (args[0]) {
                case "CB":
                    scon = new ServerCom(ServerConfigs.CONTESTANTS_BENCH_PORT);
                    servInterface = new ContestantsBenchInterface();
                    break;
                case "PG":
                    scon = new ServerCom(ServerConfigs.PLAYGROUND_PORT);
                    servInterface = new PlaygroundInterface();
                    break;
                case "RS":
                    scon = new ServerCom(ServerConfigs.REFEREE_SITE_PORT);
                    servInterface = new RefereeSiteInterface();
                    break;
                case "GR":
                    scon = new ServerCom(ServerConfigs.GENERAL_INFORMATION_REPOSITORY_PORT);
                    servInterface = new GeneralInformationRepositoryInterface();
                    break;
                default:
                    break;
            }
        }

        if (servInterface == null || scon == null) {
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
