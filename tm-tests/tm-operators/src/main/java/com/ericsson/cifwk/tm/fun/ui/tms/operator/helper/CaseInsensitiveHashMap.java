package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.ForwardingMap;

import java.util.Map;

public class CaseInsensitiveHashMap<V> extends ForwardingMap<String, V> {

    private final Map<String, V> delegate;

    public CaseInsensitiveHashMap() {
        delegate = Maps.newHashMap();
    }

    @Override
    protected Map<String, V> delegate() {
        return delegate;
    }

    private String normalizeKey(Object key) {
        return ((String) key).toLowerCase();
    }

    @Override
    public boolean containsKey(Object key) {
        return (key instanceof String) && super.containsKey(normalizeKey(key));
    }

    @Override
    public V get(Object key) {
        if (key instanceof String) {
            return super.get(normalizeKey(key));
        }
        return null;
    }

    @Override
    public V put(String key, V value) {
        return super.put(normalizeKey(key), value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        for (Entry<? extends String, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
