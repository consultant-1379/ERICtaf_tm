package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DropRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private DropRepository dropRepository;

    @Test
    public void shouldFindDropsByProduct() {
        Product product = persistProductWithDrops("ENM");
        List<Drop> enmDrops = dropRepository.findByProduct(product.getId());
        assertThat(enmDrops.size(), equalTo(2));

        List<String> dropNames = Lists.newArrayList();
        for (Drop drop : enmDrops) {
            dropNames.add(drop.getName());
        }
        assertThat(dropNames, containsInAnyOrder("16.13", "16.14"));
    }

    @Test
    public void shouldFindDropsByProductIDAndDropName() {
        Product enm = persistProductWithDrops("ENM");
        Drop drop = dropRepository.findByProductIDAndDropName(enm.getId(), "16.13");
        Assert.assertNotNull(drop);

    }

    private Product persistProductWithDrops(String productName) {
        Product product = TestEntityFactory.buildProduct(productName).build();

        Drop drop1 = TestEntityFactory.buildDrop(product, "16.13").build();
        Drop drop2 = TestEntityFactory.buildDrop(product, "16.14").build();
        persistence.persistInTransaction(product, drop1, drop2);

        return product;
    }
}
