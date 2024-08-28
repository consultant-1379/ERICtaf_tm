-- update Scopes add Functional Tests, Deployment Validation to ENM
Update SCOPES
SET `product_id` = 2,
`enabled` = TRUE
WHERE `id` = 4 OR `id` = 73;