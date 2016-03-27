/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RopeGame;

/**
 * General Description:
 * This class contains constants common for the Rope Game
 * @author Eduardo Sousa
 */
public class Constants {
    /**
     * Number of trials in a game.
     */
    public static int NUMBER_OF_TRIALS = 6;
    
    /**
     * Number of games in a match.
     */
    public static int NUMBER_OF_GAMES = 3;
    
    /**
     * Number of players in a team bench.
     */
    public static int NUMBER_OF_PLAYERS_IN_THE_BENCH = 5;
    
    /**
     * Number of players for each team in the playground.
     */
    public static int NUMBER_OF_PLAYERS_AT_PLAYGROUND = 3;
    
    /**
     * Initial minimum force for a player at instantiation time.
     */
    public static int INITIAL_MINIMUM_FORCE = 10;
    
    /**
     * Initial maximum force for a player at instantiation time.
     */
    public static int INITIAL_MAXIMUM_FORCE = 20;
    
    /**
     * Minimum time for a player to pull the rope.
     */
    public static int MINIMUM_WAIT_TIME = 1000;
    
    /**
     * Maximum time for a player to pull the rope.
     */
    public static int MAXIMUM_WAIT_TIME = 3000;
    
    /**
     * File where the game log will be stored.
     */
    public static String FILE_NAME = "game.log";
}
