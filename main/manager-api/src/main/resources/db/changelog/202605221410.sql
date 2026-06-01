-- Ensure SM2 key pair rows exist in sys_params table.
-- Without these rows, initSM2KeyPair() UPDATE cannot set the keys (no row to update).
-- INSERT IGNORE is safe: skips if the row already exists with a valid key.
INSERT IGNORE INTO `sys_params` (id, param_code, param_value, value_type, param_type, remark, creator, create_date, updater, update_date)
VALUES
    (800, 'server.public_key', 'null', 'string', 1, 'SM2公钥（自动生成）', 1, NOW(), 1, NOW()),
    (801, 'server.private_key', 'null', 'string', 1, 'SM2私钥（自动生成）', 1, NOW(), 1, NOW());
