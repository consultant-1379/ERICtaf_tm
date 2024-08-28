/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.ApiInfo;
import com.ericsson.cifwk.tm.presentation.resources.RootResource;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class RootController implements RootResource {

    @Inject
    private BuildConfiguration build;

    @Override
    public Response info() {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setVersion(build.getVersion());
        apiInfo.setBuildDate(build.getDate());
        return Responses.ok(apiInfo);
    }

}
