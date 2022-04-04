package com.jstarcraft.crawler.mind;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 亿图主题
 * 
 * @author Birdy
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdrawsoftTopic {

    private String text;

    private List<EdrawsoftTopic> child;

    EdrawsoftTopic() {
    }

    public EdrawsoftTopic(String text, List<EdrawsoftTopic> child) {
        this.text = text;
        this.child = child;
    }

    public String getTitle() {
        return text;
    }

    public List<EdrawsoftTopic> getChildren() {
        return child;
    }

}
