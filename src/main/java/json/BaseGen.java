package json;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

/**
 * 类{@code BaseGen}:动态生成类基础,只能用于实体类
 *
 * @author jiangliangzhong
 * @date 18:49 2019/10/18
 */
public final class BaseGen {
    private static Logger logger = Logger.getLogger(BaseGen.class.getName());
    /**
     * 将对象变为jsonObject
     * @date   19:44 2019/10/18
     * @author  jiangliangzhong
     * @param superFlag 是否处理父类的属性
     * @return  none
     */
    public static JsonObject toJsonObject(boolean superFlag, Object object) {
        JsonObject jsonObject = new JsonObject();
        //1、获取本类的属性名
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field:fields){
            fieldValueToJsonObject(jsonObject,field, object);
        }
        //如果不处理父类的属性json化，则直接返回本类的json化对象
        if(!superFlag){
            return jsonObject;
        }
        //父类
        Class<?> superClass = object.getClass().getSuperclass();
        //这里迭代遍历所有的父类
        while(true) {
            //判断父类是否可以json化，通过判断是否有toJsonObject
            Method supertoJsonObject = JsonTools.containToJson(superClass);
            //如果没有toJsonObject方法，则直接跳出
            if(supertoJsonObject == null){
                break;
            }
            //如果不为空，则调用toJsonObject
            Field[] superFields = superClass.getDeclaredFields();
            for (Field field : superFields) {
                fieldValueToJsonObject0(jsonObject, field,object, superClass);

            }
            //继续遍历下一个父类
            superClass = superClass.getSuperclass();
        }
        return jsonObject;
    }

    private static void fieldValueToJsonObject(JsonObject jsonObject, Field field, Object object){
        //使用本类
        fieldValueToJsonObject0(jsonObject,field,object,object.getClass());
    }
    /**
     * 将属性值添加到json中
     * @date   19:58 2019/10/18
     * @author  jiangliangzhong
     * @param  jsonObject json对象
     * @param field 属性对象
     */
    private static void fieldValueToJsonObject0(JsonObject jsonObject, Field field, Object object, Class<?> clazz){
        //获取私有成员变量,修饰符只有private
        if(field.getModifiers() == Modifier.PRIVATE){
            //使用Getter方法获取值，先判断是否有该方法
            try {
                //获取变量的类型
                Class<?> typeClass = field.getType();
                //判断是否包含toJson()
                Method toJsonObjectMethod = JsonTools.containToJson(typeClass);
                if(toJsonObjectMethod !=null){
                    //如果包含，则使用类自带的toJsonObject.toString
                    jsonObject.addProperty(field.getName(),  toJsonObjectMethod.invoke("toJson").toString());
                }else {
                    //否则直接使用toString
                    String fieldName= field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);

                    Method getter;
                    if(typeClass == boolean.class) {
                        getter = clazz.getDeclaredMethod("is" + fieldName);
                    }else{
                        getter = clazz.getDeclaredMethod("get" + fieldName);
                    }
                    Object getterResult = getter.invoke(object);
                    if(getterResult != null) {
                        //TODO 处理不同类型的value，不要直接使用toString
                        //TODO 对于没有getter的属性也要处理
                        jsonObject.addProperty(field.getName(),getterResult.toString());
                    }
                }
            } catch (NoSuchMethodException e) {
                //没有get方法，则直接不管
                logger.info(field.getName()+"属性没有get方法");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fromJson(String jsonStr) {

    }
}
