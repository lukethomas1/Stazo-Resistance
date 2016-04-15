package com.example.isaacwang.stazo_resistance;

/**
 * Created by isaacwang on 4/8/16.
 */
public class Mission {
    private final int mems;
    private final int fails;

    public Mission(int mems, int fails) {
        this.mems = mems;
        this.fails = fails;
    }

    public Mission(){
        mems = 0;
        fails = 0;
    }
    public int getMems(){return mems;}

    /**
     * Checks if mission passes or fails
     * @param fails number of fails
     * @return whether the mission passes
     */

    public boolean missionPass(int fails) {return fails < this.fails;}
}
