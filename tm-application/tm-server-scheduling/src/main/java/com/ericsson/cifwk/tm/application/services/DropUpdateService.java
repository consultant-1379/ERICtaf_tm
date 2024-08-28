package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.DropUpdateServiceImpl;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.google.inject.ImplementedBy;

@ImplementedBy(DropUpdateServiceImpl.class)
public interface DropUpdateService {

    void updateDrops();

    Product save(Product product);

    TestCampaign save(TestCampaign testCampaign);
}
