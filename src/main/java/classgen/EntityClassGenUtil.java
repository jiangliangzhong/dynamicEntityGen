package classgen;

import classgen.ass.ClassParam;
import classgen.core.ClassGenerateCore;
import classgen.core.MyClassLoader;

import java.util.List;

/**
 * 类{@code EntityClassGenUtil}: 对外提供实体类生成api
 *  功能1：读取根据传入的List<ClassParam>生态动态类，并完善关联信息
 * @author jiangliangzhong
 * @date 11:14 2019/10/20
 */
public class EntityClassGenUtil {
    public final static MyClassLoader classLoader;
    static{
        classLoader = new MyClassLoader(System.getProperty("user.dir")+"\\class-file");
    }
    public static void generateEntity(ClassParam classParam){

        ClassGenerateCore classGenerateCore = new ClassGenerateCore();
        //生成动态类
        classGenerateCore.generateClassFile(classParam.getFieldParamList(), classParam.getClassName());
    }
}
