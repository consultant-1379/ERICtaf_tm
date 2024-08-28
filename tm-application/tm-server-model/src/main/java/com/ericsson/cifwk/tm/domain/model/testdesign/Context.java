package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.NamedWithId;

import java.util.Optional;

public enum Context implements NamedWithId<Integer> {

    REST(1, "REST"),
    UI(2, "UI"),
    CLI(3, "CLI"),
    API(4, "API");

    private final int id;
    private final String name;

    Context(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Optional<Context> getEnum(String value) {
        for (Context context : Context.class.getEnumConstants()) {
            if (context.getName().equalsIgnoreCase(value)) {
                return Optional.of(context);
            }
        }
        return Optional.empty();
    }

}
