package RopeGame;

/**
 * This class starts the Rope Game. It instantiates all the active and passive
 * entities.
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {

    public static void main(String[] args) throws InterruptedException {

        Thread cb, pg, rs, gr,
                c11, c12, c13, c14, c15,
                c21, c22, c23, c24, c25,
                c1, c2,
                rf;

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

    /**
     * Function to generate a random strength when a player is instantiated
     *
     * @return a strength for a player instantiation
     */
    private static int randomStrength() {
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }
}
