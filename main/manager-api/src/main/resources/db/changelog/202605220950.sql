-- Update Google Client ID with actual user credentials
UPDATE `sys_params` 
SET `param_value` = 'YOUR_GOOGLE_CLIENT_ID' 
WHERE `param_code` = 'google.clientId';

-- Add Google Client Secret parameter
DELETE FROM `sys_params` WHERE `param_code` = 'google.clientSecret';
INSERT INTO `sys_params` (id, param_code, param_value, value_type, param_type, remark, creator, create_date, updater, update_date)
VALUES (702, 'google.clientSecret', 'YOUR_GOOGLE_CLIENT_SECRET', 'string', 1, 'Google OAuth Client Secret', 1, NOW(), 1, NOW());

