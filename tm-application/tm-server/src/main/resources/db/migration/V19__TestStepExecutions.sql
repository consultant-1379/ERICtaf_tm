ALTER TABLE `TEST_STEPS` DROP `result`;

CREATE TABLE IF NOT EXISTS `TEST_STEP_EXECUTIONS` (
  `id`                  BIGINT      AUTO_INCREMENT,
  `test_step_id`        BIGINT      NOT NULL,
  `test_execution_id`   BIGINT      NOT NULL,
  `created_at`          TIMESTAMP   NOT NULL,
  `result`              TINYINT     NOT NULL,
  `data`                LONGTEXT    NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_TEST_STEPS`
  FOREIGN KEY (`test_step_id`)
  REFERENCES `TEST_STEPS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `FK_TEST_EXECUTIONS`
  FOREIGN KEY (`test_execution_id`)
  REFERENCES `TEST_EXECUTIONS` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS `TEST_STEP_EXECUTIONS_AUD` (
  `id`                  BIGINT      AUTO_INCREMENT,
  `test_step_id`        BIGINT      NOT NULL,
  `test_execution_id`   BIGINT      NOT NULL,
  `created_at`          TIMESTAMP   NOT NULL,
  `result`              TINYINT     NOT NULL,
  `data`                LONGTEXT    NULL DEFAULT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_TEST_STEP_EXECUTIONS_AUD_AUDIT_REVISION_ENTITY`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);


