package com.hoangpro.pikacover.model;

public class MyMessage {
    public int action;
    private Object object;

    public MyMessage(int action, Object object) {
        this.action = action;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public int getAction() {
        return action;
    }
}
