package classgen.type.transform.typehander;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类{@code BaseTypeTfHander}:
 *
 * @author jiangliangzhong
 * @date 20:44 2019/10/24
 */
@Getter
public abstract class TfBaseTypeHander<T> implements TfTypeHander<T> {
    protected Class<?> typeClass;
    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {

        T result;
        try {
            // 获取结果，由子类实现
            result = getNullableResult(rs, columnName);
        } catch (Exception e) {
            throw new RuntimeException( e);
        }
        // 如果为空，则返回 null
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }
    // 从结果集中根据列索引获取数据
    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        T result;
        try {
            // 获取结果，由子类实现
            result = getNullableResult(rs, columnIndex);
        } catch (Exception e) {
            throw new RuntimeException( e);
        }
        // 如果为空，则返回 null
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }
    // 获取结果，由各个子类自己实现
    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;
    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;
}
