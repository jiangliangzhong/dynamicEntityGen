package classgen.type.transform;

import classgen.entity.JdbcTypeE;
import classgen.type.transform.typehander.TfTypeHander;

import classgen.type.transform.typehander.impl.BooleanfTTypeHandler;
import classgen.type.transform.typehander.impl.IntegerTfTypehander;
import classgen.type.transform.typehander.impl.StringTfTypehander;
import org.apache.ibatis.type.IntegerTypeHandler;


import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类{@code TfTypeHanderRegistry}:
 *
 * @author jiangliangzhong
 * @date 10:38 2019/10/28
 */
public class TfTypeHanderRegistry {
    private static Map<JdbcTypeE, TfTypeHander> jdbcTypeHandlerMap = new EnumMap<>(JdbcTypeE.class);
    private static Map<Class<?>, TfTypeHander> classTfTypeHanderMap = new HashMap<>();
    private static Map<Type, Map<JdbcTypeE,TfTypeHander>> typeHandlerMap = new ConcurrentHashMap<>();
    static{
        register(Boolean.class, new BooleanfTTypeHandler());
        register(boolean.class, new BooleanfTTypeHandler());
        register(JdbcTypeE.BOOLEAN, new BooleanfTTypeHandler());
        register(JdbcTypeE.BIT, new BooleanfTTypeHandler());

        register(Integer.class, new IntegerTfTypehander());
        register(int.class, new IntegerTfTypehander());
        register(JdbcTypeE.INTEGER, new IntegerTfTypehander());
        register(JdbcTypeE.INT, new IntegerTfTypehander());

        register(JdbcTypeE.VARCHAR, new StringTfTypehander());
    }
    public static void register(Type javaType,JdbcTypeE jdbcType, TfTypeHander tfTypeHander){
        if(javaType!=null) {
            Map<JdbcTypeE, TfTypeHander> map = typeHandlerMap.get(javaType);
            if (map == null) {
                map = new HashMap<>();

                typeHandlerMap.put(javaType, map);
            }
            map.put(jdbcType, tfTypeHander);
        }
        classTfTypeHanderMap.put(tfTypeHander.getClass(),tfTypeHander);

    }
    public static void register(Type javaType,TfTypeHander tfTypeHander){
        register(javaType,null,tfTypeHander);

    }
    public static void register(JdbcTypeE jdbcType, TfTypeHander tfTypeHander){
        jdbcTypeHandlerMap.put(jdbcType, tfTypeHander);
    }
    public static TfTypeHander getTypeHanderMapByJdbcType(JdbcTypeE jdbcType){
        if(jdbcTypeHandlerMap.containsKey(jdbcType)){
            return jdbcTypeHandlerMap.get(jdbcType);
        }
        throw new RuntimeException("不存在"+jdbcType.name()+"的关联关系");
    }
    public static TfTypeHander getTypeHanderMapByJavaType(Type javaType){
        return getTypeHanderMapByJavaType(javaType, null);
    }
    public static TfTypeHander getTypeHanderMapByJavaType(Type javaType, JdbcTypeE jdbcTypeE){
        return typeHandlerMap.get(javaType).get(jdbcTypeE);
    }
}
