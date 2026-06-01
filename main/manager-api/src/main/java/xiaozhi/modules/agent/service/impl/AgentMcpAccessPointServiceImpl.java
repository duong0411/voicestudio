package xiaozhi.modules.agent.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xiaozhi.common.constant.Constant;
import xiaozhi.common.utils.AESUtils;
import xiaozhi.common.utils.HashEncryptionUtil;
import xiaozhi.common.utils.JsonUtils;
import xiaozhi.modules.agent.Enums.XiaoZhiMcpJsonRpcJson;
import xiaozhi.modules.agent.service.AgentMcpAccessPointService;
import xiaozhi.modules.sys.service.SysParamsService;
import xiaozhi.modules.sys.utils.WebSocketClientManager;

@AllArgsConstructor
@Service
@Slf4j
public class AgentMcpAccessPointServiceImpl implements AgentMcpAccessPointService {
    private SysParamsService sysParamsService;

    @Override
    public String getAgentMcpAccessAddress(String id) {

        String url = sysParamsService.getValue(Constant.SERVER_MCP_ENDPOINT, true);
        if (StringUtils.isBlank(url) || "null".equals(url)) {
            return null;
        }
        URI uri = getURI(url);

        String agentMcpUrl = getAgentMcpUrl(uri);

        String key = getSecretKey(uri);

        String encryptToken = encryptToken(id, key);

        String encodedToken = URLEncoder.encode(encryptToken, StandardCharsets.UTF_8);

        agentMcpUrl = "%s/mcp/?token=%s".formatted(agentMcpUrl, encodedToken);
        return agentMcpUrl;
    }

    @Override
    public List<String> getAgentMcpToolsList(String id) {
        String wsUrl = getAgentMcpAccessAddress(id);
        if (StringUtils.isBlank(wsUrl)) {
            return List.of();
        }


        wsUrl = wsUrl.replace("/mcp/", "/call/");

        try {

            try (WebSocketClientManager client = WebSocketClientManager.build(
                    new WebSocketClientManager.Builder()
                            .uri(wsUrl)
                            .bufferSize(1024 * 1024)
                            .connectTimeout(8, TimeUnit.SECONDS)
                            .maxSessionDuration(10, TimeUnit.SECONDS))) {


                log.info("MCP，ID: {}", id);
                client.sendText(XiaoZhiMcpJsonRpcJson.getInitializeJson());


                List<String> initResponses = client.listenerWithoutClose(response -> {
                    try {
                        Map<String, Object> jsonMap = JsonUtils.parseObject(response, Map.class);
                        if (jsonMap != null && Integer.valueOf(1).equals(jsonMap.get("id"))) {

                            return jsonMap.containsKey("result") && !jsonMap.containsKey("error");
                        }
                        return false;
                    } catch (Exception e) {
                        log.warn(": {}", response, e);
                        return false;
                    }
                });


                boolean initSucceeded = false;
                for (String response : initResponses) {
                    try {
                        Map<String, Object> jsonMap = JsonUtils.parseObject(response, Map.class);
                        if (jsonMap != null && Integer.valueOf(1).equals(jsonMap.get("id"))) {
                            if (jsonMap.containsKey("result")) {
                                log.info("MCP，ID: {}", id);
                                initSucceeded = true;
                                break;
                            } else if (jsonMap.containsKey("error")) {
                                log.error("MCP，ID: {}, : {}", id, jsonMap.get("error"));
                                return List.of();
                            }
                        }
                    } catch (Exception e) {
                        log.warn(": {}", response, e);
                    }
                }

                if (!initSucceeded) {
                    log.error("MCP，ID: {}", id);
                    return List.of();
                }


                log.info("MCP，ID: {}", id);
                client.sendText(XiaoZhiMcpJsonRpcJson.getNotificationsInitializedJson());

                log.info("MCP，ID: {}", id);
                client.sendText(XiaoZhiMcpJsonRpcJson.getToolsListJson());


                List<String> toolsResponses = client.listener(response -> {
                    try {
                        Map<String, Object> jsonMap = JsonUtils.parseObject(response, Map.class);
                        return jsonMap != null && Integer.valueOf(2).equals(jsonMap.get("id"));
                    } catch (Exception e) {
                        log.warn(": {}", response, e);
                        return false;
                    }
                });


                for (String response : toolsResponses) {
                    try {
                        Map<String, Object> jsonMap = JsonUtils.parseObject(response, Map.class);
                        if (jsonMap != null && Integer.valueOf(2).equals(jsonMap.get("id"))) {

                            Object resultObj = jsonMap.get("result");
                            if (resultObj instanceof Map) {
                                Map<String, Object> resultMap = (Map<String, Object>) resultObj;
                                Object toolsObj = resultMap.get("tools");
                                if (toolsObj instanceof List) {
                                    List<Map<String, Object>> toolsList = (List<Map<String, Object>>) toolsObj;

                                    List<String> result = toolsList.stream()
                                            .map(tool -> (String) tool.get("name"))
                                            .filter(name -> name != null)
                                            .sorted()
                                            .collect(Collectors.toList());
                                    log.info("MCP，ID: {}, : {}", id, result.size());
                                    return result;
                                }
                            } else if (jsonMap.containsKey("error")) {
                                log.error("，ID: {}, : {}", id, jsonMap.get("error"));
                                return List.of();
                            }
                        }
                    } catch (Exception e) {
                        log.warn(": {}", response, e);
                    }
                }

                log.warn("，ID: {}", id);
                return List.of();

            }
        } catch (Exception e) {
            log.error(" MCP ，ID: {},：{}", id, e.getMessage());
            return List.of();
        }
    }

    
    private static URI getURI(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            log.error("：{}，\n:{}", url, e.getMessage());
            throw new RuntimeException("mcp，mcp");
        }
    }

    
    private static String getSecretKey(URI uri) {

        String query = uri.getQuery();

        String str = "key=";
        return query.substring(query.indexOf(str) + str.length());
    }

    
    private String getAgentMcpUrl(URI uri) {

        String wsScheme = (uri.getScheme().equals("https")) ? "wss" : "ws";

        String path = uri.getSchemeSpecificPart();

        path = path.substring(0, path.lastIndexOf("/"));
        return wsScheme + ":" + path;
    }

    
    private static String encryptToken(String agentId, String key) {

        String md5 = HashEncryptionUtil.Md5hexDigest(agentId);

        String json = "{\"agentId\": \"%s\"}".formatted(md5);

        return AESUtils.encrypt(key, json);
    }
}