package com.example.isaacwang.stazo_resistance;

/**
 * Created by isaacwang on 3/29/16.
 */
public class Player {
    private String name;
    private boolean isSpy = false;
    private int num;

    public Player(int num) {
        this.num = num;
    }

    //Setters
    public void setSpy(boolean isSpy) {this.isSpy = isSpy;}
    public void setName(String name) {this.name = name;}

    // Getters
    public String getName(){return name;}
    public boolean getSpy(){return isSpy;}
    public int getNum(){return num;}
}