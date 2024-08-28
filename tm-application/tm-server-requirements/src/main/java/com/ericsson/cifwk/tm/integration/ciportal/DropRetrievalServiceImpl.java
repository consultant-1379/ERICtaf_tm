package com.ericsson.cifwk.tm.integration.ciportal;


import com.ericsson.cifwk.tm.integration.annotations.Integration;
import com.ericsson.cifwk.tm.integration.ciportal.dto.Drops;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.google.common.collect.Lists;
import com.netflix.governator.annotations.Configuration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import javax.ws.rs.client.Client;
import java.util.List;

@Integration
public class DropRetrievalServiceImpl implements DropRetrievalService {

    @Configuration("ci.portal.url")
    private String ciPortalUrl;

    private Client client;

    public DropRetrievalServiceImpl() {
        client = ClientFactory.newClient();
    }

    @Override
    @HystrixCommand(groupKey = "ci-portal", fallbackMethod = "getDropsFallback",
            commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000"),
            threadPoolProperties = @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "60000"))
    public List<DropInfo> getDrops(String product) {

        String url = buildDropsInProductRequestUrl(product);

        Drops rawDrops = client
                .target(url)
                .request()
                .get(Drops.class);

        return formatDrops(rawDrops);
    }

    public List<DropInfo> getDropsFallback() {
        return Lists.newArrayList();
    }

    private String buildDropsInProductRequestUrl(String product) {
        return ciPortalUrl + "/dropsInProduct/.json/?products=" + product;
    }

    private List<DropInfo> formatDrops(Drops rawDrops) {
        List<DropInfo> drops = Lists.newArrayList();
        for (String s : rawDrops.getRawDrops()) {
            String[] parts = s.split(":");
            if (parts.length > 1) {
                drops.add(new DropInfo(parts[0], parts[1]));
            }
        }
        return drops;
    }

    public String getCiPortalUrl() {
        return ciPortalUrl;
    }
}
