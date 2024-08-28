package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.integration.DefectManagement;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class CreateJiraDefectCommand implements Command<Object> {

    @Inject
    private DefectManagement defectManagement;

    @Override
    public Response apply(Object input) {
        return defectManagement.createDefect(input);
    }
}
