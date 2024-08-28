
ALTER TABLE `TEST_CASE_VERSIONS` ADD COLUMN `product_feature_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `TEST_CASE_VERSIONS_AUD` ADD COLUMN `product_feature_id` BIGINT NOT NULL DEFAULT 0;

-- set variables for default values
SELECT `id` INTO @ENM_FEATURE_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Other' AND `product_id` = 2;
SELECT `id` INTO @OSS_FEATURE_ID FROM `PRODUCT_FEATURES` WHERE `name` = 'Other' AND `product_id` = 3;
SELECT `id` INTO @ENM_TECHNICAL_COMPONENTS FROM `TECHNICAL_COMPONENTS` WHERE `name` = 'Other' AND `product_id` = 2;

-- Update feature ids
UPDATE `TEST_CASE_VERSIONS` t SET `product_feature_id` = @ENM_FEATURE_ID WHERE NOT EXISTS
(SELECT id FROM `PRODUCT_FEATURES` WHERE name = 'Other' AND product_id =
(SELECT product_id FROM `PROJECTS` where id =
(SELECT project_id FROM `REQUIREMENTS` where id =
(SELECT requirement_id FROM `REQUIREMENT_TEST_CASES` where test_case_id = t.id LIMIT 1))));

UPDATE `TEST_CASE_VERSIONS` t SET `product_feature_id` =
(SELECT id FROM `PRODUCT_FEATURES` WHERE name = 'Other' AND product_id =
(SELECT product_id FROM `PROJECTS` where id =
(SELECT project_id FROM `REQUIREMENTS` where id =
(SELECT requirement_id FROM `REQUIREMENT_TEST_CASES` where test_case_id = t.id LIMIT 1))))
WHERE t.product_feature_id = 0;

-- technical component ids
UPDATE `TEST_CASE_VERSIONS` t SET `technical_component_id` = @ENM_TECHNICAL_COMPONENTS WHERE NOT EXISTS
(SELECT id FROM `TECHNICAL_COMPONENTS` WHERE name = 'Other' AND product_id =
(SELECT product_id FROM `PROJECTS` where id =
(SELECT project_id FROM `REQUIREMENTS` where id =
(SELECT requirement_id FROM `REQUIREMENT_TEST_CASES` where test_case_id = t.id LIMIT 1))));

UPDATE `TEST_CASE_VERSIONS` t SET `technical_component_id` =
(SELECT id FROM `TECHNICAL_COMPONENTS` WHERE name = 'Other' AND product_id =
(SELECT product_id FROM `PROJECTS` where id =
(SELECT project_id FROM `REQUIREMENTS` where id =
(SELECT requirement_id FROM `REQUIREMENT_TEST_CASES` where test_case_id = t.id LIMIT 1))))
WHERE t.technical_component_id is null;

-- remove product_id from Technical components
ALTER TABLE `TECHNICAL_COMPONENTS` DROP INDEX `UK_TECHNICAL_COMPONENTS`;
ALTER TABLE `TECHNICAL_COMPONENTS` DROP FOREIGN KEY `FK_TECHNICAL_COMPONENTS_PRODUCTS`;
ALTER TABLE `TECHNICAL_COMPONENTS` DROP COLUMN `product_id`;
ALTER TABLE `TECHNICAL_COMPONENTS_AUD` DROP COLUMN `product_id`;

UPDATE `TECHNICAL_COMPONENTS` SET `product_feature_id` = @OSS_FEATURE_ID WHERE product_feature_id = 0;

CREATE UNIQUE INDEX `UK_TECHNICAL_COMPONENTS` ON `TECHNICAL_COMPONENTS` (`name` ASC, `product_feature_id` ASC);
ALTER TABLE `TECHNICAL_COMPONENTS` ADD CONSTRAINT `FK_TECHNICAL_COMPONENTS_PRODUCT_FEATURE` FOREIGN KEY (`product_feature_id`) REFERENCES `PRODUCT_FEATURES` (`id`);

-- add product to user profiles
ALTER TABLE `USER_PROFILES` ADD COLUMN `product_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `USER_PROFILES` ADD CONSTRAINT `FK_USER_PROFILES_PRODUCTS` FOREIGN KEY (`product_id`) REFERENCES `PRODUCTS` (`id`);

-- update fileManagement locations
UPDATE `FILE_STORAGE` set location = REPLACE(location, 'TORF', 'ENM') WHERE location like '%TORF%';
UPDATE `FILE_STORAGE` set location = REPLACE(location, 'PLMOT', 'OSSRC') WHERE location like '%PLMOT%';
UPDATE `FILE_STORAGE` set location = REPLACE(location, 'OSS', 'OSSRC') WHERE location like '%OSS%';

-- remove default product
Update `PRODUCTS` set deleted = 1 WHERE external_id = 'default';

-- set external id the same as name
UPDATE `PRODUCTS` SET `external_id` = 'OSS-RC' WHERE `id` = 3;


