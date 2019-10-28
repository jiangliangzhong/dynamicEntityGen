package classgen.type.transform.typehander.impl;

import classgen.type.transform.typehander.TfBaseTypeHander;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类{@code BooleanTypeHandler}:
 *
 * @author jiangliangzhong
 * @date 21:17 2019/10/28
 */
public class BooleanfTTypeHandler extends TfBaseTypeHander<Boolean> {
    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getBoolean(columnName);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }
}
