package com.yyHaker.lexical.property;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * FAInformationProperty
 *
 * @author Le Yuan
 * @date 2016/10/16
 */
public class FAInformationProperty {
    private final SimpleIntegerProperty currentState=new SimpleIntegerProperty();
    private  final SimpleStringProperty   stateTransitionList=new SimpleStringProperty();
    private final SimpleBooleanProperty isFiniteState=new SimpleBooleanProperty();


    public FAInformationProperty(int currentState, String stateTransitionList, boolean isFiniteState) {
       setCurrentState(currentState);
        setStateTransitionList(stateTransitionList);
        setIsFiniteState(isFiniteState);
    }

    public int getCurrentState() {
        return currentState.get();
    }

    public SimpleIntegerProperty currentStateProperty() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState.set(currentState);
    }

    public String getStateTransitionList() {
        return stateTransitionList.get();
    }

    public SimpleStringProperty stateTransitionListProperty() {
        return stateTransitionList;
    }

    public void setStateTransitionList(String stateTransitionList) {
        this.stateTransitionList.set(stateTransitionList);
    }

    public boolean isIsFiniteState() {
        return isFiniteState.get();
    }

    public SimpleBooleanProperty isFiniteStateProperty() {
        return isFiniteState;
    }

    public void setIsFiniteState(boolean isFiniteState) {
        this.isFiniteState.set(isFiniteState);
    }
}
