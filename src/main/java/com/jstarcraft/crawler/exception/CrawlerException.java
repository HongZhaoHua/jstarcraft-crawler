package com.jstarcraft.crawler.exception;

/**
 * 爬虫异常
 * 
 * @author Birdy
 */
public class CrawlerException extends RuntimeException {

    private static final long serialVersionUID = 8007956419630522723L;

    public CrawlerException() {
        super();
    }

    public CrawlerException(String message, Throwable exception) {
        super(message, exception);
    }

    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(Throwable exception) {
        super(exception);
    }

}
