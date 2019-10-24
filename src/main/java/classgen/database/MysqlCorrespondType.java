package classgen.database;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;

/**
 * 类{@code MysqlCorrespondType}:
 *
 * @author jiangliangzhong
 * @date 20:54 2019/10/22
 */
public class MysqlCorrespondType  implements CorrespondType{

    @Override
    public  Class<?> toJavaType(String fieldTypeName) {
        switch (fieldTypeName.toLowerCase()){
            case "int":
                return Integer.class;
            case "varchar":
                return String.class;
            case "char":
                return Character.class;
            case "blob":
                return Character.class;
            case "text":
                return String.class;
            case "integer":
                return Long.class;
            case "tinyint":
                return Integer.class;
            case "bit":
                return Boolean.class;
            case "bigint":
                return BigInteger.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            case "decimal":
                return BigDecimal.class;
            case "datatime":
                return String.class;
            default:
                return String.class;
        }
    }



}
