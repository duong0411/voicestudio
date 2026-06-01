package xiaozhi.common.page;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class PageData<T> implements Serializable {
    @Schema(description = "")
    private int total;

    @Schema(description = "")
    private List<T> list;

    
    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int) total;
    }
}