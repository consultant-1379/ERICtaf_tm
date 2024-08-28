UPDATE `TEST_CASES` n
  JOIN (SELECT tc_id, MIN(id) min_id FROM `TEST_CASES` GROUP BY tc_id HAVING COUNT(*) > 1) d
    ON n.tc_id = d.tc_id AND n.id <> d.min_id
SET n.tc_id = CONCAT(n.tc_id,'_', id);

CREATE UNIQUE INDEX `UK_TEST_CASES` ON `TEST_CASES` (`tc_id` ASC);