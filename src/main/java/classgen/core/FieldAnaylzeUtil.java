package classgen.core;

import classgen.ass.FieldParam;
import classgen.database.DBType;
import com.google.common.base.CaseFormat;

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 类{@code FieldAnaylzeUtil}: 字段解析工具
 *
 * @author jiangliangzhong
 * @date 17:24 2019/10/22
 */
public class FieldAnaylzeUtil {
    /**
     * 解析一张表的字段对应关系
     * @date   17:28 2019/10/22
     * @author  jiangliangzhong
     * @param
     * @return  none
     */
    public static void fieldAnalyze(Connection connection, List<FieldParam> fieldParamList, String schema, String tableName, String driverClassName){
        DBType dbType = DBType.valueOf(driverClassName);
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(dbType.getSelectSql());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：请检查查询字段的sql语句是否正确或者数据库连接是否正确");
        }
        try {
            preparedStatement.setString(1, schema);
            preparedStatement.setString(2, tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                //对应java类型
                Class<?> clazz = dbType.getCorrespondType().toJavaType(resultSet.getString(2));
                //对应的类属性名
                String classFieldName = formatFieldName(resultSet.getString(1));
                String methodName = classFieldName.substring(0,1).toUpperCase() + classFieldName.substring(1);
                String getMethodName;
                if(clazz == boolean.class){
                    getMethodName = "is"+ methodName;
                }else{
                    getMethodName = "get"+methodName;
                }
                String setMethodName = "set"+methodName;
                //添加字段信息
                fieldParamList.add(new FieldParam(resultSet.getString(1),clazz,classFieldName, setMethodName,getMethodName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：查询");
        }


    }

    public static String formatFieldName(String ctFieldName){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,ctFieldName);
    }
    public static String basicType(String typeName){
        switch (typeName){
            case "int":
                return Integer.class.getName();
            case "char":
                return Character.class.getName();
            case "short":
                return Short.class.getName();
            case "float":
                return Float.class.getName();
            case "double":
                return Double.class.getName();
            case "boolean":
                return Boolean.class.getName();
            case "long":
                return Long.class.getName();
            case "byte":
                return Byte.class.getName();
            default:
                return typeName;
        }
    }

}
