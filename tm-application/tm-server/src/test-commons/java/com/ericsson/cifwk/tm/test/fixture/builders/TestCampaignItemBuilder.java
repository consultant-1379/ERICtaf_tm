package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.users.User;

public class TestCampaignItemBuilder extends EntityBuilder<TestCampaignItem> {

    public TestCampaignItemBuilder() {
        super(new TestCampaignItem());
    }

    public TestCampaignItemBuilder withUser(User user) {
        entity.setUser(user);
        return this;
    }

    public TestCampaignItemBuilder withTestPlan(TestCampaign testCampaign) {
        entity.setTestCampaign(testCampaign);
        return this;
    }

    public TestCampaignItemBuilder withTestCaseVersion(TestCaseVersion testCaseVersion) {
        entity.setTestCaseVersion(testCaseVersion);
        return this;
    }

    public TestCampaignItemBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }
}
