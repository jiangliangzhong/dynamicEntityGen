package classgen.core;

import classgen.ass.FieldParam;
import classgen.superinfo.EntitySuperInfo;
import com.google.gson.Gson;
import javassist.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.base.CaseFormat;

/**
 * 类{@code ObjectGenerateUtil}: 动态生成类
 *
 * @author jiangliangzhong
 * @date 22:55 2019/10/18
 */
public class ClassGenerateCore {
    private static ClassPool classPool;
    private static Logger logger;
    static{
        classPool = ClassPool.getDefault();
        logger = Logger.getLogger(ClassGenerateCore.class.getName());
    }

    private String configXmlPath;
    private EntitySuperInfo entitySuperInfo;

    //必须输入配置文件路径
    public ClassGenerateCore(String configXmlPath) {
        this.configXmlPath = configXmlPath;
    }

    /**
     * 解析配置文件
     * @date   19:55 2019/10/20
     * @author  jiangliangzhong
     * @param configXmlPath 配置xml文件路径
     */
    private void loadConfigurationFile(String configXmlPath){

    }

    public void generateClassFile(final List<FieldParam> fieldParamList,String className) {
        CtClass objClass = classPool.makeClass(className);
        //1. 设置好接口
        try {
            //TODO 弄成一个配置文件,实体类可以继承通用多个配置接口
            CtClass intterface = classPool.get("json.IJsonParse");
            //只添加一个接口
            objClass.setInterfaces(new CtClass[]{intterface});
        } catch (NotFoundException e) {
//            logger.info();
            throw new RuntimeException(e);
        }
        //2. 设置超类
            //TODO 读配置文件
        //3. 设置成员变量,Getter,Setter
        //保存属性的类型，用于构造有参构造函数
        List<CtClass> ctTypeList = new ArrayList<>();
        //遍历字段,添加进类中
        for(FieldParam fieldParam:fieldParamList){
            try {
                //加载属性类型
                CtClass ctType = classPool.get(fieldParam.getFieldType().getName());
                ctTypeList.add(ctType);
                //规范化属性名字
                String ctFieldName = this.checkCtFieldName(fieldParam.getFieldName());
                //将字段名和属性名关联起来
                fieldParam.setClassFieldName(ctFieldName);
                //新建属性对象
                CtField ctField = new CtField(ctType, ctFieldName, objClass);
                //设置修饰符
                ctField.setModifiers(fieldParam.getModifierNum());
                //添加到类中
                objClass.addField(ctField);
                //添加getter和setter
                String methodName = ctFieldName.substring(0,1).toUpperCase() + ctFieldName.substring(1);
                String getMethodName;
                if(fieldParam.getFieldType() == boolean.class){
                    getMethodName = "is"+ methodName;
                }else{
                    getMethodName = "get"+methodName;
                }
                String setMethodName = "set"+methodName;
                objClass.addMethod(CtNewMethod.setter(setMethodName, ctField));
                objClass.addMethod(CtNewMethod.getter(getMethodName,ctField));
                //设置setter， getter名
                fieldParam.setGetterName(getMethodName);
                fieldParam.setSetterName(setMethodName);
            }catch (CannotCompileException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        //4.添加构造方法,无参和有参都添加
        try {
            objClass.addConstructor(CtNewConstructor.make(null,null,objClass));
            CtClass[] constructorParams = ctTypeList.toArray(new CtClass[0]);
            objClass.addConstructor(CtNewConstructor.make(constructorParams,null,objClass));
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        //5、实现接口
        try {
            //拿到接口
            CtClass[] objClassInterfaces = objClass.getInterfaces();
            for(CtClass objClassInterface:objClassInterfaces){
                //拿到需要实现的方法
                CtMethod[] interfaceMethods = objClassInterface.getDeclaredMethods();
                //遍历需要实现的方法
                for(CtMethod iMethod:interfaceMethods){
                    CtMethod implMethod = new CtMethod(iMethod.getReturnType(),iMethod.getName(),
                            iMethod.getParameterTypes(), objClass);
                    //TODO 根据配置文件加载对应实现代码
                    if(iMethod.getName().equals("toJson")){
                        implMethod.setBody("return \" \";");
                    }else{
                        implMethod.setBody(" return;");
                    }
                    //添加到类中
                    objClass.addMethod(implMethod);
                }
            }
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }

        //6.保存动态创建的类，这里可以只保存在内存中，或者写入文件中，为了让其他程序也能加载类，则写入硬盘中。
        try {
            objClass.writeFile(System.getProperty("user.dir")+"\\class-file\\");
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }


    }
    private String checkCtFieldName(String ctFieldName){
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,ctFieldName);
    }

}
