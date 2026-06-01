-- Force update google.clientId and google.clientSecret parameters if they are empty
UPDATE `sys_params`
SET `param_value` = 'YOUR_GOOGLE_CLIENT_ID'
WHERE `param_code` = 'google.clientId' AND (`param_value` = '' OR `param_value` IS NULL OR `param_value` = 'YOUR_GOOGLE_CLIENT_ID');

UPDATE `sys_params`
SET `param_value` = 'YOUR_GOOGLE_CLIENT_SECRET'
WHERE `param_code` = 'google.clientSecret' AND (`param_value` = '' OR `param_value` IS NULL OR `param_value` = 'YOUR_GOOGLE_CLIENT_SECRET');

