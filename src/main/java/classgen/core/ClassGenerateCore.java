package classgen.core;

import classgen.ass.ClassParam;
import classgen.ass.FieldParam;
import classgen.ass.GenerateTaskDetail;
import classgen.superinfo.EntitySuperInfo;
import classgen.superinfo.InterfaceInfo;
import classgen.superinfo.MethodInfo;
import com.google.gson.Gson;
import javassist.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.base.CaseFormat;
import lombok.Getter;

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
    @Getter
    private GenerateTaskDetail generateTaskDetail;
    private EntitySuperInfo entitySuperInfo;
    private String classFilePath;
    private ClassGenerateCore(GenerateTaskDetail generateTaskDetail) {
        this.generateTaskDetail = generateTaskDetail;
        entitySuperInfo = this.generateTaskDetail.getEntitySuperInfo();
        this.classFilePath = this.generateTaskDetail.getTableClassAssInfo().getClassFileDir();
    }
    /**
     * 执行生成任务对外提供的接口
     * @date   14:59 2019/10/23
     * @author  jiangliangzhong
     */
    public static void generateFromTaskDetail(GenerateTaskDetail generateTaskDetail){
        ClassGenerateCore classGenerateCore = new ClassGenerateCore(generateTaskDetail);
        classGenerateCore.execGenerateTask();
    }
    private void execGenerateTask(){
        //遍历该任务下，每个类的任务
        for(ClassParam classParam:generateTaskDetail.getTableClassAssInfo().getClassParamList()){
            //生成该类的文件
            this.generateClassFile(classParam);
        }
    }

    public void generateClassFile(ClassParam classParam) {
        CtClass objClass = classPool.makeClass(classParam.getClassName());
        //设置多态信息
        if(entitySuperInfo!=null) {
            //1. 设置好接口和实现方法
            if (!entitySuperInfo.getInterfaceInfoList().isEmpty()) {
                //遍历接口信息
                for (InterfaceInfo interfaceInfo : entitySuperInfo.getInterfaceInfoList()) {
                    try {
                        CtClass interfaceCt = classPool.get(interfaceInfo.getInterfaceName());
                        //添加接口
                        objClass.addInterface(interfaceCt);
                        for (MethodInfo methodInfo : interfaceInfo.getMethodInfoList()) {
                            CtMethod iMethod = interfaceCt.getDeclaredMethod(methodInfo.getMethodName());
                            //添加实现方法
                            CtMethod impMethod = new CtMethod(iMethod.getReturnType(), iMethod.getName(),
                                    iMethod.getParameterTypes(), objClass);
                            try {
                                impMethod.setBody(methodInfo.getMethodName());
                                objClass.addMethod(impMethod);
                            } catch (CannotCompileException e) {
                                e.printStackTrace();
                                throw new RuntimeException("异常：无法编译" + iMethod.getName() + "的实现代码");
                            }

                        }
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException("异常：无法得到接口" + interfaceInfo.getInterfaceName() + "的类对象");
                    }
                }

            }
            //2. 设置超类
            if (entitySuperInfo.getSuperClassInfo().getClassName() != null) {
                try {
                    CtClass superClass = classPool.get(entitySuperInfo.getSuperClassInfo().getClassName());
                    objClass.setSuperclass(superClass);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException("异常：无法获取" + entitySuperInfo.getSuperClassInfo().getClassName() + "类对象，请检查配置文件");
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                    throw new RuntimeException("异常：无法为实体类设置超类为" + entitySuperInfo.getSuperClassInfo().getClassName() + "的类对象，请检查配置文件");
                }

            }
        }
        //3. 设置成员变量,Getter,Setter
        //保存属性的类型，用于构造有参构造函数
        List<CtClass> ctTypeList = new ArrayList<>();
        //遍历字段,添加进类中
        for(FieldParam fieldParam:classParam.getFieldParamList()){
            try {
                //加载属性类型
                CtClass ctType = classPool.get(fieldParam.getFieldType().getName());
                ctTypeList.add(ctType);
                //规范化属性名字
                String ctFieldName = fieldParam.getFieldName();
                //将字段名和属性名关联起来
                fieldParam.setClassFieldName(ctFieldName);
                //新建属性对象
                CtField ctField = new CtField(ctType, ctFieldName, objClass);
                //设置修饰符
                ctField.setModifiers(fieldParam.getModifierNum());
                //添加到类中
                objClass.addField(ctField);
                //添加getter和setter
                objClass.addMethod(CtNewMethod.setter(fieldParam.getSetterName(), ctField));
                objClass.addMethod(CtNewMethod.getter(fieldParam.getGetterName(),ctField));
            }catch (CannotCompileException | NotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("为动态类添加属性名，发生错误");
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

        //5.保存动态创建的类，这里可以只保存在内存中，或者写入文件中，为了让其他程序也能加载类，则写入硬盘中。
        try {
            objClass.writeFile(classFilePath);
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }


}
