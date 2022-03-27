package com.jstarcraft.crawler.mind;

import java.util.ArrayList;

public class Topic {

    private String text;

    private int type;

    private ArrayList<Topic> child;

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    public ArrayList<Topic> getChild() {
        return child;
    }

}
