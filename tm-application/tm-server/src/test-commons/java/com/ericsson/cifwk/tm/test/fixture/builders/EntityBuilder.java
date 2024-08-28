package com.ericsson.cifwk.tm.test.fixture.builders;

public abstract class EntityBuilder<T> {

    protected T entity;

    public EntityBuilder(T entity) {
        this.entity = entity;
    }

    public T build() {
        return entity;
    }
}
