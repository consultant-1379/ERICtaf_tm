-- Increase timestamp precision fraction to milliseconds
ALTER TABLE `TEST_STEP_EXECUTIONS` MODIFY created_at TIMESTAMP(3) NOT NULL;
ALTER TABLE `TEST_STEP_EXECUTIONS_AUD` MODIFY created_at TIMESTAMP(3) NOT NULL;
ALTER TABLE `USER_SESSIONS` MODIFY created_at TIMESTAMP(3) NOT NULL;
ALTER TABLE `USER_SESSIONS` MODIFY deleted_at TIMESTAMP(3) NULL;
ALTER TABLE `VERIFY_STEP_EXECUTIONS` MODIFY created_at TIMESTAMP(3) NOT NULL;
ALTER TABLE `VERIFY_STEP_EXECUTIONS_AUD` MODIFY created_at TIMESTAMP(3) NOT NULL;
ALTER TABLE `IMPORTS` MODIFY created_at TIMESTAMP(3);
