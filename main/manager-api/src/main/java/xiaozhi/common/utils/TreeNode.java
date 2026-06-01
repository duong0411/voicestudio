package xiaozhi.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class TreeNode<T> implements Serializable {

    
    private Long id;
    
    private Long pid;
    
    private List<T> children = new ArrayList<>();

}