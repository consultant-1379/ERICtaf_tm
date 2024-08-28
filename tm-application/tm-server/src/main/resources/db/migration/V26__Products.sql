-- Create products table
CREATE TABLE IF NOT EXISTS `PRODUCTS` (
  `id`          BIGINT       AUTO_INCREMENT,
  `deleted`     BOOLEAN      NULL DEFAULT FALSE,
  `external_id` VARCHAR(255) NOT NULL,
  `name`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `UK_PRODUCTS` ON `PRODUCTS` (`external_id` ASC);

CREATE TABLE IF NOT EXISTS `PRODUCTS_AUD` (
  `id`          BIGINT       NOT NULL,
  `deleted`     BOOLEAN      NULL DEFAULT FALSE,
  `external_id` VARCHAR(255) NOT NULL,
  `name`        VARCHAR(255) NOT NULL,
  `REV`         INT          NOT NULL,
  `REVTYPE`     TINYINT      NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `REV`),
  CONSTRAINT `FK_PRODUCTS_AUD_AUDIT_REVISION_ENTITY`
  FOREIGN KEY (`REV`)
  REFERENCES `AUDIT_REVISION_ENTITY` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

-- Populate products table
INSERT INTO `PRODUCTS` (`id`, `external_id`, `name`) VALUES (1, 'default', '');
INSERT INTO `PRODUCTS` (`id`, `external_id`, `name`) VALUES (2, 'ENM', 'ENM');
INSERT INTO `PRODUCTS` (`id`, `external_id`, `name`) VALUES (3, 'OSSRC', 'OSSRC');
INSERT INTO `PRODUCTS` (`id`, `external_id`, `name`) VALUES (4, 'Eiffel', 'Eiffel');

-- Link projects to products
ALTER TABLE `PROJECTS` ADD COLUMN `product_id` BIGINT NOT NULL DEFAULT 1;
ALTER TABLE `PROJECTS_AUD` ADD COLUMN `product_id` BIGINT;

-- Link ENM projects
UPDATE `PROJECTS`
SET `product_id` = 2
WHERE `external_id` IN (
  'CDS', 'CIP', 'DMT', 'LITPCDS', 'LT', 'NETSUP', 'PLM', 'SON', 'TAFTEST', 'TCP', 'TDDDCDDP', 'TOR', 'TORCM', 'TORD',
  'TORF', 'TORFM', 'TORFTST', 'TORFTUI', 'TORFTUISSO', 'TORRV', 'TORSF', 'TORUM', 'TPI', 'TS', 'TSIAM', 'TT'
);

-- Link OSSRC projects
UPDATE `PROJECTS`
SET `product_id` = 3
WHERE `external_id` IN ('BSI', 'BSIMAUTO', 'DEFTFC', 'EQEV', 'ORA', 'OSS', 'OW', 'PLMEEQV', 'SVCT', 'WLRCM');

-- Link Eiffel projects
UPDATE `PROJECTS`
SET `product_id` = 4
WHERE `external_id` IN ('DURACI');

-- Add projects constraint
ALTER TABLE `PROJECTS` ADD CONSTRAINT `FK_PROJECTS_PRODUCTS`
FOREIGN KEY (`product_id`)
REFERENCES `PRODUCTS` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
