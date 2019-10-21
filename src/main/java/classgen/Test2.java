package classgen;

import classgen.ass.ClassParam;
import classgen.ass.FieldParam;
import classgen.core.ClassGenerateCore;
import classgen.core.MyClassLoader;
import javassist.Modifier;
import javassist.NotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类{@code Test2}:
 *
 * @author jiangliangzhong
 * @date 16:18 2019/10/19
 */
public class Test2 {
    public static void main(String[] args) throws NotFoundException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        List<FieldParam> fieldParams = new ArrayList<>();
        FieldParam fieldParam = new FieldParam(Modifier.PRIVATE,"id",int.class);fieldParams.add(fieldParam);
        ClassParam classParam = new ClassParam();
        classParam.setFieldParamList(fieldParams);
        classParam.setClassName("jiang.Jiang");
        classParam.setSimpleClassName("Jiang");
        classParam.setPackageName("jiang");
        classParam.setTableName("jiang");

        EntityClassGenUtil.generateEntity(classParam);
        Class<?> clzz = EntityClassGenUtil.classLoader.loadClass(classParam.getClassName());
        Constructor constructor = clzz.getConstructor();
        Object obj = constructor.newInstance();
        Method[] methods = obj.getClass().getMethods();
        obj.getClass().getMethod("setId", int.class).invoke(obj, 1);
        System.out.println(obj.getClass().getMethod("getId").invoke(obj));
        System.out.println();
    }
}
