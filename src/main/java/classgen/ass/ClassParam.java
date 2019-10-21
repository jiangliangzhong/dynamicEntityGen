package classgen.ass;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类{@code ClassParam}:表与类对应关系类
 *
 * @author jiangliangzhong
 * @date 19:20 2019/10/19
 */
@Data
public class ClassParam {

    private String tableName;
    private String className;
    /**字段和属性对应关系*/
    private List<FieldParam> fieldParamList;



}
