ALTER TABLE `PRODUCTS` ADD `trs_recordable` BIT DEFAULT FALSE;
ALTER TABLE `PRODUCTS_AUD` ADD `trs_recordable` BIT DEFAULT FALSE;

ALTER TABLE `TEST_EXECUTIONS` ADD `recorded_in_trs` BIT DEFAULT FALSE;
ALTER TABLE `TEST_EXECUTIONS_AUD` ADD `recorded_in_trs` BIT DEFAULT FALSE;

ALTER TABLE `TEST_PLANS` ADD `parent_id` BIGINT(20) DEFAULT NULL AFTER `id`;
ALTER TABLE `TEST_PLANS_AUD` ADD `parent_id` BIGINT(20) DEFAULT NULL AFTER `id`;

ALTER TABLE `TEST_PLANS` ADD `trs_record_id` BIGINT(20) DEFAULT NULL;
ALTER TABLE `TEST_PLANS_AUD` ADD `trs_record_id` BIGINT(20) DEFAULT NULL;

CREATE TABLE IF NOT EXISTS `TRS_JOBS` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `job_name` VARCHAR(255) NOT NULL,
  `job_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `TRS_JOBS_AUD` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `job_name` VARCHAR(255) NOT NULL,
  `job_id` VARCHAR(255) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TRS_JOBS_AUD_AUDIT_REVISION_ENTITY` FOREIGN KEY (`REV`) REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS `TRS_SESSIONS` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `session_id` VARCHAR(255) NOT NULL,
  `iso_id` BIGINT(20) NOT NULL,
  `trs_job_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TRS_SESSIONS_ISO` (`iso_id`),
  CONSTRAINT `FK_TRS_SESSIONS_ISO` FOREIGN KEY (`iso_id`) REFERENCES `ISOS` (`id`),
  KEY `FK_TRS_SESSIONS_TRS_JOB` (`trs_job_id`),
  CONSTRAINT `FK_TRS_SESSIONS_TRS_JOB` FOREIGN KEY (`trs_job_id`) REFERENCES `TRS_JOBS` (`id`),
  UNIQUE KEY `UK_ISO_TRS_JOB` (`iso_id`, `trs_job_id`)
);

CREATE TABLE IF NOT EXISTS `TRS_SESSIONS_AUD` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `session_id` VARCHAR(255) NOT NULL,
  `iso_id` BIGINT(20) NOT NULL,
  `trs_job_id` BIGINT(20) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TRS_SESSIONS_AUD_AUDIT_REVISION_ENTITY` FOREIGN KEY (`REV`) REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS `TRS_RESULTS` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `test_case_id` BIGINT(20) NOT NULL,
  `trs_result_id` VARCHAR(255) NOT NULL,
  `trs_session_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TRS_RESULTS_TEST_CASE` (`test_case_id`),
  CONSTRAINT `FK_TRS_RESULTS_TEST_CASE` FOREIGN KEY (`test_case_id`) REFERENCES `TEST_CASES` (`id`),
  KEY `FK_TRS_RESULTS_TRS_SESSION` (`trs_session_id`),
  CONSTRAINT `FK_TRS_RESULTS_TRS_SESSION` FOREIGN KEY (`trs_session_id`) REFERENCES `TRS_SESSIONS` (`id`),
  UNIQUE KEY `UK_TEST_CASE_ID_TRS_SESSION` (`test_case_id`, trs_session_id)
);

CREATE TABLE IF NOT EXISTS `TRS_RESULTS_AUD` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `test_case_id` BIGINT(20) NOT NULL,
  `trs_result_id` VARCHAR(255) NOT NULL,
  `trs_session_id` BIGINT(20) NOT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TRS_RESULTS_AUD_AUDIT_REVISION_ENTITY` FOREIGN KEY (`REV`) REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);
