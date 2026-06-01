-- Google OAuth Client ID parameter (leave empty to disable Google OAuth button)
DELETE FROM `sys_params` WHERE `param_code` = 'google.clientId';
INSERT INTO `sys_params` (id, param_code, param_value, value_type, param_type, remark, creator, create_date, updater, update_date)
VALUES (701, 'google.clientId', '', 'string', 1, 'Google OAuth Client ID', 1, NOW(), 1, NOW());
