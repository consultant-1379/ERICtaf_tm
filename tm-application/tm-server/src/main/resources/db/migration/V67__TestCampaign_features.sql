-- -----------------------------------------------------
-- Table `TEST_CAMPAIGN_FEATURES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGN_FEATURES` (
  `id` BIGINT AUTO_INCREMENT,
 `test_campaign_id` BIGINT(20) NOT NULL,
 `feature_id` BIGINT(20) NOT NULL,
 PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_CAMPAIGN_FEATURE`
    FOREIGN KEY (`test_campaign_id`)
    REFERENCES `TEST_PLANS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_FEATURE_TEST_CAMPAIGN`
    FOREIGN KEY (`feature_id`)
    REFERENCES `PRODUCT_FEATURES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);


-- -----------------------------------------------------
-- Table `TEST_CAMPAIGN_FEATURES_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGN_FEATURES_AUD` (
  `id` BIGINT AUTO_INCREMENT,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `test_campaign_id` BIGINT(20) NOT NULL,
  `feature_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_CAMPAIGN_FEATURES_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

INSERT INTO TEST_CAMPAIGN_FEATURES (test_campaign_id, feature_id) SELECT id, product_feature_id FROM TEST_PLANS;

ALTER TABLE `TEST_PLANS` DROP FOREIGN KEY `FK_TEST_PLANS_PRODUCT_FEATURE`;
ALTER TABLE `TEST_PLANS` DROP COLUMN `product_feature_id`;
ALTER TABLE `TEST_PLANS_AUD` DROP COLUMN `product_feature_id`;


ALTER TABLE `TEST_TEAMS` ADD UNIQUE KEY `UK_TEST_TEAMS_FEATURE` (`name`, product_feature_id);
ALTER TABLE `TEST_SUITES` ADD UNIQUE KEY `UK_TEST_SUITES_FEATURE` (`name`, product_feature_id);



