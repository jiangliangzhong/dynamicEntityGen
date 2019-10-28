package classgen.core;


import classgen.association.entity.FieldProperty;
import classgen.entity.JdbcTypeE;
import classgen.type.transform.TfTypeHanderRegistry;
import com.google.common.base.CaseFormat;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.List;

/**
 * 类{@code FieldAnaylzeUtil}: 字段解析工具
 *
 * @author jiangliangzhong
 * @date 17:24 2019/10/22
 */
public class FieldAnaylzeUtil {
    public static void fieldAutoAnalyze(Connection connection,List<FieldProperty> fieldParamList,String driverClassName,String tableName){
        fieldAutoAnalyze(connection,fieldParamList, "","",tableName);
    }
    /**
     * 解析一张表的字段对应关系
     * @date   17:28 2019/10/22
     * @author  jiangliangzhong

     */
    public static void fieldAutoAnalyze(Connection connection, List<FieldProperty> fieldParamList, String databaseName, String schema, String tableName){
        if(databaseName!=null && databaseName.isEmpty()){
            databaseName = null;
        }
        if(schema !=null && schema.isEmpty()){
            schema = null;
        }
        try {
            DatabaseMetaData databaseMetaData =connection.getMetaData();


            ResultSet tableSet = databaseMetaData.getTables(databaseName, schema, tableName,null);
            int tableCnt =0;
            while(tableSet.next()){
                tableCnt ++;
            }
            if(tableCnt ==0){
                throw new RuntimeException("不存在表，请重新检查配置文件和表名大小写");
            }else if(tableCnt > 1){
                throw new RuntimeException("当前信息查询到的表不唯一，请补充信息");
            }
            ResultSet resultSet = databaseMetaData.getColumns(databaseName,schema,tableName,"%");
            while(resultSet.next()){
                //列名
                String columnName = resultSet.getString("COLUMN_NAME");
                //列类型
                String typeName = resultSet.getString("TYPE_NAME");
                JdbcTypeE jdbcType = JdbcTypeE.valueOf(typeName.toUpperCase());
                //对应的类属性名
                String classFieldName = formatFieldName(columnName);
                //字段对应的javaType
                Class<?> javaType = (Class<?>) ((ParameterizedType)TfTypeHanderRegistry.getTypeHanderMapByJdbcType(jdbcType)
                        .getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                //get和set方法
                String methodName = classFieldName.substring(0,1).toUpperCase() + classFieldName.substring(1);
                String getMethodName;
                if(javaType == boolean.class){
                    getMethodName = "is"+ methodName;
                }else{
                    getMethodName = "get"+methodName;
                }
                String setMethodName = "set"+methodName;
                //添加字段信息
                fieldParamList.add(new FieldProperty(columnName,jdbcType,javaType, classFieldName,setMethodName,getMethodName));
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
    public static String createWholeTableName(String separe, String databaseName, String schema, String tableName){
        StringBuilder wholeTableName =new StringBuilder();
        if(!databaseName.isEmpty()){
            wholeTableName.append(databaseName);
            wholeTableName.append(separe);
        }
        if(!schema.isEmpty()){
            wholeTableName.append(schema);
            wholeTableName.append(separe);
        }
        wholeTableName.append(tableName);
        return wholeTableName.toString();
    }
}
