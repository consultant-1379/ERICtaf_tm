/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.domain.model.shared.Ordered;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Mapping {

    private Mapping() {
    }

    static <T> T newInstance(Class<T> instanceClass) {
        try {
            return instanceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T cast(Object instance, Class<T> instanceClass) {
        if (instance == null) {
            return null;
        }
        Preconditions.checkArgument(instanceClass.isInstance(instance));
        return (T) instance;
    }

    public static <E extends Identifiable<ID>, D extends Identifiable<ID>, ID> Diff<ID> idsDiff(
            Collection<E> entities,
            Collection<D> dtos) {
        Set<E> entitySnapshot = Sets.newLinkedHashSet(entities);
        Set<ID> entityIds = ids(entitySnapshot);
        Set<ID> dtoIds = ids(dtos);

        Sets.SetView<ID> skipped = Sets.difference(entityIds, dtoIds);
        return new Diff<>(dtoIds, skipped);
    }

    public static <E extends Identifiable<ID>, D extends Identifiable<ID>, ID> Diff<E> mapDiff(
            Collection<E> entities,
            Collection<D> dtos,
            DtoMapper<D, E> mapper,
            Class<E> entityClass) {
        Set<E> entitySnapshot = Sets.newLinkedHashSet(entities);
        Map<ID, E> entityMap = mapIds(entitySnapshot);
        Map<ID, D> dtoMap = mapIds(dtos);

        List<E> added = Lists.newArrayList();
        for (D dto : dtos) {
            E entity = entityMap.get(dto.getId());
            if (entity == null) {
                added.add(mapper.mapDto(dto, entityClass));
            } else {
                added.add(mapper.mapDto(dto, entity));
            }
        }

        List<E> skipped = Lists.newArrayList();
        for (E entity : entitySnapshot) {
            if (!dtoMap.containsKey(entity.getId())) {
                skipped.add(entity);
            }
        }
        return new Diff<>(added, skipped);
    }

    public static <E extends AuditedEntity & Identifiable<ID>,
            D extends Identifiable<ID>, ID> Diff<E> mapDiffAudited(

            Collection<E> entities,
            Collection<D> dtos,
            DtoMapper<D, E> mapper,
            Class<E> entityClass) {
        Diff<E> diff = mapDiff(entities, dtos, mapper, entityClass);
        for (E entity : diff.getSkipped()) {
            entity.delete();
        }
        return diff;
    }

    public static <E extends AuditedEntity & Identifiable<ID> & Ordered<Integer>,
            D extends Identifiable<ID>,
            ID> Diff<E> mapDiffAuditedAndOrdered(

            Collection<E> entities,
            Collection<D> dtos,
            DtoMapper<D, E> mapper,
            Class<E> entityClass) {
        Diff<E> diff = mapDiffAudited(entities, dtos, mapper, entityClass);
        Set<E> added = diff.getAdded();
        int i = 1;
        for (E entity : added) {
            entity.setSequenceOrder(i++);
        }
        return diff;
    }

    static <T extends Identifiable<ID>, ID> Map<ID, T> mapIds(Collection<T> identifiables) {
        Map<ID, T> map = Maps.newLinkedHashMap();
        for (T identifiable : identifiables) {
            ID id = identifiable.getId();
            if (id != null) {
                map.put(id, identifiable);
            }
        }
        return map;
    }

    public static <T extends Identifiable<ID>, ID> Set<ID> ids(Collection<T> identifiables) {
        return mapIds(identifiables).keySet();
    }

    public static <E extends Identifiable<ID>, D extends Identifiable<ID>, ID> boolean idEquals(E one, D two) {
        if (one == null || two == null) return one == two;
        return one.getId().equals(two.getId());
    }

    public static <E, D> List<D> mapEntities(
            Collection<E> entities,
            final EntityMapper<E, D> mapper,
            final Class<D> dtoClass) {
        return FluentIterable.from(entities)
                .transform(new Function<E, D>() {
                    @Override
                    public D apply(E input) {
                        return mapper.mapEntity(input, dtoClass);
                    }
                })
                .toList();
    }

}
