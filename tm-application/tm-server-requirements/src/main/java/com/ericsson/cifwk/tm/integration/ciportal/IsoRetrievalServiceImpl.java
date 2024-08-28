package com.ericsson.cifwk.tm.integration.ciportal;

import com.ericsson.cifwk.tm.integration.IsoRetrievalService;
import com.ericsson.cifwk.tm.integration.annotations.Integration;
import com.ericsson.cifwk.tm.integration.ciportal.dto.CiIso;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.google.common.collect.Lists;
import com.netflix.governator.annotations.Configuration;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.List;

@Integration
public class IsoRetrievalServiceImpl implements IsoRetrievalService {

    @Configuration("ci.portal.url")
    private String ciPortalUrl;

    private Client client;

    public IsoRetrievalServiceImpl() {
        client = ClientFactory.newClient();
    }

    @Override
    @HystrixCommand(groupKey = "ci-portal", fallbackMethod = "getIsosFallback",
            commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000"),
            threadPoolProperties = @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "60000"))
    public List<IsoInfo> getIsos(String product, String drop) {

        String url = buildRequestUrl(product, drop);
        List<CiIso> ciIsos = client
                .target(url)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<CiIso>>() {});

        return mapToDtos(ciIsos);
    }

    public List<IsoInfo> getIsosFallback() {
        return Lists.newArrayList();
    }

    private String buildRequestUrl(String product, String drop) {
        String url = ciPortalUrl + "api/product/{0}/drop/{1}/isos/";
        return MessageFormat.format(url, product, drop);
    }

    private List<IsoInfo> mapToDtos(List<CiIso> ciIsos) {
        List<IsoInfo> dtos = Lists.newArrayList();
        for (CiIso ciIso : ciIsos) {
            dtos.add(new IsoInfo(ciIso.getIsoName(), ciIso.getIsoVersion()));
        }
        return dtos;
    }

}
