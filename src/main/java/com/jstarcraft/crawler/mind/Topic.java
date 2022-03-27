package com.jstarcraft.crawler.mind;

import java.util.List;

public class Topic {

    private String text;

    private List<Topic> child;

    public String getText() {
        return text;
    }

    public List<Topic> getChildren() {
        return child;
    }

}
