package com.ericsson.cifwk.tm.utils;

import com.ericsson.cifwk.tm.common.Versionable;

import java.util.Comparator;

public class VersionComparator implements Comparator<Versionable> {

    @Override
    public int compare(Versionable o1, Versionable o2) {
        return o2.getArtifactVersion().compareTo(o1.getArtifactVersion());
    }
}
