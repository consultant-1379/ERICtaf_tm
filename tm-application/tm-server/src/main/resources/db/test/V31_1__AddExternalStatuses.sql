UPDATE `REQUIREMENTS` SET `external_statusname` = 'Done' WHERE id = 2;
UPDATE `REQUIREMENTS` SET `external_statusname` = 'In progress' WHERE id = 3;
UPDATE `REQUIREMENTS` SET `external_statusname` = 'New' WHERE id = 4;
UPDATE `REQUIREMENTS` SET `external_statusname` = 'Closed' WHERE id = 5;
UPDATE `REQUIREMENTS` SET `external_statusname` = 'Open' WHERE id = 6;

INSERT INTO `TEST_CASES` (`id`, `tc_id`, `deleted`, `updated_by_user_id`) VALUES
(23, 'TestCase_with_ListOfRequirements_1', 0, 1),
(24, 'TestCase_with_ListOfRequirements_2', 0, 1);

INSERT INTO `TEST_CASE_VERSIONS` (id, deleted, automation_candidate, comment, description, execution_type, precondition, priority, sequence_number, technical_component_id, test_case_id, test_case_status, title, type) VALUES
(24, 0, 2, 'Jira Status check, TEST DATA', '4 requirements assigned', 2, 'Test', 'MEDIUM', 1, 1, 23, 1, 'testDefaultTest', 'FUNCTIONAL'),
(25, 0, 2, 'Jira Status check, TEST DATA', '3 requirements assigned', 2, 'Test', 'MEDIUM', 1, 1, 24, 3, 'testDefaultTest', 'FUNCTIONAL');

UPDATE `TEST_CASES` SET `current_version_id` = 24 WHERE `id` = 23;
UPDATE `TEST_CASES` SET `current_version_id` = 25 WHERE `id` = 24;

INSERT INTO `REQUIREMENTS` (`id`, `deleted`, `external_id`, `parent_id`, `release_id`, `external_type`, `external_title`, `external_summary`, `project_id`, `external_statusname`) VALUES
(7, 0, 'CIP-7777', NULL, NULL, 'Story', 'CIP-7777', 'Jira Status check, TEST DATA', 2, 'Resolved'),
(8, 0, 'CIP-8888', NULL, NULL, 'Story', 'CIP-8888', 'Jira Status check, TEST DATA', 2, '');

INSERT INTO `REQUIREMENT_TEST_CASES` (`requirement_id`, `test_case_id`) VALUES
(2, 23),
(7, 24),
(2, 24),
(3, 24),
(3, 25),
(6, 25),
(7, 25),
(8, 25);
