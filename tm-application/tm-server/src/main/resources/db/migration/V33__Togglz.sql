-- Creates table for feature toggle management as required by org.togglz.core.repository.jdbc.JDBCStateRepository
CREATE TABLE IF NOT EXISTS `FEATURE_TOGGLES` (
  `feature_name`    VARCHAR(100) NOT NULL PRIMARY KEY,
  `feature_enabled` BOOLEAN NOT NULL DEFAULT FALSE,
  `strategy_id`     VARCHAR(200),
  `strategy_params` VARCHAR(2000)
)