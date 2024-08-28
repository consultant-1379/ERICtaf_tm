-- Add precondition field to `TEST_CASES`
ALTER TABLE `TEST_CASES` ADD COLUMN `precondition` TEXT NULL DEFAULT NULL;
ALTER TABLE `TEST_CASES_AUD` ADD COLUMN `precondition` TEXT NULL DEFAULT NULL;

-- Migrate preconditions from `TEST_FIELDS` to `TEST_CASES`
UPDATE `TEST_CASES` tc
SET tc.`precondition` = (
  SELECT tf.`value` FROM `TEST_FIELDS` tf
  WHERE tc.`id` = tf.`test_case_id`
    AND tf.`name` = 'PRE'
);

-- Remove preconditions from `TEST_FIELDS`
UPDATE `TEST_FIELDS`
SET `deleted` = TRUE
WHERE `name` = 'PRE'
