UPDATE `PROJECTS`
SET `deleted` = true
WHERE `external_id` IN ('TOR', 'TSIAM', 'TORUM', 'TORFM', 'TCP', 'TDDDCDDP');