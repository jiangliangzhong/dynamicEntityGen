package classgen.type.transform.typehander;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 接口{@code ITypeHander}: jdbctype转换接口
 *
 * @author jiangliangzhong
 * @date 20:41 2019/10/24
 */
public interface TfTypeHander<T> {
    T getResult(ResultSet rs, String columnName) throws SQLException;
    T getResult(ResultSet rs, int columnIndex) throws SQLException;
}
