CREATE TABLE IF NOT EXISTS `USERS_AUD` (
  `id`          BIGINT       AUTO_INCREMENT,
  `external_id` VARCHAR(255) NOT NULL,
  `REV`         INT          NOT NULL,
  `REVTYPE`     TINYINT      NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `USERS_AUD_AUDIT_REVISION_ENTITY`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE INDEX `USERS_AUD_AUDIT_REVISION_ENTITY_idx` ON `USERS_AUD` (`REV` ASC);

-- Move test execution audit index
ALTER TABLE `TEST_CASES_AUD` DROP INDEX `TEST_EXECUTIONS_AUD_AUDIT_REVISION_ENTITY_idx`;
CREATE INDEX `TEST_EXECUTIONS_AUD_AUDIT_REVISION_ENTITY_idx` ON `TEST_EXECUTIONS_AUD` (`REV` ASC);
