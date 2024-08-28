package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import com.ericsson.cifwk.tm.presentation.dto.DropInfo;

public enum DropType {

    ENM_OTHER(getDrop(8L, "ENM", "Other")),
    ENM_2(getDrop(3L, "ENM", "2.0.ENM")),
    OSS_2(getDrop(6L, "OSS-RC", "2.0.OSS")),
    ASSURE_OTHER(getDrop(11L, "Assure", "Other"));

    private DropInfo dropInfo;

    DropType(DropInfo dropInfo) {
        this.dropInfo = dropInfo;
    }

    public static DropInfo getDrop(Long id, String productName, String name) {
        return new DropInfo(id, productName, name);
    }

    public DropInfo getDropInfo() {
        return dropInfo;
    }
}
