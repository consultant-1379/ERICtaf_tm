/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.IsoRetrievalService;
import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.integration.ciportal.DropRetrievalService;
import com.ericsson.cifwk.tm.integration.ciportal.DropRetrievalServiceImpl;
import com.ericsson.cifwk.tm.integration.ciportal.IsoRetrievalServiceImpl;
import com.ericsson.cifwk.tm.integration.email.EmailService;
import com.ericsson.cifwk.tm.integration.email.impl.EmailServiceImpl;
import com.ericsson.cifwk.tm.integration.fileStorage.FileManager;
import com.ericsson.cifwk.tm.integration.fileStorage.impl.FileManagerImpl;
import com.ericsson.cifwk.tm.integration.jira.impl.DefectManagementImpl;
import com.ericsson.cifwk.tm.integration.jira.impl.RequirementManagementImpl;
import com.ericsson.cifwk.tm.tce.TceClient;
import com.ericsson.cifwk.tm.tce.impl.TceClientImpl;
import com.ericsson.cifwk.tm.trs.TrsClient;
import com.ericsson.cifwk.tm.trs.impl.TrsClientImpl;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.ericsson.cifwk.tm.trs.service.impl.TrsServiceImpl;
import com.google.inject.AbstractModule;

@GuiceModule(env = {"stage", "prod"}, priority = 3)
public class IntegrationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RequirementManagement.class).to(RequirementManagementImpl.class);
        bind(DefectManagement.class).to(DefectManagementImpl.class);
        bind(EmailService.class).to(EmailServiceImpl.class);
        bind(DropRetrievalService.class).to(DropRetrievalServiceImpl.class);
        bind(IsoRetrievalService.class).to(IsoRetrievalServiceImpl.class);
        bind(FileManager.class).to(FileManagerImpl.class);
        bind(TrsClient.class).to(TrsClientImpl.class);
        bind(TceClient.class).to(TceClientImpl.class);
        bind(TrsService.class).to(TrsServiceImpl.class);
    }

}
