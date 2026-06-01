package xiaozhi.modules.knowledge.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xiaozhi.modules.knowledge.service.KnowledgeFilesService;


@Component
@AllArgsConstructor
@Slf4j
public class DocumentStatusSyncTask {

    private final KnowledgeFilesService knowledgeFilesService;

    
    @Scheduled(fixedDelay = 30000)
    public void syncRunningDocuments() {
        try {

            knowledgeFilesService.syncRunningDocuments();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
