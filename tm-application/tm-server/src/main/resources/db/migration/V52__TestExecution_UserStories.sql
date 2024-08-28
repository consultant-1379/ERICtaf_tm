CREATE TABLE IF NOT EXISTS `REQUIREMENTS_TEST_EXECUTIONS` (
  `requirement_id`         BIGINT NOT NULL,
  `test_execution_id` BIGINT NOT NULL,
  PRIMARY KEY (`requirement_id`, `test_execution_id`),
  CONSTRAINT `FK_REQUIREMENTS_TEST_EXECUTION`
  FOREIGN KEY (`test_execution_id`)
  REFERENCES `TEST_EXECUTIONS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_EXECUTION_REQUIREMENTS`
  FOREIGN KEY (`requirement_id`)
  REFERENCES `REQUIREMENTS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);

CREATE INDEX `FK_REQUIREMENTS_TEST_EXECUTIONS_idx` ON `REQUIREMENTS_TEST_EXECUTIONS` (`test_execution_id` ASC);


CREATE TABLE IF NOT EXISTS `REQUIREMENTS_TEST_EXECUTIONS_AUD` (
  `requirement_id`         BIGINT  NOT NULL,
  `test_execution_id` BIGINT  NOT NULL,
  `REV`               INT     NOT NULL,
  `REVTYPE`           TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`requirement_id`, `test_execution_id`, `REV`),
  CONSTRAINT `REQUIREMENTS_TEST_EXECUTIONS_AUD_AUDIT_REVISION_ENTITY`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT);
