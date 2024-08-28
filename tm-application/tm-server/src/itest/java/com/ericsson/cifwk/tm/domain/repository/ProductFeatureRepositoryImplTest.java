package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.google.common.collect.Sets;
import org.junit.Test;

import javax.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildFeature;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildProduct;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ProductFeatureRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private ProductFeatureRepository featureRepository;

    @Test
    public void shouldRetreiveFeaturesByProduct() {
        Product enm = persistProductWithFeatures("ENM");

        List<ProductFeature> enmFeatures = featureRepository.findByProduct(enm.getId());
        assertThat(enmFeatures.size(), equalTo(2));

        List<String> enmFeatureNames = enmFeatures.stream()
                .map(f -> f.getName())
                .collect(Collectors.toList());

        assertThat(enmFeatureNames, containsInAnyOrder("ENM_FM", "ENM_PM"));

        Product oss = persistProductWithFeatures("OSS");

        List<ProductFeature> ossFeatures = featureRepository.findByProduct(oss.getId());
        assertThat(ossFeatures.size(), equalTo(2));

        List<String> ossFeatureNames = ossFeatures.stream()
                .map(f -> f.getName())
                .collect(Collectors.toList());

        assertThat(ossFeatureNames, containsInAnyOrder("OSS_FM", "OSS_PM"));
    }

    @Test
    public void shouldRetrieveFeaturesByProductAndName() {
        Product enm = persistProductWithFeatures("ENM");
        ProductFeature fm = featureRepository.findByProductAndName(enm.getId(), "ENM_FM");

        assertThat(fm.getProduct(), equalTo(enm));
        assertThat(fm.getName(), equalTo("ENM_FM"));
    }

    private Product persistProductWithFeatures(String productName) {
        Product product = buildProduct(productName).build();

        ProductFeature fm = buildFeature(productName + "_FM", product, Sets.newHashSet()).build();
        ProductFeature pm = buildFeature(productName + "_PM", product, Sets.newHashSet()).build();

        product.setFeatures(Sets.newHashSet(fm, pm));
        persistence().persistInTransaction(product);

        return product;
    }
}
