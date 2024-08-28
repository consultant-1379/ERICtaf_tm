package com.ericsson.cifwk.tm.trs.service.exceptions;

public class UnresolvedContextException extends RuntimeException {

    public UnresolvedContextException(String jobName) {
        super("Cannot resolve context for job " + jobName);
    }
}
