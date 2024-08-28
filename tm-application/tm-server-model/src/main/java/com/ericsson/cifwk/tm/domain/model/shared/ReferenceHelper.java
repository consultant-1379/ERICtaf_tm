package com.ericsson.cifwk.tm.domain.model.shared;

import com.ericsson.cifwk.tm.common.HasName;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.google.common.base.Preconditions;

public final class ReferenceHelper {

    private ReferenceHelper() {
    }

    public static <ID, T extends Identifiable<ID>> T parseEnumById(ID id, Class<T> referenceClass) {
        Preconditions.checkArgument(referenceClass.isEnum());
        if (id == null) {
            return null;
        }
        for (T value : referenceClass.getEnumConstants()) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }

    public static <T extends HasName> T parseEnumByName(String name, Class<T> referenceClass) {
        Preconditions.checkArgument(referenceClass.isEnum());
        if (name == null) {
            return null;
        }
        String normalizedName = name.replace("_", " "); // To normalize enum constant names
        for (T value : referenceClass.getEnumConstants()) {
            if (normalizedName.equalsIgnoreCase(value.getName())) {
                return value;
            }
        }
        return null;
    }

}
