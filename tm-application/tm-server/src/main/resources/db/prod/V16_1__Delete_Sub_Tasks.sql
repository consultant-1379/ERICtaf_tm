-- delete `asdasd` test case & links

update TEST_CASES set deleted=1 where id = 8;
delete from REQUIREMENT_TEST_CASES where requirement_id = 11;

-- move tc from subtasks to tasks manually
update REQUIREMENT_TEST_CASES set requirement_id = 9727 where requirement_id = 1017;
update REQUIREMENT_TEST_CASES set requirement_id = 9728 where requirement_id = 1016;

-- move tc from subtasks to tasks automatically
update IGNORE REQUIREMENT_TEST_CASES 
	set requirement_id = (select parent_id from REQUIREMENTS where id = requirement_id)
	where requirement_id in (
		select id from REQUIREMENTS where external_type='Sub-task' and parent_id is not null);

-- delete duplicates
delete from REQUIREMENT_TEST_CASES
	where requirement_id in (
		select id from REQUIREMENTS where external_type='Sub-task' and parent_id is not null);
		
-- delete all subtasks
update REQUIREMENTS set deleted = 1 where external_type='Sub-task';