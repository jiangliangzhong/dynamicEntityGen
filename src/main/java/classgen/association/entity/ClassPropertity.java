package classgen.association.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ClassPropertity {
    /** 表名*/
    private String tableName;
    /** 模式名 */
    private String schema;
    /** 数据库名*/
    private String databaseName;
    /**
     * 对应的实体类名
     */
    private String className;
    /**
     * 是否自动匹配
     */
    private boolean autoMatch;
    /**数据库列和类属性对应关系*/
    private List<FieldProperty> fieldPropertyList;

}
