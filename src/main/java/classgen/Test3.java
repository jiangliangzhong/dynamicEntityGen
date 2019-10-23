package classgen;


import classgen.exception.CannotInitGenTool;
import classgen.mapass.EntityClassInfo;
import classgen.mapass.TableInfo;
import classgen.mapass.TableWithEntityRelPool;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * 类{@code Test3}:
 *
 * @author jiangliangzhong
 * @date 16:03 2019/10/22
 */
public class Test3 {
    public static void main(String[] args) throws CannotInitGenTool, SQLException, ExecutionException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
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
