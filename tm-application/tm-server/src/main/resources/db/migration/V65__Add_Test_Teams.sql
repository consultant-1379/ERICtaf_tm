-- -----------------------------------------------------
-- Table `TEST_TEAMS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_TEAMS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  `product_feature_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_TEAM_FEATURES`
    FOREIGN KEY (`product_feature_id`)
    REFERENCES `PRODUCT_FEATURES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- -----------------------------------------------------
-- Table `TEST_TEAMS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_TEAMS_AUD` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  `product_feature_id` BIGINT(20) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_TEAM_FEATURES_AUD`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- -----------------------------------------------------
-- Table `TESTS_SUITE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_SUITES` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  `product_feature_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_SUITES_FEATURES`
    FOREIGN KEY (`product_feature_id`)
    REFERENCES `PRODUCT_FEATURES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

-- -----------------------------------------------------
-- Table `TESTS_SUITE_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_SUITES_AUD` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  `product_feature_id` BIGINT(20) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_SUITE_FEATURES_AUD`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

ALTER TABLE `TEST_CASE_VERSIONS` ADD COLUMN `test_team_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `TEST_CASE_VERSIONS_AUD` ADD COLUMN `test_team_id` BIGINT  NULL DEFAULT NULL;
ALTER TABLE `TEST_CASE_VERSIONS` ADD COLUMN `test_suite_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `TEST_CASE_VERSIONS_AUD` ADD COLUMN `test_suite_id` BIGINT NULL DEFAULT NULL;

-- USER_PROFILES` add admin
ALTER TABLE `USER_PROFILES` ADD COLUMN `administrator` BOOLEAN NULL DEFAULT FALSE;


