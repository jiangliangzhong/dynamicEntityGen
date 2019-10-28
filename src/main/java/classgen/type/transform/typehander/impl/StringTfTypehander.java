package classgen.type.transform.typehander.impl;

import classgen.type.transform.typehander.TfBaseTypeHander;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类{@code StringTfTypehander}:
 *
 * @author jiangliangzhong
 * @date 10:42 2019/10/28
 */
public class StringTfTypehander extends TfBaseTypeHander<String> {
    public StringTfTypehander() {
        typeClass = String.class;
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }
}
