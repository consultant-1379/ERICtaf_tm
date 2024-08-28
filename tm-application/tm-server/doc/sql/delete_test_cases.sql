-- Safe delete test cases with all references. Replace "643,644,645,646,647,544" to required test cases.

delete from requirement_test_cases where test_case_id in (643,644,645,646,647,544);

delete from scope_test_cases where test_case_id in (643,644,645,646,647,544);

delete from test_fields where test_case_id in (643,644,645,646,647,544);

delete from verify_steps where test_step_id in (select id from test_steps where test_case_id in (643,644,645,646,647,544));

delete from test_steps where test_case_id in (643,644,645,646,647,544);

delete from test_cases where id in (643,644,645,646,647,544);