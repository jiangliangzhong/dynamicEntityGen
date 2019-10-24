import classgen.EntityClassGenUtil;
import classgen.exception.CannotInitGenTool;
import classgen.mapass.EntityClassInfo;
import classgen.mapass.FieldMemberInfo;
import classgen.mapass.TableInfo;
import classgen.mapass.TableWithEntityRelPool;
import javassist.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 类{@code Application}:
 *
 * @author jiangliangzhong
 * @date 16:51 2019/10/18
 */
public class Application {
    public static void main(String[] args) throws ClassNotFoundException, NotFoundException, CannotCompileException, IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, InterruptedException, ExecutionException, CannotInitGenTool {
        //初始化类
        EntityClassGenUtil.generateEntity();



        EntityClassInfo entityClassInfo = TableWithEntityRelPool.getEntityClassInfoByKey(new TableInfo("student", "school"));


        Class<?> clazz = entityClassInfo.getMyClassLoader().loadClass(entityClassInfo.getClassName());
        Constructor constructor = clazz.getConstructor();
        Object obj = constructor.newInstance();

//        ResultSet rs;
//        while(rs.next) {
//            for (FieldMemberInfo fieldMemberInfo : entityClassInfo.getFieldMemberInfoList()) {
//
//                Object val = rs.getString(fieldMemberInfo.getDbFieldName());
//                rs.get
//                obj.getClass().getMethod(fieldMemberInfo.getSetterName(), fieldMemberInfo.getType()).invoke(obj, val);
//            }
//        }

    }
}
