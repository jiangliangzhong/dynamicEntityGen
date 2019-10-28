package classgen.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类{@code TableInfo}:
 *
 * @author jiangliangzhong
 * @date 15:09 2019/10/23
 */
@Data

@EqualsAndHashCode
public class TableInfo {
    private String tableName;
    /** 如果是mysql，则是数据库名 */
    private String schema;
    private String databaseName;

    public TableInfo(String tableName, String schema, String databaseName) {
        this.tableName = tableName;
        this.schema = schema;
        this.databaseName = databaseName;
        if(schema ==null){
            this.schema = "";
        }
        if(databaseName == null){
            this.databaseName = "";
        }
    }
}
