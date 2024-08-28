CREATE TABLE IF NOT EXISTS `PROJECTS` (
  `id` BIGINT AUTO_INCREMENT,
	`external_id` VARCHAR(50) NOT NULL,
	`name` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `external_id` (`external_id`)
);

CREATE TABLE IF NOT EXISTS `PROJECTS_AUD` (
  `id` BIGINT NOT NULL,
  `external_id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `REV` INT NOT NULL,
  `REVTYPE` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_PROJECTS_AUD_AUDIT_REVISION_ENTITY`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

CREATE TABLE IF NOT EXISTS `USER_PROFILES` (
  `id` BIGINT AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `project_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id` (`user_id`),
  INDEX `FK_USER_PROFILES_USERS_idx` (`user_id`),
  INDEX `FK_USER_PROFILES_PROJECTS_idx` (`project_id`),
  CONSTRAINT `FK_USER_PROFILES_USERS` FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`),
  CONSTRAINT `FK_USER_PROFILES_PROJECTS` FOREIGN KEY (`project_id`) REFERENCES `PROJECTS` (`id`)
);

-- Add new column in the table. Change UNIQUE index for the table.
ALTER TABLE `REQUIREMENTS` ADD COLUMN `project_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `REQUIREMENTS` ADD INDEX `FK_REQUIREMENTS_PROJECTS_idx` (`project_id`);
ALTER TABLE `REQUIREMENTS` ADD CONSTRAINT `FK_REQUIREMENTS_PROJECTS` FOREIGN KEY (`project_id`) REFERENCES `PROJECTS` (`id`);

-- Add new column in the audit table.
ALTER TABLE `REQUIREMENTS_AUD` ADD COLUMN `project_id` BIGINT(20) NULL DEFAULT NULL;

-- Add new PROJECTS from JIRA.
INSERT INTO `PROJECTS` (`name`, `external_id`) VALUES
  ('ASSURE', 'EQEV'),
  ('CI_FrameworkTeam_PDUOSS', 'CIP'),
  ('DURA CI', 'DURACI'),
  ('SimNet Simulated Networks', 'NETSUP'),
  ('OSSRC', 'OSS'),
  ('TOR', 'TOR'),
  ('TOR Deployment', 'TORD'),
  ('TOR Project', 'TORF'),
  ('TOR Security Identity and Access Manager', 'TSIAM'),
  ('TOR Upgrade Manager', 'TORUM'),
  ('TOR_FM', 'TORFM'),
  ('TOR_FT1_ServiceFramework', 'TORSF'),
  ('TOR_FT2_Mediation', 'TT'),
  ('TOR_FT3_UI', 'TORFTUI'),
  ('TOR_FT4_UI_SSO', 'TORFTUISSO'),
  ('TOR_FT5_Streaming', 'TORFTST'),
  ('BSIM/TOR', 'BSI'),
  ('CDS', 'CDS'),
  ('Deployment Management Tool', 'DMT'),
  ('LITP CDS', 'LITPCDS'),
  ('LITP TEST', 'LT'),
  ('PLM & Customer Support', 'PLM'),
  ('SON', 'SON'),
  ('TOR Customer Projects', 'TCP'),
  ('TOR DDC DDP', 'TDDDCDDP'),
  ('TOR PM Initiation', 'TPI'),
  ('TOR Security', 'TS'),
  ('TOR_CM', 'TORCM');
