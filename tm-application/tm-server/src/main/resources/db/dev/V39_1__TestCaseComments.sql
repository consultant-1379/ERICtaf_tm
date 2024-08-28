-- Test data to check Test case comments. See DURACI-2974

INSERT INTO `TEST_CASES` (`id`, `deleted`, `tc_id`, `updated_by_user_id`) VALUES
  (25, 0, 'TestCase_for_comments_test_01', 1);
INSERT INTO `TEST_CASES` (`id`, `deleted`, `tc_id`, `updated_by_user_id`) VALUES
  (26, 0, 'TestCase_for_comments_with_comments_02', 1);


INSERT INTO `TEST_CASE_VERSIONS` (id, deleted, automation_candidate, comment, description, execution_type, precondition, priority, sequence_number, technical_component_id, test_case_id, test_case_status, title, type) VALUES
  (26, 0, 2, 'Comments: TestCase_for_comments. No comments 01', 'Comments', 2, 'For comments', 'MEDIUM', 1, 22, 25, 2, 'testDefaultTest', 'FUNCTIONAL');
INSERT INTO `TEST_CASE_VERSIONS` (id, deleted, automation_candidate, comment, description, execution_type, precondition, priority, sequence_number, technical_component_id, test_case_id, test_case_status, title, type) VALUES
  (27, 0, 2, 'Comments: TestCase_for_comments. Exist comments 02', 'Predefined comments', 2, 'Predefined comments', 'MEDIUM', 1, 22, 26, 2, 'testDefaultTest', 'FUNCTIONAL');


UPDATE `TEST_CASES` SET current_version_id = 26 WHERE id = 25;
UPDATE `TEST_CASES` SET current_version_id = 27 WHERE id = 26;



INSERT INTO `REQUIREMENT_TEST_CASES`(`requirement_id`, `test_case_id`) VALUES
  (4, 26);
INSERT INTO `REQUIREMENT_TEST_CASES`(`requirement_id`, `test_case_id`) VALUES
  (1, 27);

INSERT INTO `POSTS`(`message`, `author_id`, `object_name_ref`, `object_id`) VALUES
  ('Test comment 01', 1, 1, 26);
INSERT INTO `POSTS`(`message`, `author_id`, `object_name_ref`, `object_id`) VALUES
  ('Test comment 02', 1, 1, 26);
