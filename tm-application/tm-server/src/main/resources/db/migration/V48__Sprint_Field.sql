ALTER TABLE  REQUIREMENTS ADD COLUMN `external_deliveredIn` VARCHAR(255) NULL DEFAULT NULL;
ALTER TABLE  REQUIREMENTS_AUD ADD COLUMN `external_deliveredIn` VARCHAR(255) NULL DEFAULT NULL;

ALTER TABLE  DEFECTS ADD COLUMN `external_deliveredIn` VARCHAR(255) NULL DEFAULT NULL;
ALTER TABLE  DEFECTS_AUD ADD COLUMN `external_deliveredIn` VARCHAR(255) NULL DEFAULT NULL;