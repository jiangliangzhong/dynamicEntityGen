import javassist.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 类{@code Application}:
 *
 * @author jiangliangzhong
 * @date 16:51 2019/10/18
 */
public class Application {
    public static void main(String[] args) throws ClassNotFoundException, NotFoundException, CannotCompileException, IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassPool classpool =ClassPool.getDefault();
        CtClass ctClass = classpool.makeClass("entities.Student");
        CtClass superTest = classpool.get("json.IJsonParse");
        try {
            ctClass.setSuperclass(superTest);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        try {
            ctClass.addField(CtField.make("private int age;",ctClass));

        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        ctClass.addMethod(CtMethod.make("public void setAge(int age){this.age = age;}", ctClass));
        ctClass.addMethod(CtMethod.make("public int getAge(){return this.age;}", ctClass));

        byte[] byteArray = ctClass.toBytecode();
        FileOutputStream output = new FileOutputStream(System.getProperty("user.dir")+"\\target\\classes\\Student.class");
        output.write(byteArray);
        output.close();

        Class<?> clazz = Application.class.getClassLoader().loadClass("entities.Student");
        Object obj = clazz.newInstance();
        obj.getClass().getMethod("test").invoke(obj);
    }
}
