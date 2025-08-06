package org.ucd.shortlink.project.service;

/**
 * URL Title fetch interface layer
 */
public interface UrlTitleService {
    /**
     * Retrieve provided url fetch url website title content in str.
     *
     * @param url provided website url address
     * @return title of the website in str
     */
    String getTitleByUrl(String url);
}
