package classgen.mapass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.validation.Schema;
import java.util.Objects;

/**
 * 类{@code TableInfo}:
 *
 * @author jiangliangzhong
 * @date 15:09 2019/10/23
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class TableInfo {
    private String tableName;
    /** 如果是mysql，则是数据库名 */
    private String schema;
}
