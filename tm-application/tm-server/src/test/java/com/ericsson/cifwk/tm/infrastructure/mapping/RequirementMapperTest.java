package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.RequirementView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RequirementMapperTest {

    private RequirementMapper requirementMapper;

    @Mock
    ProjectMapper projectMapper;

    @Before
    public void setUp() {
        requirementMapper = new RequirementMapper(null, projectMapper);
    }

    @Test
    public void testRequirementInfoTreeList() {
        Requirement requirement = getRequirement(1);
        requirement.addChild(getRequirement(2));

        Requirement requirement3 = getRequirement(3);
        requirement3.addChild(getRequirement(4));
        requirement3.addChild(getRequirement(5));

        requirement.addChild(requirement3);

        RequirementInfo dto =
                requirementMapper.mapEntity(requirement, RequirementInfo.class, RequirementView.Tree.class);

        assertThat(dto.getId(), equalTo(requirement.getId()));
        assertThat(dto.getLabel(), equalTo(requirement.getExternalLabel()));
        assertThat(dto.getSummary(), equalTo(requirement.getExternalSummary()));
        assertThat(dto.getType(), equalTo(requirement.getExternalType()));
        assertThat(dto.getExternalStatusName(), equalTo(requirement.getExternalStatusName()));

        Set<RequirementInfo> children = dto.getChildren();
        Iterator<RequirementInfo> childrenIterator = children.iterator();

        assertThat(children, hasSize(2));
        assertThat(childrenIterator.next().getId(), equalTo(2L));

        RequirementInfo dto3 = childrenIterator.next();

        assertThat(dto3.getId(), equalTo(3L));
        assertThat(dto3.getChildren().size(), equalTo(2));
    }

    @Test
    public void testRequirementTreeOrdering() {
        Requirement root = getRequirement(1);
        Requirement bb = getRequirement(2, "bb");
        root.addChild(bb);
        Requirement aa = getRequirement(3, "aa");
        root.addChild(aa);
        Requirement cc = getRequirement(4, "cc");
        root.addChild(cc);
        Requirement bbb = getRequirement(5, "bbb");
        bb.addChild(bbb);
        Requirement aaa = getRequirement(6, "aaa");
        bb.addChild(aaa);

        RequirementInfo dto =
                requirementMapper.mapEntity(root, RequirementInfo.class, RequirementView.Tree.class);

        Set<RequirementInfo> children1 = dto.getChildren();

        assertThat(children1.size(), equalTo(3));

        Iterator<RequirementInfo> iterator1 = children1.iterator();
        RequirementInfo node1 = iterator1.next();
        RequirementInfo node2 = iterator1.next();
        RequirementInfo node3 = iterator1.next();

        assertThat(node1.getExternalId(), equalTo("aa"));
        assertThat(node2.getExternalId(), equalTo("bb"));
        assertThat(node3.getExternalId(), equalTo("cc"));

        Set<RequirementInfo> children2 = node2.getChildren();

        assertThat(children2.size(), equalTo(2));

        Iterator<RequirementInfo> iterator2 = children2.iterator();
        RequirementInfo node21 = iterator2.next();
        RequirementInfo node22 = iterator2.next();

        assertThat(node21.getExternalId(), equalTo("aaa"));
        assertThat(node22.getExternalId(), equalTo("bbb"));
    }

    private Requirement getRequirement(int id) {
        return getRequirement(id, "id" + id);
    }

    private Requirement getRequirement(int id, String externalId) {
        Requirement result = new Requirement(String.valueOf(id));
        result.setId((long) id);
        result.setExternalId(externalId);
        result.setExternalSummary("summary" + id);
        result.setExternalLabel("title" + id);
        result.setExternalType("type" + id);
        result.setExternalStatusName("externalStatusName" + id);
        return result;
    }

}
