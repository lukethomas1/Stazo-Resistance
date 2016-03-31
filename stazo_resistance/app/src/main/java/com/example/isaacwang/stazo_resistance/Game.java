package com.example.isaacwang.stazo_resistance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Game {

    //Setup Logic
    private static final int win = 3; //How many rounds it takes to win the game
    private static int numPlayers; //How many players are in the game
    private Player[] players;
    private int playerIndex = 0; //Where to insert the next player
    private boolean resistanceWon;

    //Mission Logic
    private Player[] agents = new Player[5]; //Agents on the mission
    private int round = 0; //The round of the game we're on, 0-4
    private int proposerIndex = 0; //Who is currently proposing
    private int agentIndex = 0; //Where to insert next agent
    private int sabotageIndex = 0; //Who is currently succeed/sabotaging
    private int fails = 0; //Number of fails on the mission
    private int spyScore = 0; //Score of the Spies
    private int resistanceScore = 0; //Score of the Resistance

    // Sequences for missions depending on number of players
    private static final Mission[] fiveSequence = {new Mission(2,1), new Mission(3,1),
            new Mission(2,1), new Mission(3,1), new Mission(3,1)};
    private static final Mission[] sixSequence = {new Mission(2,1), new Mission(3,1),
            new Mission(4,1), new Mission(3,1), new Mission(4,1)};
    private static final Mission[] sevenSequence = {new Mission(2,1), new Mission(3,1),
            new Mission(3,1), new Mission(4,2), new Mission(4,1)};
    private static final Mission[] entSequence = {new Mission(3,1), new Mission(4,1),
            new Mission(4,1), new Mission(5,2), new Mission(5,1)};

    // Array of all the sequences
    private static final Mission[][] allSequences = {fiveSequence, sixSequence, sevenSequence,
            entSequence, entSequence, entSequence};

    // The sequence to be used for this game
    private Mission[] sequence;

    // Getters
    public int getNumPlayers () {return numPlayers;}
    public Player getPlayer(int index) {return players[index];}
    public Player getProposer() {return players[proposerIndex];}
    public int getIteratorIndex() {return playerIndex;}
    public Mission getMission() {return sequence[round];}
    public Player[] getAgents() {return agents;}
    public int getAgentIndex() {return agentIndex;}
    public int getSabotageIndex() {return sabotageIndex;}
    public int getSpyScore () {return spyScore;}
    public int getResistanceScore () {return resistanceScore;}
    public String getPlayerName(int index) {
        if (index >= numPlayers) {
            return "default";
        }
        else {
            return getPlayer(index).getName();
        }
    }
    public boolean getWhoWon() {
        return resistanceWon;
    }

    // Setters
    public void incrementSpyScore () {spyScore++;}
    public void incrementResistanceScore () {resistanceScore++;}
    public void setWhoWon(boolean won) {
        resistanceWon = won;
    }

    //SETUP LOGIC BELOW---------------------------------------------------------------------------
    /**
     * Constructor for game
     * @param numPlayers number of players
     */
    public Game(int numPlayers) {

        // initialize variables
        this.numPlayers = numPlayers;
        players = new Player[numPlayers];
        sequence = allSequences[numPlayers-5];

        // generate players, no names
        generatePlayers();

        // set spies
        assignSpies();
    }

    /**
     * generates nameless players to prep for assignSpies
     */
    private void generatePlayers() {
        for (int i=0; i < numPlayers; i++) {
            players[i] = new Player(i);
        }
    }

    /**
     * Assigns the spies randomly in the spy array.
     */
    private void assignSpies () {
        int maxSpies = (numPlayers + 2) / 3; //Gets max number of spies according to playerCount
        int numSpies = 0; //Current number of spies in array

        while (numSpies < maxSpies) {

            //Randomly generates index from 0 to playerCount-1
            int randomNum = (int) (Math.random() * numPlayers);

            //If the player at that index is not already a spy, make them a spy
            if (!players[randomNum].isSpy()) {
                players[randomNum].setSpy(true);
                numSpies++;
            }
        }
    }

    /**
     * Add a player to the array (just sets the name)
     * @param name The player to be added.
     * @return t/f depending on success of adding the player.
     */
    public Boolean addPlayerName (String name) {
        if (playerIndex < numPlayers) {
            players[playerIndex].setName(name);
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Iterates through the players, returning the next one in the array.
     * @return The next player in the array. If there are no players left, returns null.
     */
    public Player getNextPlayer() {
        // Ensure there is a next player to return
        if ( playerIndex < players.length )
            return players[playerIndex++];
        else return null;
    }

    //Have we iterated through all the players?
    public Boolean fullyIterated() {return playerIndex >= numPlayers;}


    //MISSION LOGIC BELOW--------------------------------------------------------------------------

    //Add a player to the mission if not already on it
    public void addToMission(Player player) {
        //If the player is already on the mission, do nothing
        if (!isOnMission(player) && agentIndex < getMission().getMems()) {
            agents[agentIndex++] = player;
        }
    }

    //Is the player already in the mission?
    public boolean isOnMission(Player player) {

        //Not a for-each loop because agent array is never truly emptied, index is just reset
        for (int i = 0; i < agentIndex; i++) {
            if (agents[i].equals(player)) {
                return true;
            }
        }
        return false;
    }

    //Is the mission ready to be voted on?
    public boolean missionReady(){return (agentIndex == getMission().getMems());}

    //Called when an agent fails a mission
    public void sabotageMission(){fails ++;}

    //Called to "clear" a mission, resets index
    public void clearAgents() {agentIndex = 0;}


    /**
     * Checks if mission passes or fails
     * @param fails the number of fails entered
     * @return whether the mission passed
     */
    public boolean executeMission () {
        sabotageIndex = 0;
        if (getMission().missionPass(fails)){
            resistanceScore++;
            round++;
            fails = 0;
            return true;
        }
        else {
            spyScore++;
            round++;
            fails = 0;
            return false;
        }
    }

    public void incrementProposer() {
        proposerIndex++;
        if (proposerIndex >= numPlayers) {
            proposerIndex = 0;
        }
    }
    public void incrementSabotager() {sabotageIndex++;}

    public static class Mission {
        private final int numMems;
        private final int numFails;
        public Mission(int mems, int fails) {
            numMems = mems;
            numFails = fails;
        }
        public int getMems(){return numMems;}

        /**
         * Checks if mission passes or fails
         * @param fails number of fails
         * @return whether the mission passes
         */

        private boolean missionPass(int fails) {return fails < numFails;}
    }
}
