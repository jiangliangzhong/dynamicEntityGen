package classgen.ass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 类{@code ClassParam}:表与类对应关系类
 *
 * @author jiangliangzhong
 * @date 19:20 2019/10/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassParam {
    private String tableName;
    private String schema;
    private String className;
    private boolean autoMatch;
    /**字段和属性对应关系*/
    private List<FieldParam> fieldParamList;

}
