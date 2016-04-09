package com.example.isaacwang.stazo_resistance;

/**
 * Created by isaacwang on 4/8/16.
 */
public class Mission {
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
