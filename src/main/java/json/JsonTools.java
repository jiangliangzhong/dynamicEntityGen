package json;

import java.lang.reflect.Method;

/**
 * 类{@code JsonTools}:
 *
 * @author jiangliangzhong
 * @date 20:33 2019/10/18
 */
public class JsonTools {
    /**
     * 判断是否包含toJson方法
     * @date   20:15 2019/10/18
     * @author  jiangliangzhong
     * @param  clazz 类
     * @return  true包含，false不包含
     */
    public static Method containToJson(Class<?> clazz){
        Method supertoJsonObject;
        try {
            supertoJsonObject = clazz.getDeclaredMethod("toJson");
        } catch (NoSuchMethodException e) {
            return null;
        }
        return supertoJsonObject;
    }
}
