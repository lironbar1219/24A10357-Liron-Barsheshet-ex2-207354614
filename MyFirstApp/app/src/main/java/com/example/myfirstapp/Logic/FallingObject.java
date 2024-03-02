package com.example.myfirstapp.Logic;

public class FallingObject {

    private int posI;
    private int posJ;

    private Boolean isCoin;

    public FallingObject(int posI, int posJ, Boolean isCoin) {
        this.posI = posI;
        this.posJ = posJ;
        this.isCoin = isCoin;
    }

    public int getposI() {
        return posI;
    }

    public int getposJ() {
        return posJ;
    }

    public void setposI(int posI) {
        this.posI = posI;
    }

    public void setposJ(int posJ) {
        this.posJ = posJ;
    }

    public Boolean getIsCoin() {
        return isCoin;
    }
}
