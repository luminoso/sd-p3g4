package RopeGame;

import java.util.Scanner;

/**
 * General description: This class starts the Rope Game.
 * It instantiates all the active and passive entities.
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class RopeGame {

    public static void main(String[] args) throws InterruptedException {
        
        new Thread(() -> ServerSide.ServerRopeGame.main(new String[]{"CB"})).start();
        new Thread(() -> ServerSide.ServerRopeGame.main(new String[]{"PG"})).start();
        new Thread(() -> ServerSide.ServerRopeGame.main(new String[]{"RS"})).start();
        new Thread(() -> ServerSide.ServerRopeGame.main(new String[]{"GR"})).start();
        Thread.sleep(1000);
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CH", "1", "0"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CH", "2", "1"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "1"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "2"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "3"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "4"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "1", "5"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "1"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "2"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "3"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "4"})).start();
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"CT", "2", "5"})).start();
        Thread.sleep(1000);
        new Thread(() -> ClientSide.ClientRopeGame.main(new String[]{"RF"})).start();

    }

    /**
     * Function to generate a random strength when a player is instantiated.
     *
     * @return a strength for a player instantiation
     */
    private static int randomStrength() {
        return Constants.INITIAL_MINIMUM_FORCE + (int) (Math.random() * (Constants.INITIAL_MAXIMUM_FORCE - Constants.INITIAL_MINIMUM_FORCE));
    }
}
