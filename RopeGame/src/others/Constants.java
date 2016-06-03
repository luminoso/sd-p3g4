package others;

/**
 * This class contains common constants for the Rope Game
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class Constants {

    // number of trials in a game
    public static final int NUMBER_OF_TRIALS = 6;

    // number of games in a match
    public static final int NUMBER_OF_GAMES = 3;

    // number of players in a team bench
    public static final int NUMBER_OF_PLAYERS_IN_THE_BENCH = 5;

    // number of players for each team in the playground
    public static final int NUMBER_OF_PLAYERS_AT_PLAYGROUND = 3;

    // initial minimum force for a player at instantiation time
    public static final int INITIAL_MINIMUM_FORCE = 10;

    // initial maximum force for a player at instantiation time
    public static final int INITIAL_MAXIMUM_FORCE = 20;

    // minimum time for a player to pull the rope (in ms)
    public static final int MINIMUM_WAIT_TIME = 1;

    // maximum time for a player to pull the rope (in ms)
    public static final int MAXIMUM_WAIT_TIME = 3;

    // position of the flag for a win by knock out
    public static final int KNOCK_OUT_FLAG_POSITION = 4;

    // file where the game log will be stored
    public static final String FILE_NAME = "unordered_game.log";
    
    // file where the reordered game log will be stored
    public static final String REORDER_FILE_NAME = "game.log";
    
    // 2*team size + 2 coachs + 1 Referee
    public static final int VECTOR_TIMESTAMP_SIZE = 3 + (NUMBER_OF_PLAYERS_IN_THE_BENCH * 2);
}
