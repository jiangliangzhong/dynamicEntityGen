package classgen.type.transform;



import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类{@code AliasManager}: 数据库字段对应的java类型管理器
 *
 * @author jiangliangzhong
 * @date 20:49 2019/10/24
 */
public class AliasManager {
    private static Map<String,Class<?>> TYPE_ALIASES;
    static{
        TYPE_ALIASES = new ConcurrentHashMap<>();
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);

        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("char", Character.class);
        registerAlias("string", String.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("bigdecimal", BigInteger.class);
        registerAlias("decimal", BigDecimal.class);

    }

    /**
     * 注册别名
     * @date   20:52 2019/10/24
     * @author  jiangliangzhong
     */
    // 注册别名
    public static void registerAlias(String alias, Class<?> value) {
        if (alias == null) {
            throw new RuntimeException("The parameter alias cannot be null");
        }
        // 将名称转换为小写
        String key = alias.toLowerCase(Locale.ENGLISH);
        // 判断名称是否存在，如果别名已存在，且对应的类型不一致，则抛异常
        if (TYPE_ALIASES.containsKey(key) && TYPE_ALIASES.get(key) != null && !TYPE_ALIASES.get(key).equals(value)) {
            throw new RuntimeException("The alias '" + alias + "' is already mapped to the value '" + TYPE_ALIASES.get(key).getName() + "'.");
        }
        // 注册，别名和类型的对应关系
        TYPE_ALIASES.put(key, value);
    }
    /**
     * 解析别名
     * @date   20:53 2019/10/24
     * @author  jiangliangzhong
     * @return  none
     */
    public static Class<?> resolveAlias(String string) {
        if (string == null) {
            return null;
        }
        // 别名转换为小写，因为在注册的时候，转换过
        String key = string.toLowerCase(Locale.ENGLISH);
        Class<?> value;
        // 如果该别名已经注册，则获取对应的类型
        if (TYPE_ALIASES.containsKey(key)) {
            value = TYPE_ALIASES.get(key);
        } else {
            // 尝试使用反射来获取类型
            value = String.class;
        }
        // 返回对应的类型
        return value;

    }
}
