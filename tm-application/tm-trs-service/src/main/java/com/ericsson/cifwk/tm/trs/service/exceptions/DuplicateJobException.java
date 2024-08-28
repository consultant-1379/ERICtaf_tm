package com.ericsson.cifwk.tm.trs.service.exceptions;


public class DuplicateJobException extends RuntimeException {

    public DuplicateJobException(String jobName, String contexts) {
        super("Cannot record data in TRS. Jobs with name: " + jobName + " exist in more than 1 context: " + contexts);
    }
}
