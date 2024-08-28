-- -----------------------------------------------------
-- Table `TEST_TYPE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_TYPE` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted`     BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  `product_id` BIGINT   NOT NULL,
  PRIMARY KEY (`id`),
   KEY `FK_PRODUCT_TEST_TYPE` (`product_id`),
  CONSTRAINT `FK_PRODUCT_TEST_TYPE`
  FOREIGN KEY (`product_id`) REFERENCES `PRODUCTS` (`id`)
   ON DELETE RESTRICT
   ON UPDATE RESTRICT
);

ALTER TABLE `TEST_TYPE` ADD UNIQUE KEY `UK_PRODUCT_TEST_TYPE` (`name`, `product_id`);

-- -----------------------------------------------------
-- Table `TEST_TYPE_AUD`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `TEST_TYPE_AUD` (
  `id` BIGINT,
  `deleted`     BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `product_id` BIGINT NOT NULL,
   PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_PRODUCT_TEST_TYPE_AUD_AUDIT_REVISION_ENTITY`
  FOREIGN KEY (`REV`) REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

ALTER TABLE `TEST_CASE_VERSIONS` ADD COLUMN `test_type_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `TEST_CASE_VERSIONS_AUD` ADD COLUMN `test_type_id` BIGINT NOT NULL DEFAULT 0;
