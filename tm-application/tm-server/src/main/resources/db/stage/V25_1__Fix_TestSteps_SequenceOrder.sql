UPDATE TEST_STEPS ts SET ts.sequence_order = ts.id
WHERE ts.sequence_order = 0;

UPDATE VERIFY_STEPS vs SET vs.sequence_order = vs.id
WHERE vs.sequence_order = 0;
