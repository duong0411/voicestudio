package xiaozhi.modules.knowledge.rag;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import xiaozhi.common.exception.ErrorCode;
import xiaozhi.common.exception.RenException;


@Slf4j
public class KnowledgeBaseAdapterFactory {


    private static final Map<String, Class<? extends KnowledgeBaseAdapter>> adapterRegistry = new HashMap<>();


    private static final Map<String, KnowledgeBaseAdapter> adapterCache = new ConcurrentHashMap<>();


    private static final int MAX_CACHE_SIZE = 50;

    static {

        registerAdapter("ragflow", xiaozhi.modules.knowledge.rag.impl.RAGFlowAdapter.class);

    }

    
    public static void registerAdapter(String adapterType, Class<? extends KnowledgeBaseAdapter> adapterClass) {
        if (adapterRegistry.containsKey(adapterType)) {
            log.warn(" '{}' ，", adapterType);
        }
        adapterRegistry.put(adapterType, adapterClass);
        log.info(": {} -> {}", adapterType, adapterClass.getSimpleName());
    }

    
    public static KnowledgeBaseAdapter getAdapter(String adapterType, Map<String, Object> config) {
        String cacheKey = buildCacheKey(adapterType, config);


        if (adapterCache.containsKey(cacheKey)) {
            log.debug(": {}", cacheKey);
            return adapterCache.get(cacheKey);
        }


        KnowledgeBaseAdapter adapter = createAdapter(adapterType, config);


        if (adapterCache.size() >= MAX_CACHE_SIZE) {
            log.warn(" ({})，", MAX_CACHE_SIZE);

            adapterCache.clear();
        }

        adapterCache.put(cacheKey, adapter);
        log.info(": {}", cacheKey);

        return adapter;
    }

    
    public static KnowledgeBaseAdapter getAdapter(String adapterType) {
        return getAdapter(adapterType, null);
    }

    
    public static Set<String> getRegisteredAdapterTypes() {
        return adapterRegistry.keySet();
    }

    
    public static boolean isAdapterTypeRegistered(String adapterType) {
        return adapterRegistry.containsKey(adapterType);
    }

    
    public static void clearCache() {
        int cacheSize = adapterCache.size();
        adapterCache.clear();
        log.info("， {} ", cacheSize);
    }

    
    public static void removeCacheByType(String adapterType) {
        int removedCount = 0;
        for (String cacheKey : adapterCache.keySet()) {
            if (cacheKey.startsWith(adapterType + "@")) {
                adapterCache.remove(cacheKey);
                removedCount++;
            }
        }
        log.info(" '{}' ， {} ", adapterType, removedCount);
    }

    
    public static Map<String, Object> getFactoryStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("registeredAdapterTypes", adapterRegistry.keySet());
        status.put("cachedAdapterCount", adapterCache.size());
        status.put("cacheKeys", adapterCache.keySet());
        return status;
    }

    
    private static KnowledgeBaseAdapter createAdapter(String adapterType, Map<String, Object> config) {
        if (!adapterRegistry.containsKey(adapterType)) {
            throw new RenException(ErrorCode.RAG_ADAPTER_TYPE_NOT_SUPPORTED,
                    ": " + adapterType);
        }

        try {
            Class<? extends KnowledgeBaseAdapter> adapterClass = adapterRegistry.get(adapterType);
            KnowledgeBaseAdapter adapter = adapterClass.getDeclaredConstructor().newInstance();


            if (config != null) {
                adapter.initialize(config);


                if (!adapter.validateConfig(config)) {
                    throw new RenException(ErrorCode.RAG_CONFIG_VALIDATION_FAILED,
                            ": " + adapterType);
                }
            }

            log.info(": {}", adapterType);
            return adapter;

        } catch (Exception e) {
            log.error(": {}", adapterType, e);
            throw new RenException(ErrorCode.RAG_ADAPTER_CREATION_FAILED,
                    ": " + adapterType + ", : " + e.getMessage());
        }
    }

    
    private static String buildCacheKey(String adapterType, Map<String, Object> config) {
        if (config == null || config.isEmpty()) {
            return adapterType + "@default";
        }


        StringBuilder keyBuilder = new StringBuilder(adapterType + "@");


        int configHash = config.hashCode();
        keyBuilder.append(configHash);

        return keyBuilder.toString();
    }
}