package com.yyHaker.lexical.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录状态信息的类
 * Node
 *
 * @author Le Yuan
 * @date 2016/10/15
 */
public class Node {
    private int  CurrentState;                              //当前状态
    private Map<String,Node> stateTransitionList;//当前状态的转换表集合
    private boolean isFiniteState;                        //是否为终止状态

    public Node(int currentState, Map<String, Node> stateTransitionList, boolean isFiniteState) {
        CurrentState = currentState;
        this.stateTransitionList = stateTransitionList;
        this.isFiniteState = isFiniteState;
    }

    public Node(int currentState) {
        CurrentState = currentState;
        this.stateTransitionList = new HashMap<String,Node>();
        this.isFiniteState =false;
    }



    public int getCurrentState() {
        return CurrentState;
    }

    public void setCurrentState(int currentState) {
        CurrentState = currentState;
    }

    public Map<String, Node> getStateTransitionList() {
        return stateTransitionList;
    }

    public void setStateTransitionList(Map<String, Node> stateTransitionList) {
        this.stateTransitionList = stateTransitionList;
    }

    public boolean isFiniteState() {
        return isFiniteState;
    }

    public void setFiniteState(boolean finiteState) {
        isFiniteState = finiteState;
    }

    @Override
    public String toString() {
        return "CurrentState="+CurrentState+","+stateTransitionListToString()+",isFiniteState="+isFiniteState;
    }

    /**
     * 重写toString方法，得到stateTransitionList的String形式
     * @return
     */
    public String  stateTransitionListToString(){
        StringBuilder sb=new StringBuilder();
        List<String> keyList=new ArrayList<String>();
        for (Map.Entry<String,Node> entry: stateTransitionList.entrySet()){
              keyList.add(entry.getKey());
            //sb.append(entry.getKey()+"->"+stateTransitionList.get(entry.getKey())+" ");
        }

        for (String key:keyList){
            sb.append(key+"->"+stateTransitionList.get(key).getCurrentState()+" ");
        }
        return sb.toString();
    }

    /**
     * 判断当前Node对于inputChar是否能进入下一个nextNode,如果能返回nextNode对象,
     * 如果不能返回null
     * @param inputChar 输入字符
     * @return Node
     */
    public Node getNextStateNode(char inputChar){
        if (stateTransitionList!=null){
            for (Map.Entry<String,Node> entry:stateTransitionList.entrySet()){
                if((inputChar+"").matches( entry.getKey())){
                    return stateTransitionList.get(entry.getKey());
                }
            }
        }
        return null;
    }




}
