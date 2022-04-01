package com.jstarcraft.crawler.book;

public class WereadChapter extends Chapter {

    private int id;

    private int level;

    public WereadChapter(String title, int id, int level) {
        super(title);
        this.id = id;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

}
