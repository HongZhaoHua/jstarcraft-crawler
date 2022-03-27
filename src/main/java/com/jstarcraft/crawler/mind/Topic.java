package com.jstarcraft.crawler.mind;

import java.util.List;

public class Topic {

    private String text;

    private int type;

    private List<Topic> child;

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    public List<Topic> getChild() {
        return child;
    }

}
