import classgen.EntityClassGenUtil;
import classgen.database.DataBaseConfig;
import classgen.exception.CannotInitGenTool;
import classgen.mapping.EntityClassInfo;
import classgen.mapping.EntityClassProperty;
import classgen.mapping.TableInfo;
import classgen.mapping.TableWithEntityRelPool;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 类{@code Application}:
 *
 * @author jiangliangzhong
 * @date 16:51 2019/10/18
 */
public class Application {
    public static void main(String[] args) throws ClassNotFoundException, NotFoundException, CannotCompileException, IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, InterruptedException, ExecutionException, CannotInitGenTool, SQLException {
        //初始化类
        EntityClassGenUtil.generateEntity();
        EntityClassInfo entityClassInfo = TableWithEntityRelPool.getEntityClassInfoByKey(new TableInfo("apply_school", "", "chain"));

        DataBaseConfig dataBaseConfig = new DataBaseConfig("com.mysql.jdbc.Driver","root","123456",
                "jdbc:mysql://192.168.50.248:3306/student?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8");
//        DataBaseConfig dataBaseConfig = new DataBaseConfig("com.mysql.jdbc.Driver","lab246","lizhenhao",
//                "jdbc:postgresql://172.16.13.21:5432/lab246");
        Connection connection = dataBaseConfig.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from stuInfo");
        while(resultSet.next()){
            Object object = EntityClassGenUtil.getRowValue(resultSet, entityClassInfo);
            System.out.println(object.getClass().getMethod(entityClassInfo.getFieldMemberInfoByKey("id").getGetterName()).invoke(object));
        }





    }
}
