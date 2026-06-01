-- Add Web Client to FIRMWARE_TYPE dict data
DELETE FROM `sys_dict_data` WHERE `id` = 101055 OR `dict_value` = 'web-client';
INSERT INTO `sys_dict_data` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `remark`, `sort`, `creator`, `create_date`, `updater`, `update_date`) VALUES
(101055, 101, 'Web Client (Mic / Laptop / Phone)', 'web-client', 'Web Client for Mic / Laptop / Phone', 55, 1, NOW(), 1, NOW());
