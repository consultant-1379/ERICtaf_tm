UPDATE `PRODUCTS` SET `name` = 'OSS-RC' WHERE `id` = 3;

CREATE TABLE IF NOT EXISTS `PRODUCT_FEATURES` (
  `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(255) NOT NULL,
  `product_id` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PRODUCT_FEATURE_PRODUCT` (`product_id`),
  CONSTRAINT `FK_PRODUCT_FEATURE_PRODUCT` FOREIGN KEY (`product_id`) REFERENCES `PRODUCTS` (`id`)
);

CREATE TABLE IF NOT EXISTS `PRODUCT_FEATURES_AUD` (
  `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(255) NOT NULL,
  `product_id` BIGINT(20)   NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_PRODUCT_FEATURE_AUD_AUDIT_REVISION_ENTITY` FOREIGN KEY (`REV`) REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGNS_TECHNICAL_COMPONENTS` (
  `id`                      BIGINT(20) NOT NULL AUTO_INCREMENT,
  `test_campaign_id`        BIGINT(20) NOT NULL,
  `technical_component_id`  BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TEST_CAMPAIGNS_TECHNICAL_COMPONENTS_TEST_CAMPAIGN` (`test_campaign_id`),
  CONSTRAINT `FK_TEST_CAMPAIGNS_TECHNICAL_COMPONENTS_TEST_CAMPAIGN` FOREIGN KEY (`test_campaign_id`) REFERENCES `TEST_PLANS` (`id`),
  KEY `FK_TEST_CAMPAIGNS_TECHNICAL_COMPONENTS_COMPONENT` (`technical_component_id`),
  CONSTRAINT `FK_TEST_CAMPAIGNS_TECHNICAL_COMPONENTS_COMPONENT` FOREIGN KEY (`technical_component_id`) REFERENCES `TECHNICAL_COMPONENTS` (`id`),
  UNIQUE KEY `UK_TEST_CAMPAIGN_ID_TECHNICAL_COMPONENT_ID` (`test_campaign_id`, `technical_component_id`)
);

CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGNS_TECHNICAL_COMPONENTS_AUD` (
  `id`                     BIGINT(20) NOT NULL AUTO_INCREMENT,
  `test_campaign_id`       BIGINT(20) NOT NULL,
  `technical_component_id` BIGINT(20) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_CAMPAIGNS_COMPONENTS_AUD_AUDIT_REVISION_ENTITY` FOREIGN KEY (`REV`) REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

ALTER TABLE `PRODUCTS` DROP INDEX `UK_PRODUCTS`;
CREATE UNIQUE INDEX `UK_PRODUCTS_EXTERNAL_ID` ON `PRODUCTS` (`external_id` ASC);
CREATE UNIQUE INDEX `UK_PRODUCTS_NAME` ON `PRODUCTS` (`name` ASC);

ALTER TABLE `TECHNICAL_COMPONENTS` ADD `product_feature_id` BIGINT(20) NOT NULL AFTER `product_id`;
ALTER TABLE `TECHNICAL_COMPONENTS_AUD` ADD `product_feature_id` BIGINT(20) NOT NULL AFTER `product_id`;

ALTER TABLE `TEST_PLANS` ADD `drop_id` BIGINT(20) AFTER `environment`;
ALTER TABLE `TEST_PLANS_AUD` ADD `drop_id` BIGINT(20) AFTER `environment`;

ALTER TABLE `TEST_PLANS` ADD `product_feature_id` BIGINT(20) NOT NULL AFTER `drop_id`;
ALTER TABLE `TEST_PLANS_AUD` ADD `product_feature_id` BIGINT(20) NOT NULL AFTER `drop_id`;

ALTER TABLE `PRODUCTS` ADD `has_drops` BOOLEAN DEFAULT FALSE;
UPDATE `PRODUCTS` SET `has_drops` = TRUE WHERE `external_id` IN ('ENM', 'OSSRC','Ericsson Orchestrator');

ALTER TABLE `PRODUCTS_AUD` ADD `has_drops` BOOLEAN DEFAULT FALSE;
