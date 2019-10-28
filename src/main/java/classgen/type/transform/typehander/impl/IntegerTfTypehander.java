package classgen.type.transform.typehander.impl;

import classgen.type.transform.typehander.TfBaseTypeHander;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类{@code IntegerTfTypehander}:
 *
 * @author jiangliangzhong
 * @date 10:44 2019/10/28
 */
public class IntegerTfTypehander extends TfBaseTypeHander<Integer> {
    public IntegerTfTypehander() {
        typeClass = Integer.class;
    }

    @Override
    public Integer getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getInt(columnName);
    }

    @Override
    public Integer getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }
}
