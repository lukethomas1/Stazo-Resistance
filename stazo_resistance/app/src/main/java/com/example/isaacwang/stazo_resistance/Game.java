package com.example.isaacwang.stazo_resistance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Game {

    private static final int win = 3; //How many rounds it takes to win the game

    private static int numPlayers; //How many players are in the game
    private Player[] players;
    private int playerIndex = 0; //Where to insert the next player

    private Player[] agents; //Agents on the mission
    private int missionCount = 0;

    private int spyScore = 0; //Score of the Spies
    private int resistanceScore = 0; //Score of the Resistance
    private int round = 0; //The round of the game we're on, 0-4

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

    public void assignMission () {
        Mission[] mission = allSequences[numPlayers - 5];

        int mem = mission[missionCount].getMems();

        agents = new Player[mem];
    }

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
            if (!players[randomNum].getSpy()) {
                players[randomNum].setSpy(true);
                numSpies++;
            }
        }
    }

    public int getNumPlayers () {return numPlayers;}

    public int getSpyScore () {return spyScore;}

    public int getResistanceScore () {return resistanceScore;}

    public int getPlayerIndex() {return playerIndex;}

    public Mission getMission() {return sequence[round];}

    public Player[] getAgents() {return agents;}

    /**
     * Add a player to the array (just sets the name)
     * @param name The player to be added.
     * @return t/f depending on success of adding the player.
     */
    public Boolean addPlayer (String name) {
        if (playerIndex < numPlayers) {
            players[playerIndex].setName(name);
            playerIndex++;
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Checks if mission passes or fails
     * @param fails the number of fails entered
     * @return whether the mission passed
     */
    public boolean executeMission (int fails) {
        if (getMission().missionPass(fails)){
            resistanceScore++;
            round++;
            return true;
        }
        else {
            spyScore++;
            round++;
            return false;
        }
    }

    private static class Mission {
        private final int numMems;
        private final int numFails;
        public Mission(int mems, int fails) {
            numMems = mems;
            numFails = fails;
        }
        public int getMems(){return numMems;}
        public int getFails(){return numFails;}

        /**
         * Checks if mission passes or fails
         * @param fails number of fails
         * @return whether the mission passes
         */
        public boolean missionPass(int fails){
            return fails < numFails;
        }
    }
}
