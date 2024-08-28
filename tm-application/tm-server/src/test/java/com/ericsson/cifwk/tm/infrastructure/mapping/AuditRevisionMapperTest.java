package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.Modification;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AuditRevisionMapperTest {

    private AuditRevisionMapper mapper;

    @Before
    public void setUp() {
        mapper = new AuditRevisionMapper();
    }

    @Test
    public void testMapEntity() throws Exception {
        Map<Number, AuditRevisionEntity> revisionEntityMap = Maps.newHashMap();
        AuditRevisionEntity entity1 = new AuditRevisionEntity();
        AuditRevisionEntity entity2 = new AuditRevisionEntity();
        revisionEntityMap.put(1, entity1);
        revisionEntityMap.put(2, entity2);

        User user = new User();
        user.setUserName("id");
        entity1.setUser(user);
        entity1.setTimestamp(1L);
        entity2.setUser(user);
        entity2.setTimestamp(2L);

        List<Modification> modifications = Lists.newArrayList();
        for (AuditRevisionEntity entity : revisionEntityMap.values()) {
            Modification modification = mapper.mapEntity(entity, Modification.class);
            modifications.add(modification);
        }

        assertThat(modifications.get(0).getUsername(), equalTo("id"));
        assertThat(modifications.get(0).getTimestamp().getTime(), equalTo(1L));
        assertThat(modifications.get(1).getUsername(), equalTo("id"));
        assertThat(modifications.get(1).getTimestamp().getTime(), equalTo(2L));
    }

}
