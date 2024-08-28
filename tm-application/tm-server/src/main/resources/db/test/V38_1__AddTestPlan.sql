INSERT INTO `TEST_PLANS` (`id`, `name`, `description`, `release_id`, `project_id`, `environment`) VALUES
(10, 'Test Plan #10', 'Test plan with mixed executions', 4, 2, 'test');



INSERT INTO `TEST_PLAN_ITEMS` (`test_plan_id`, `test_case_id`) VALUES
(10, 1),
(10, 2),
(10, 3),
(10, 4),
(10, 5);


INSERT INTO `TEST_EXECUTIONS` (`id`, `test_plan_id`, `test_case_id`, `created_at`, `author_id`, `result`, `comment`) VALUES
(18, 10, 1, '2014-09-01 16:04:45', 1, 1, 'Test Execution Comment 10.1_1'),
(19, 10, 2, '2014-09-01 16:05:46', 1, 4, 'Test Execution Comment 10.2_1'),
(20, 10, 3, '2014-09-01 16:15:16', 1, 3, 'Test Execution Comment 10.3_1'),
(21, 10, 4, '2014-09-01 16:20:24', 1, 5, 'Test Execution Comment 10.4_1');