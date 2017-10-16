package com.tvs.comet.models;

/**
 * Created by UITOUX10 on 10/13/17.
 */

public class GetStatewiseRankingGraph {
    String ActID, StateID, StateDescr, ActDescr;
    int green, yellow, red;

    public String getActID() {
        return ActID;
    }

    public void setActID(String actID) {
        ActID = actID;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getStateDescr() {
        return StateDescr;
    }

    public void setStateDescr(String stateDescr) {
        StateDescr = stateDescr;
    }

    public String getActDescr() {
        return ActDescr;
    }

    public void setActDescr(String actDescr) {
        ActDescr = actDescr;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getYellow() {
        return yellow;
    }

    public void setYellow(int yellow) {
        this.yellow = yellow;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }
}
