package RopeGame;

/**
 * Constants for server configuration.
 * 
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public class ServerConfigs {
    /**
     * Number of trials in a game.
     */
    public static final String CONTESTANTS_BENCH_ADDRESS = "127.0.0.1";
    
    public static final int CONTESTANTS_BENCH_PORT = 22330;
    
    /**
     * Number of games in a match.
     */
    public static final String PLAYGROUND_ADDRESS = "127.0.0.1";
    
    /**
     * Number of players in a team bench.
     */
    public static final int PLAYGROUND_PORT = 22331;
    
    /**
     * Number of players for each team in the playground.
     */
    public static final String REFEREE_SITE_ADDRESS = "127.0.0.1";
    
    /**
     * Initial minimum force for a player at instantiation time.
     */
    public static final int REFEREE_SITE_PORT = 22332;
    
    /**
     * Initial maximum force for a player at instantiation time.
     */
    public static final String GENERAL_INFORMATION_REPOSITORY_ADDRESS = "127.0.0.1";
    
    /**
     * Minimum time for a player to pull the rope (in ms).
     */
    public static final int GENERAL_INFORMATION_REPOSITORY_PORT = 22333;
}
