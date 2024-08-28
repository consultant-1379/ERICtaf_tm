/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.ericsson.cifwk.tm.infrastructure.stubs.DropRetrievalServiceStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.EmailServiceStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.FileManagerImplStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.IsoRetrievalServiceStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.JiraDefectManagementStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.JiraRequirementManagementStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.TceClientStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.TrsClientStub;
import com.ericsson.cifwk.tm.infrastructure.stubs.TrsServiceStub;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.IsoRetrievalService;
import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.integration.ciportal.DropRetrievalService;
import com.ericsson.cifwk.tm.integration.email.EmailService;
import com.ericsson.cifwk.tm.integration.fileStorage.FileManager;
import com.ericsson.cifwk.tm.tce.TceClient;
import com.ericsson.cifwk.tm.trs.TrsClient;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.google.inject.AbstractModule;

@GuiceModule(env = {"dev"})
public class IntegrationStubModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RequirementManagement.class).to(JiraRequirementManagementStub.class);
        bind(DefectManagement.class).to(JiraDefectManagementStub.class);
        bind(EmailService.class).to(EmailServiceStub.class);
        bind(DropRetrievalService.class).to(DropRetrievalServiceStub.class);
        bind(IsoRetrievalService.class).to(IsoRetrievalServiceStub.class);
        bind(FileManager.class).to(FileManagerImplStub.class);
        bind(TrsClient.class).to(TrsClientStub.class);
        bind(TceClient.class).to(TceClientStub.class);
        bind(TrsService.class).to(TrsServiceStub.class);
    }

}
