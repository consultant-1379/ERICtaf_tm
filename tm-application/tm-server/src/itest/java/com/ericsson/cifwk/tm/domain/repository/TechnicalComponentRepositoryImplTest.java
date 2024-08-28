package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildFeature;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildProduct;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TechnicalComponentRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private TechnicalComponentRepository componentRepository;

    @Test
    public void testFindByProductAndFeature() {
        Product product = persistProductWithFeaturesAndComponents();

        ProductFeature fm = getFeatureByName(product, "FM");

        List<TechnicalComponent> fmComponents = componentRepository.findByFeatureIds(Lists.newArrayList(fm.getId()));
        assertThat(fmComponents.size(), equalTo(2));
        List<String> enmFmComponentNames = fmComponents.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        assertThat(enmFmComponentNames, containsInAnyOrder("FM_COMPONENT_1", "FM_COMPONENT_2"));

        ProductFeature pm = getFeatureByName(product, "PM");

        List<TechnicalComponent> pmComponents = componentRepository.findByFeatureIds(Lists.newArrayList(pm.getId()));
        assertThat(pmComponents.size(), equalTo(3));
        List<String> enmPmComponentNames = pmComponents.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        assertThat(enmPmComponentNames, containsInAnyOrder("PM_COMPONENT_1", "PM_COMPONENT_2", "PM_COMPONENT_3"));

        List<TechnicalComponent> nonExistantComponents = componentRepository.findByFeatureIds(Lists.newArrayList(1275689L));
        assertThat(nonExistantComponents.size(), equalTo(0));
    }

    @Test
    public void testFindByIds() {
        Product enm = buildProduct("ENM").build();
        ProductFeature enmFm = buildFeature("FM", enm, Sets.newHashSet()).build();
        Set<TechnicalComponent> fmComponents = buildTechnicalComponents(enm, enmFm, "FM_COMPONENT_1", "FM_COMPONENT_2");
        enmFm.setComponents(fmComponents);
        enm.setFeatures(Sets.newHashSet(enmFm));

        persistence().persistInTransaction(enm);

        List<Long> savedComponentIds = enm.getFeatures().stream()
                .filter(f -> "FM".equals(f.getName()))
                .findAny()
                .get()
                .getComponents()
                .stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());

        List<TechnicalComponent> savedComponents = componentRepository.findByIds(savedComponentIds);

        assertThat(savedComponents.size(), equalTo(2));

        List<String> savedComponentNames = savedComponents.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        assertThat(savedComponentNames, containsInAnyOrder("FM_COMPONENT_1", "FM_COMPONENT_2"));
    }

    private Product persistProductWithFeaturesAndComponents() {
        Product product = buildProduct("ENM").build();

        ProductFeature fm = buildFeature("FM", product, Sets.newHashSet()).build();
        ProductFeature pm = buildFeature("PM", product, Sets.newHashSet()).build();

        Set<TechnicalComponent> fmComponents = buildTechnicalComponents(product, fm, "FM_COMPONENT_1", "FM_COMPONENT_2");
        Set<TechnicalComponent> pmComponents = buildTechnicalComponents(product, pm, "PM_COMPONENT_1", "PM_COMPONENT_2", "PM_COMPONENT_3");

        fm.setComponents(fmComponents);
        pm.setComponents(pmComponents);

        product.setFeatures(Sets.newHashSet(fm, pm));

        persistence().persistInTransaction(product);

        return product;
    }

    private Set<TechnicalComponent> buildTechnicalComponents(Product product, ProductFeature feature, String... names) {
        Set<TechnicalComponent> components = Sets.newHashSet();
        for (String name : names) {
            TechnicalComponent component = new TechnicalComponent(name, feature);
            components.add(component);
        }
        return components;
    }

    private ProductFeature getFeatureByName(Product product, String featureName) {
        return product.getFeatures()
                .stream()
                .filter(f -> featureName.equals(f.getName()))
                .findFirst()
                .get();
    }
}
