-- -----------------------------------------------------
-- Table `Test Campaign Group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGN_GROUP` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  `product_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_CAMPAIGN_GROUP_PRODUCTS`
    FOREIGN KEY (`product_id`)
    REFERENCES `PRODUCTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);


-- -----------------------------------------------------
-- Table `Test Campaign Group AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGN_GROUP_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `product_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_CAMPAIGN_GROUP_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE INDEX `FK_TEST_CAMPAIGN_GROUP_AUD_AUDIT_REVISION_ENTITY_idx` ON `TEST_CAMPAIGN_GROUP_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS` (
  `test_campaign_group_id` BIGINT NOT NULL,
  `test_campaign_id` BIGINT NOT NULL,
  PRIMARY KEY (`test_campaign_group_id`, `test_campaign_id`),
  CONSTRAINT `FK_TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS`
    FOREIGN KEY (`test_campaign_id`)
    REFERENCES `TEST_PLANS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_CAMPAIGN_TEST_CAMPAIGN_GROUP`
    FOREIGN KEY (`test_campaign_group_id`)
    REFERENCES `TEST_CAMPAIGN_GROUP` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS_idx` ON `TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS` (`test_campaign_id` ASC);


-- -----------------------------------------------------
-- Table `TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS_AUD` (
  `REV` INT NOT NULL,
  `test_campaign_group_id` BIGINT NOT NULL,
  `test_campaign_id` BIGINT NOT NULL,
  `REVTYPE` TINYINT NULL,
  PRIMARY KEY (`REV`, `test_campaign_group_id`, `test_campaign_id`),
  CONSTRAINT `FK_TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);