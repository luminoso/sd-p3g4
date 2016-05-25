package RopeGame;

/**
 * This class starts the Rope Game. It instantiates all the active and passive
 * entities.
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {

    /**
     * Initializes the Client of RopeGame Client/Server implementation.
     * 
     * Client:
     * <ul>
     * <li>Referee: RF
     * <li>Coach: ClientRopeGame CH team strategy
     * <li>Contestant: ClientRopeGame CT team id
     * </ul>
     *
     * Server:
     * <ul>
     * <li>Playground: PG
     * <li>RefereeSite: RS
     * <li>General Information Repository: GR
     * <li>ContestantsBech: CB
     * </ul>
     *
     * @param args --help for full details
     * @throws InterruptedException if early termination
     */
    public static void main(String[] args) throws InterruptedException {

        Thread server, client;

        if (args.length == 1) {
            if (!args[0].contains("RF")) {
                server = new Thread(() -> {
                    ServerSide.ServerRopeGame.main(new String[]{args[0]});
                });
                server.setName("ServerMainThread");
                server.start();
                server.join();
            }
        }

        client = new Thread(() -> {
            ClientSide.ClientRopeGame.main(args);
        });
        client.setName("ClientMainThread");
        client.start();
        client.join();

    }

}
