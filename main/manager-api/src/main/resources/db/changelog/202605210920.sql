-- Add parameters (temperature, max_tokens, top_p, top_k) to Gemini, Ollama, Xinference, Doubao, and ChatGLM LLM providers

UPDATE `ai_model_provider` 
SET `fields` = '[{"key":"api_key","label":"API密钥","type":"string"},{"key":"model_name","label":"模型名称","type":"string"},{"key":"http_proxy","label":"HTTP代理","type":"string"},{"key":"https_proxy","label":"HTTPS代理","type":"string"},{"key":"temperature","label":"温度","type":"number"},{"key":"max_tokens","label":"最大令牌数","type":"number"},{"key":"top_p","label":"top_p值","type":"number"},{"key":"top_k","label":"top_k值","type":"number"}]' 
WHERE `id` = 'SYSTEM_LLM_gemini';

UPDATE `ai_model_provider` 
SET `fields` = '[{"key":"model_name","label":"模型名称","type":"string"},{"key":"base_url","label":"服务地址","type":"string"},{"key":"temperature","label":"温度","type":"number"},{"key":"max_tokens","label":"最大令牌数","type":"number"},{"key":"top_p","label":"top_p值","type":"number"}]' 
WHERE `id` = 'SYSTEM_LLM_ollama';

UPDATE `ai_model_provider` 
SET `fields` = '[{"key":"model_name","label":"模型名称","type":"string"},{"key":"base_url","label":"服务地址","type":"string"},{"key":"temperature","label":"温度","type":"number"},{"key":"max_tokens","label":"最大令牌数","type":"number"},{"key":"top_p","label":"top_p值","type":"number"}]' 
WHERE `id` = 'SYSTEM_LLM_xinference';

UPDATE `ai_model_provider` 
SET `fields` = '[{"key":"base_url","label":"基础URL","type":"string"},{"key":"model_name","label":"模型名称","type":"string"},{"key":"api_key","label":"API密钥","type":"string"},{"key":"temperature","label":"温度","type":"number"},{"key":"max_tokens","label":"最大令牌数","type":"number"},{"key":"top_p","label":"top_p值","type":"number"}]' 
WHERE `id` = 'SYSTEM_LLM_doubao';

UPDATE `ai_model_provider` 
SET `fields` = '[{"key":"model_name","label":"模型名称","type":"string"},{"key":"url","label":"服务地址","type":"string"},{"key":"api_key","label":"API密钥","type":"string"},{"key":"temperature","label":"温度","type":"number"},{"key":"max_tokens","label":"最大令牌数","type":"number"},{"key":"top_p","label":"top_p值","type":"number"}]' 
WHERE `id` = 'SYSTEM_LLM_chatglm';
