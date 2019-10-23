import classgen.EntityClassGenUtil;
import classgen.exception.CannotInitGenTool;
import classgen.mapass.EntityClassInfo;
import classgen.mapass.TableInfo;
import classgen.mapass.TableWithEntityRelPool;
import javassist.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        EntityClassGenUtil.generateEntity();
        Map<TableInfo, EntityClassInfo> entityClassInfoMap = TableWithEntityRelPool.getEntityClassInfoMap();
        EntityClassInfo entityClassInfo = entityClassInfoMap.get(new TableInfo("student", "school"));
        Class<?> clazz = entityClassInfo.getMyClassLoader().loadClass(entityClassInfo.getClassName());
        Constructor constructor = clazz.getConstructor();
        Object obj = constructor.newInstance();
        obj.getClass().getMethod("setId", Integer.class).invoke(obj, 1);
        System.out.println(obj.getClass().getMethod("getId").invoke(obj));
        System.out.println("");
    }
}
