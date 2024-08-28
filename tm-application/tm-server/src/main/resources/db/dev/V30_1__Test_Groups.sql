-- Delete test groups
DELETE FROM `SCOPE_TEST_CASES`
WHERE `scope_id` IN (1, 2, 3, 4, 5, 6);

DELETE FROM `SCOPES`
WHERE `id` IN (1, 2, 3, 4, 5, 6);
