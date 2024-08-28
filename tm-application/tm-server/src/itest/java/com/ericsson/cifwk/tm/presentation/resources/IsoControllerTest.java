package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IsoControllerTest extends BaseControllerLevelTest {

    private Drop drop;

    private static final GenericType<List<IsoInfo>> ISO_INFO_LIST =
            new GenericType<List<IsoInfo>>() {
            };

    @Test
    public void shouldGetIsosByProductAndDrop() throws Exception {
        persistEntities();

        Response response = app.client()
                .path("/tm-server/api/isos")
                .queryParam("dropId", drop.getId())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        List<IsoInfo> isos = response.readEntity(ISO_INFO_LIST);
        response.close();
        assertThat(isos.size(), equalTo(2));
    }

    private void persistEntities() {
        app.persistence().cleanupTables();
        Product enm = TestEntityFactory.buildProduct("ENM").build();
        drop = TestEntityFactory.buildDrop(enm, "16.3").build();
        app.persistence().persistInTransaction(enm, drop);
    }
}
