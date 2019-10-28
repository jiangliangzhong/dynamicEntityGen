package classgen.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于标志jdbcType
 * @date   15:53 2019/10/28
 * @author  jiangliangzhong
 */
public enum JdbcTypeE {
    /** jdbc对应的类型*/
    ARRAY(2003),
    BIT(-7),
    TINYINT(-6),
    SMALLINT(5),
    INTEGER(4),
    INT(8001),
    BIGINT(-5),
    FLOAT(6),
    REAL(7),
    DOUBLE(8),
    NUMERIC(2),
    DECIMAL(3),
    CHAR(1),
    VARCHAR(12),
    LONGVARCHAR(-1),
    DATE(91),
    TIME(92),
    TIMESTAMP(93),
    BINARY(-2),
    VARBINARY(-3),
    LONGVARBINARY(-4),
    NULL(0),
    OTHER(1111),
    BLOB(2004),
    CLOB(2005),
    BOOLEAN(16),
    CURSOR(-10),
    UNDEFINED(-2147482648),
    NVARCHAR(-9),
    NCHAR(-15),
    NCLOB(2011),
    STRUCT(2002),
    JAVA_OBJECT(2000),
    DISTINCT(2001),
    REF(2006),
    DATALINK(70),
    ROWID(-8),
    LONGNVARCHAR(-16),
    SQLXML(2009),
    DATETIMEOFFSET(-155),
    TIME_WITH_TIMEZONE(2013),
    TIMESTAMP_WITH_TIMEZONE(2014);


    private final int TYPE_CODE;
    private static Map<Integer, JdbcTypeE> codeLookup = new HashMap();

    private JdbcTypeE(int TYPE_CODE) {
        this.TYPE_CODE = TYPE_CODE;
    }
    public static JdbcTypeE forCode(int code) {
        return (JdbcTypeE)codeLookup.get(code);
    }

    static {
        JdbcTypeE[] var0 = values();
        int var1 = var0.length;

        for (JdbcTypeE type : var0) {
            codeLookup.put(type.TYPE_CODE, type);
        }

    }
}
