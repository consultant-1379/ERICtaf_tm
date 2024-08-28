package com.ericsson.cifwk.tm.common;

import com.netflix.governator.annotations.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class UrlHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHelper.class);

    @Configuration("trs.url")
    private String tvsUrl;

    @Configuration("trs.api")
    private String tvsApiUrl;

    public String buildJobQueryUrl(String jobName) {
        String encodedParams = null;
        try {
            encodedParams = URLEncoder.encode("|" + jobName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Could not encode job query url", e);
        }
        return tvsApiUrl + "jobs?q=name=string" + encodedParams;
    }

    public String getContextListUrl() {
        return tvsUrl + "api/tce/contexts";
    }

    public String getTvsApiUrl() {
        return tvsApiUrl;
    }
}
