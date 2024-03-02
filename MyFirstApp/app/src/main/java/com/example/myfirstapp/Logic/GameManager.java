package com.example.myfirstapp.Logic;

import java.util.ArrayList;

public class GameManager {
    private int life;
    private int numRows;
    private int numCols;
    private ArrayList<FallingObject> fallingObjects = new ArrayList<>();

    private int posSpaceShip;

    public GameManager(int life, int numRows, int numCols) {
        this.life = life;
        this.numRows = numRows;
        this.numCols = numCols;
        this.posSpaceShip = 1;
    }

    public int getPosSpaceShip() {
        return posSpaceShip;
    }

    public ArrayList<FallingObject> getFallingObjects() {
        return fallingObjects;
    }

    public void moveLeft() {
        if(posSpaceShip != 0) {
            posSpaceShip -= 1;
        }
    }

    public void moveRight() {
        if(posSpaceShip != numCols-1) {
            posSpaceShip +=1;
        }
    }

    public void moveDownAllObjects() {
        for(int i = 0; i < fallingObjects.size(); i++) {
            fallingObjects.get(i).setposI(fallingObjects.get(i).getposI()+1);
        }
    }

    public void dropObject() {
        int posI = -1;
        int posJ = -1;

        int posIC = -1;
        int posJC = -1;

        int posI2 = -1;
        int posJ2 = -1;

        posJ = (int)(Math.random() * numCols);
        fallingObjects.add(new FallingObject(posI, posJ, false));

        do {
            posJC = (int)(Math.random() * numCols);
        } while (posJC == posJ);
        fallingObjects.add(new FallingObject(posIC, posJC, true));

    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return this.life;
    }
}
