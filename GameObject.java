package com.example.brian.stazoresistance;

/**
 * Created by Brian on 3/29/2016.
 */
public class GameObject {

    private static final int win = 3; //How many rounds it takes to win the game

    private static int playerCount; //How many players are in the game

    private String[] players; //Array of player names
    private Boolean[] spy; //Parallel array of t/f depending on if the player is a spy or not
    private int playerIndex = 0; //Where to insert the next player

    private int spyScore = 0; //Score of the Spies
    private int resistanceScore = 0; //Score of the Resistance

    public GameObject (int count) {
        playerCount = count;

        players = new String[playerCount];
        spy = new Boolean[playerCount];

        randomizeSpies();
    }

    private void randomizeSpies () {
        int maxSpies = (playerCount + 2) / 3; //Gets max number of spies according to playerCount
        int numSpies = 0; //Current number of spies in array

        for (int i = 0; i < spy.length; i++) {

            spy[i] = false;

            //Already assigned all the spies, keep filling up the rest with false.
            if (numSpies >= maxSpies) {
                continue;
            }

            //Randomize spies; 0 = not spy, 1 = spy
            int randomNum = (int)(Math.random());

            if (randomNum == 1) {
                spy[i] = true;
                numSpies++;
            }
        }

        //Failed to assign correct number of spies, redo
        if (numSpies < maxSpies) {
            randomizeSpies();
        }
    }

    public int getNumPlayers () {
        return playerCount;
    }

    public Boolean addPlayer (String player) {

        if (playerIndex < playerCount) {

            players[playerIndex] = player;
            playerIndex++;

        }
        else {
            return false;
        }

        return true;
    }

    public int getSpyScore () {
        return spyScore;
    }

    public int getResistanceScore () {
        return resistanceScore;
    }
}