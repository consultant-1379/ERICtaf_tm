-- Test data for local testing environment (H2)

INSERT INTO `TEST_PLANS` (`id`, `name`, `description`, `release_number`, `project_id`, `environment`) VALUES
(1, 'Test Plan #1', 'Test plan from V6_1 Migration (No project)', '1.0.1', NULL, 'test'),
(2, 'Test Plan #2', 'Test plan with 3 test cases which are not executed (No project)', '1.1.0', NULL, 'test'),
(3, 'Test Plan #3', 'Test plan with successful execution (No project)', '2.0.0', NULL, 'test'),
(4, 'Test Plan #4', 'Test Plan without Project and Release', NULL, NULL, 'test'),
(5, 'Test Plan #5', '(CI_PDUOSS) Test plan with selected Project from CIP project', '2.0.0', 2, 'test'),
(6, 'Test Plan #6', '(Assure) Test plan to test defects in the test execution', '1.0.0', 1, 'test'),
(7, 'Test Plan #7', '(Assure) Test plan with passed test case', '1.0.1', 1, 'test'),
(8, 'Test Plan #8', '(Assure) Test plan with failed test case', '1.0.2', 1, 'test'),
(9, 'Test Plan #9', '(Assure) Test plan without test cases', '1.0.2', 1, 'test');
