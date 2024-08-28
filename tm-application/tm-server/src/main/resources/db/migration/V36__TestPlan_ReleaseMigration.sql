ALTER TABLE `TEST_PLANS`
ADD COLUMN `release_id` BIGINT(20) NULL DEFAULT NULL
AFTER `project_id`;

ALTER TABLE `TEST_PLANS_AUD`
ADD COLUMN `release_id` BIGINT(20) NULL DEFAULT NULL
AFTER `project_id`;

ALTER TABLE `TEST_PLANS`
ADD CONSTRAINT `FK_TEST_PLAN_RELEASE`
FOREIGN KEY (`release_id`)
REFERENCES `RELEASES` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE `RELEASES` DROP INDEX `UK_RELEASES`;
CREATE UNIQUE INDEX `UK_RELEASES` ON `RELEASES` (`external_id`, `product_id`);

-- Migration Script -- START
INSERT INTO RELEASES
(`external_id`, `name`, `product_id`, `migrated`)
  SELECT
    TP.release_number,
    TP.release_number,
    P.product_id,
    TRUE
  FROM TEST_PLANS TP
    LEFT JOIN PROJECTS P ON (P.id = TP.project_id)
  WHERE TP.release_number IS NOT NULL AND TP.release_number != ''
  GROUP BY TP.release_number, P.product_id
  ORDER BY P.product_id ASC, TP.release_number ASC;

-- Update TestPlans with Release and Project
UPDATE TEST_PLANS TP
SET TP.release_id = (
  SELECT R.id FROM PROJECTS P
    INNER JOIN RELEASES R ON (P.product_id = R.product_id)
  WHERE TP.project_id = P.id AND TP.release_number = R.external_id AND TP.release_number IS NOT NULL AND TP.release_number != '' AND TP.project_id IS NOT NULL
)
WHERE TP.release_id IS NULL;

-- Update TestPlans with Release where Project is not set
UPDATE TEST_PLANS as TP
SET TP.release_id = (
  SELECT R.id FROM RELEASES R
  WHERE TP.release_number = R.external_id AND R.product_id IS NULL AND TP.release_number IS NOT NULL AND TP.release_number != '' AND TP.project_id IS NULL
)
WHERE TP.release_id IS NULL;
-- Migration Script -- END

-- Remove old column
ALTER TABLE `TEST_PLANS` DROP COLUMN `release_number`;
ALTER TABLE `TEST_PLANS_AUD` DROP COLUMN `release_number`;
