-- 对0.3.0版本之前的参数进行修改
update `sys_params` set param_value = '.mp3;.wav;.p3' where  param_code = 'plugins.play_music.music_ext';
update `ai_model_config` set config_json =  '{\"type\": \"intent_llm\", \"llm\": \"LLM_ChatGLMLLM\"}' where  id = 'Intent_intent_llm';

-- 添加edge音色
delete from `ai_tts_voice` where tts_model_id = 'TTS_EdgeTTS';
INSERT INTO `ai_tts_voice` VALUES 
('TTS_EdgeTTS0001', 'TTS_EdgeTTS', 'Edge TTS — Xiaoxiao (nữ)', 'zh-CN-XiaoxiaoNeural', 'tiếng Phổ thông', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0002', 'TTS_EdgeTTS', 'Edge TTS — Yunyang (nam)', 'zh-CN-YunyangNeural', 'tiếng Phổ thông', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0003', 'TTS_EdgeTTS', 'Edge TTS — Xiaoyi (nữ)', 'zh-CN-XiaoyiNeural', 'tiếng Phổ thông', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0004', 'TTS_EdgeTTS', 'Edge TTS — Yunjian (nam)', 'zh-CN-YunjianNeural', 'tiếng Phổ thông', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0005', 'TTS_EdgeTTS', 'Edge TTS — Yunxi (nam)', 'zh-CN-YunxiNeural', 'tiếng Phổ thông', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0006', 'TTS_EdgeTTS', 'Edge TTS — Yunxia (nam)', 'zh-CN-YunxiaNeural', 'tiếng Phổ thông', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0007', 'TTS_EdgeTTS', 'Edge TTS — Xiaobei (nữ, Liêu Ninh)', 'zh-CN-liaoning-XiaobeiNeural', 'Liêu Ninh', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0008', 'TTS_EdgeTTS', 'Edge TTS — Xiaoni (nữ, Thiểm Tây)', 'zh-CN-shaanxi-XiaoniNeural', 'Thiểm Tây', NULL, NULL, 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0009', 'TTS_EdgeTTS', 'Edge TTS — HiuGaai (nữ, HK)', 'zh-HK-HiuGaaiNeural', 'tiếng Quảng Đông', 'General', 'Friendly, Positive', 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0010', 'TTS_EdgeTTS', 'Edge TTS — HiuMaan (nữ, HK)', 'zh-HK-HiuMaanNeural', 'tiếng Quảng Đông', 'General', 'Friendly, Positive', 1, NULL, NULL, NULL, NULL),
('TTS_EdgeTTS0011', 'TTS_EdgeTTS', 'Edge TTS — WanLung (nam, HK)', 'zh-HK-WanLungNeural', 'tiếng Quảng Đông', 'General', 'Friendly, Positive', 1, NULL, NULL, NULL, NULL);

-- 增加是否允许用户注册参数
delete from `sys_params` where  id in (103,104);
INSERT INTO `sys_params` (id, param_code, param_value, value_type, param_type, remark) VALUES (103, 'server.allow_user_register', 'false', 'boolean', 1, '是否运行管理员以外的人注册');
INSERT INTO `sys_params` (id, param_code, param_value, value_type, param_type, remark) VALUES (104, 'server.fronted_url', 'http://xiaozhi.server.com', 'string', 1, '下发六位验证码时显示的控制面板地址');

-- 修正CosyVoiceSiliconflow音色
delete from `ai_tts_voice` where tts_model_id = 'TTS_CosyVoiceSiliconflow';
INSERT INTO `ai_tts_voice` VALUES ('TTS_CosyVoiceSiliconflow0001', 'TTS_CosyVoiceSiliconflow', 'CosyVoice (nam)', 'FunAudioLLM/CosyVoice2-0.5B:alex', 'tiếng Trung', 'https://example.com/cosyvoice/alex.mp3', NULL, 6, NULL, NULL, NULL, NULL);
INSERT INTO `ai_tts_voice` VALUES ('TTS_CosyVoiceSiliconflow0002', 'TTS_CosyVoiceSiliconflow', 'CosyVoice (nữ)', 'FunAudioLLM/CosyVoice2-0.5B:bella', 'tiếng Trung', 'https://example.com/cosyvoice/bella.mp3', NULL, 6, NULL, NULL, NULL, NULL);
