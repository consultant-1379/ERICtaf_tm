Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'Functional' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'FUNCTIONAL';

Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'Performance' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'PERFORMANCE';

Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'Robustness' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'ROBUSTNESS';

Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'Workflow' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'WORKFLOW';

Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'Security' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'SECURITY';

Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'Scalability' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'SCALABILITY';


Update `TEST_CASE_VERSIONS` t set `test_type_id` =
(SELECT id FROM `TEST_TYPE` WHERE name = 'High Availability' AND product_id =
(SELECT product_id FROM `PRODUCT_FEATURES` WHERE id = t.product_feature_id))
WHERE t.type = 'HIGH_AVAILABILITY';

-- remove old column type
ALTER TABLE `TEST_CASE_VERSIONS` DROP COLUMN `type`;
ALTER TABLE `TEST_CASE_VERSIONS_AUD` DROP COLUMN `type`;

