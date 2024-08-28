-- Test data to check show/hide "Edit" & "Remove" buttons test case. See DURACI-3636

INSERT INTO `TEST_CASES` (`id`, `deleted`, `tc_id`, `updated_by_user_id`) VALUES
  (22, 0, 'TestCase_with_TwoVersions', 1);

INSERT INTO `TEST_CASE_VERSIONS` (id, deleted, automation_candidate, comment, description, execution_type, precondition, priority, sequence_number, technical_component_id, test_case_id, test_case_status, title, type) VALUES
  (22, 0, 2, 'First edition. Hide/show UI action bar buttons', '1. Hide or show buttons', 2, '1. Button precondition', 'MEDIUM', 1, 1, 22, 2, 'testDefaultTest', 'FUNCTIONAL'),
  (23, 0, 2, 'Second edition. Hide/show UI action bar buttons', '2. Hide or show buttons', 2, '2. Button precondition', 'MEDIUM', 2, 1, 22, 2, 'testDefaultTest', 'FUNCTIONAL');

UPDATE `TEST_CASES` SET current_version_id = 23 WHERE id = 22;

INSERT INTO `REQUIREMENT_TEST_CASES`(`requirement_id`, `test_case_id`) VALUES
  (4, 22);
