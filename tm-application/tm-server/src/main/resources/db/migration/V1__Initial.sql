-- -----------------------------------------------------
-- Table `AUDIT_REVISION_ENTITY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AUDIT_REVISION_ENTITY` (
  `id` INT AUTO_INCREMENT,
  `timestamp` BIGINT NOT NULL,
  `user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `FEATURES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `FEATURES` (
  `id` BIGINT AUTO_INCREMENT,
  `external_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));

CREATE UNIQUE INDEX `UK_FEATURES` ON `FEATURES` (`external_id` ASC);


-- -----------------------------------------------------
-- Table `USERS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `USERS` (
  `id` BIGINT AUTO_INCREMENT,
  `external_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));

CREATE UNIQUE INDEX `UK_USERS` ON `USERS` (`external_id` ASC);


-- -----------------------------------------------------
-- Table `RELEASES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `RELEASES` (
  `id` BIGINT AUTO_INCREMENT,
  `external_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));

CREATE UNIQUE INDEX `UK_RELEASES` ON `RELEASES` (`external_id` ASC);


-- -----------------------------------------------------
-- Table `REQUIREMENTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `REQUIREMENTS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `external_id` VARCHAR(255) NOT NULL,
  `parent_id` BIGINT NULL DEFAULT NULL,
  `release_id` BIGINT NULL DEFAULT NULL,
  `external_type` VARCHAR(255) NULL DEFAULT NULL,
  `external_title` VARCHAR(255) NULL DEFAULT NULL,
  `external_summary` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_REQUIREMENTS_REQUIREMENTS`
    FOREIGN KEY (`parent_id`)
    REFERENCES `REQUIREMENTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_REQUIREMENTS_RELEASES`
    FOREIGN KEY (`release_id`)
    REFERENCES `RELEASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE UNIQUE INDEX `UK_REQUIREMENTS` ON `REQUIREMENTS` (`external_id` ASC);

CREATE INDEX `FK_REQUIREMENTS_REQUIREMENTS_idx` ON `REQUIREMENTS` (`parent_id` ASC);

CREATE INDEX `FK_REQUIREMENTS_RELEASES_idx` ON `REQUIREMENTS` (`release_id` ASC);


-- -----------------------------------------------------
-- Table `IMPORTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IMPORTS` (
  `id` BIGINT AUTO_INCREMENT,
  `created_at` TIMESTAMP NOT NULL,
  `result` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `author_id` BIGINT NULL DEFAULT NULL,
  `requirement_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_IMPORTS_USERS`
    FOREIGN KEY (`author_id`)
    REFERENCES `USERS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_IMPORTS_REQUIREMENTS`
    FOREIGN KEY (`requirement_id`)
    REFERENCES `REQUIREMENTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_IMPORTS_USERS_idx` ON `IMPORTS` (`author_id` ASC);

CREATE INDEX `FK_IMPORTS_REQUIREMENTS_idx` ON `IMPORTS` (`requirement_id` ASC);


-- -----------------------------------------------------
-- Table `TECHNICAL_COMPONENTS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TECHNICAL_COMPONENTS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `TEST_CASES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CASES` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `tc_id` VARCHAR(255) NOT NULL,
  `description` LONGTEXT NULL DEFAULT NULL,
  `priority` VARCHAR(255) NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `type` VARCHAR(255) NOT NULL,
  `requirement_id` BIGINT NULL DEFAULT NULL,
  `technical_component_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_CASES_REQUIREMENTS`
    FOREIGN KEY (`requirement_id`)
    REFERENCES `REQUIREMENTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_CASES_TECHNICAL_COMPONENTS`
    FOREIGN KEY (`technical_component_id`)
    REFERENCES `TECHNICAL_COMPONENTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE UNIQUE INDEX `UK_TEST_CASES` ON `TEST_CASES` (`tc_id` ASC, `type` ASC, `requirement_id` ASC);

CREATE INDEX `FK_TEST_CASES_REQUIREMENTS_idx` ON `TEST_CASES` (`requirement_id` ASC);

CREATE INDEX `FK_TEST_CASES_TECHNICAL_COMPONENTS_idx` ON `TEST_CASES` (`technical_component_id` ASC);


-- -----------------------------------------------------
-- Table `IMPORT_TEST_CASES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `IMPORT_TEST_CASES` (
  `import_id` BIGINT NOT NULL,
  `test_case_id` BIGINT NOT NULL,
  PRIMARY KEY (`import_id`, `test_case_id`),
  CONSTRAINT `FK_IMPORT_TEST_CASES`
    FOREIGN KEY (`test_case_id`)
    REFERENCES `TEST_CASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_CASE_IMPORTS`
    FOREIGN KEY (`import_id`)
    REFERENCES `IMPORTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_IMPORT_TEST_CASES_idx` ON `IMPORT_TEST_CASES` (`test_case_id` ASC);


-- -----------------------------------------------------
-- Table `REQUIREMENTS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `REQUIREMENTS_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `external_id` VARCHAR(255) NULL DEFAULT NULL,
  `parent_id` BIGINT NULL DEFAULT NULL,
  `release_id` BIGINT NULL DEFAULT NULL,
  `external_type` VARCHAR(255) NULL DEFAULT NULL,
  `external_title` VARCHAR(255) NULL DEFAULT NULL,
  `external_summary` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_REQUIREMENTS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_REQUIREMENTS_AUD_AUDIT_REVISION_ENTITY_idx` ON `REQUIREMENTS_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `REQUIREMENT_FEATURES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `REQUIREMENT_FEATURES` (
  `requirement_id` BIGINT NOT NULL,
  `feature_id` BIGINT NOT NULL,
  PRIMARY KEY (`requirement_id`, `feature_id`),
  CONSTRAINT `FK_REQUIREMENT_FEATURES`
    FOREIGN KEY (`feature_id`)
    REFERENCES `FEATURES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_FEATURE_REQUIREMENTS`
    FOREIGN KEY (`requirement_id`)
    REFERENCES `REQUIREMENTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_REQUIREMENT_FEATURES_idx` ON `REQUIREMENT_FEATURES` (`feature_id` ASC);


-- -----------------------------------------------------
-- Table `REQUIREMENT_FEATURES_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `REQUIREMENT_FEATURES_AUD` (
  `REV` INT NOT NULL,
  `requirement_id` BIGINT NOT NULL,
  `feature_id` BIGINT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`REV`, `requirement_id`, `feature_id`),
  CONSTRAINT `FK_REQUIREMENT_FEATURES_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);


-- -----------------------------------------------------
-- Table `SCOPES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SCOPES` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));

CREATE UNIQUE INDEX `UK_SCOPES` ON `SCOPES` (`name` ASC);


-- -----------------------------------------------------
-- Table `SCOPES_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SCOPES_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_SCOPES_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_SCOPES_AUD_AUDIT_REVISION_ENTITY_idx` ON `SCOPES_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `SCOPE_TEST_CASES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SCOPE_TEST_CASES` (
  `scope_id` BIGINT NOT NULL,
  `test_case_id` BIGINT NOT NULL,
  PRIMARY KEY (`scope_id`, `test_case_id`),
  CONSTRAINT `FK_SCOPE_TEST_CASES`
    FOREIGN KEY (`test_case_id`)
    REFERENCES `TEST_CASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_CASE_SCOPES`
    FOREIGN KEY (`scope_id`)
    REFERENCES `SCOPES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_SCOPE_TEST_CASES_idx` ON `SCOPE_TEST_CASES` (`test_case_id` ASC);


-- -----------------------------------------------------
-- Table `SCOPE_TEST_CASES_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SCOPE_TEST_CASES_AUD` (
  `REV` INT NOT NULL,
  `scope_id` BIGINT NOT NULL,
  `test_case_id` BIGINT NOT NULL,
  `REVTYPE` TINYINT NULL,
  PRIMARY KEY (`REV`, `scope_id`, `test_case_id`),
  CONSTRAINT `FK_SCOPE_TESTS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);


-- -----------------------------------------------------
-- Table `TECHNICAL_COMPONENTS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TECHNICAL_COMPONENTS_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TECHNICAL_COMPONENTS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TECHNICAL_COMPONENT_AUD_AUDIT_REVISION_ENTITY_idx` ON `TECHNICAL_COMPONENTS_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `TEST_CASES_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_CASES_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `tc_id` VARCHAR(255) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `description` LONGTEXT NULL DEFAULT NULL,
  `priority` VARCHAR(255) NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `requirement_id` BIGINT NULL DEFAULT NULL,
  `technical_component_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_CASES_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_CASES_AUD_AUDIT_REVISION_ENTITY_idx` ON `TEST_CASES_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `TEST_FIELDS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_FIELDS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `field` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `value` VARCHAR(255) NULL DEFAULT NULL,
  `test_case_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_FIELDS_TEST_CASES`
    FOREIGN KEY (`test_case_id`)
    REFERENCES `TEST_CASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_FIELDS_TEST_CASES_idx` ON `TEST_FIELDS` (`test_case_id` ASC);


-- -----------------------------------------------------
-- Table `TEST_FIELDS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_FIELDS_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `field` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `value` VARCHAR(255) NULL DEFAULT NULL,
  `test_case_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_FIELDS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_FIELDS_AUD_AUDIT_REVISION_ENTITY_idx` ON `TEST_FIELDS_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `TEST_LINKS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_LINKS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `type` VARCHAR(255) NOT NULL,
  `from_id` BIGINT NOT NULL,
  `to_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_LINKS_TEST_CASES_FROM`
    FOREIGN KEY (`from_id`)
    REFERENCES `TEST_CASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_LINKS_TEST_CASES_TO`
    FOREIGN KEY (`to_id`)
    REFERENCES `TEST_CASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_LINKS_TEST_CASES_FROM_idx` ON `TEST_LINKS` (`from_id` ASC);

CREATE INDEX `FK_TEST_LINKS_TEST_CASES_TO_idx` ON `TEST_LINKS` (`to_id` ASC);


-- -----------------------------------------------------
-- Table `TEST_LINKS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_LINKS_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `from_id` BIGINT NULL DEFAULT NULL,
  `to_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_LINKS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_LINKS_AUD_AUDIT_REVISION_ENTITY_idx` ON `TEST_LINKS_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `TEST_STEPS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_STEPS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `execute` LONGTEXT NOT NULL,
  `sequence_order` INT NOT NULL,
  `test_case_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_STEPS_TEST_CASES`
    FOREIGN KEY (`test_case_id`)
    REFERENCES `TEST_CASES` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_STEPS_TEST_CASES_idx` ON `TEST_STEPS` (`test_case_id` ASC);


-- -----------------------------------------------------
-- Table `TEST_STEPS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TEST_STEPS_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `execute` LONGTEXT NULL DEFAULT NULL,
  `sequence_order` INT NULL DEFAULT NULL,
  `test_case_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_STEPS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_TEST_STEPS_AUD_AUDIT_REVISION_ENTITY_idx` ON `TEST_STEPS_AUD` (`REV` ASC);


-- -----------------------------------------------------
-- Table `VERIFY_STEPS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VERIFY_STEPS` (
  `id` BIGINT AUTO_INCREMENT,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `verify_step` LONGTEXT NOT NULL,
  `sequence_order` INT NOT NULL DEFAULT 0,
  `test_step_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_VERIFY_STEPS_TEST_STEPS`
    FOREIGN KEY (`test_step_id`)
    REFERENCES `TEST_STEPS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_VERIFY_STEPS_TEST_STEPS_idx` ON `VERIFY_STEPS` (`test_step_id` ASC);


-- -----------------------------------------------------
-- Table `VERIFY_STEPS_AUD`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VERIFY_STEPS_AUD` (
  `id` BIGINT NOT NULL,
  `deleted` BOOLEAN NULL DEFAULT FALSE,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  `verify_step` LONGTEXT NULL DEFAULT NULL,
  `sequence_order` INT NOT NULL DEFAULT 0,
  `test_step_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_VERIFY_STEPS_AUD_AUDIT_REVISION_ENTITY`
    FOREIGN KEY (`REV`)
    REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_VERIFY_STEPS_AUD_AUDIT_REVISION_ENTITY_idx` ON `VERIFY_STEPS_AUD` (`REV` ASC);

-- -- initial data update after migration
-- update test_cases set type = 'FUNCTIONAL' where type like 'Func%';
-- update test_cases set type = 'PERFORMANCE' where type like 'Perf%';
-- update test_cases set type = 'ROBUSTNESS' where type like 'Robu%';
-- update test_cases set type = 'HIGH_AVAILABILITY' where type like 'High%';
-- update test_cases set type = 'SECURITY' where type like 'Secu%';
-- update test_cases set type = 'WORKFLOW' where type like 'Work%';
-- update test_cases set type = 'SCALABILITY' where type like 'Scal%';


-- -----------------------------------------------------
-- Table `USER_SESSIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `USER_SESSIONS` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `session_id` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`));
