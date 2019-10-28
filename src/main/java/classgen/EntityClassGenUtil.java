package classgen;

import classgen.adpaters.TransfromUtil;
import classgen.association.entity.BuildTaskFilePathProperity;
import classgen.association.entity.BuildTaskProperity;
import classgen.association.entity.FieldProperty;

import classgen.core.ClassGenerateCore;
import classgen.core.configuration.BuildTaskConfiguration;
import classgen.core.configuration.GlobalConfiguration;
import classgen.core.MyClassLoader;

import classgen.exception.CannotInitGenTool;
import classgen.mapping.EntityClassInfo;
import classgen.mapping.FieldMemberInfo;
import classgen.mapping.TableWithEntityRelPool;
import classgen.type.transform.TfTypeHanderRegistry;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 类{@code EntityClassGenUtil}: 对外提供实体类生成api
 *  功能1：读取根据传入的List<ClassParam>生态动态类，并完善关联信息
 * @author jiangliangzhong
 * @date 11:14 2019/10/20
 */
public class EntityClassGenUtil {
    public static void generateEntity() throws CannotInitGenTool, ExecutionException, InterruptedException {
        if(GlobalConfiguration.getGlobalProperity() == null){
            //如果为空，则说明全局变量为空，则不能使用此功能
            throw new CannotInitGenTool("异常：无法初始化该工具，请检查是否存在conf/global-conf.xml文件");
        }
        generateEntity0();
    }

    public static void generateEntity(String globalConfigFilePath) throws CannotInitGenTool, InterruptedException, ExecutionException {
        //如果加载全局变量文件错误，则直接报错
        try{
            GlobalConfiguration.loadGlobalConfiguration(globalConfigFilePath);
        }catch(RuntimeException e){
            throw new CannotInitGenTool("异常：加载全局配置文件失败，请检查格式");
        }
        generateEntity0();

    }

    private static void generateEntity0() throws InterruptedException, ExecutionException {
        ExecutorService executorService = new ThreadPoolExecutor(5,5,0L,
                TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),new BasicThreadFactory.Builder().namingPattern("fix-thread-pool-%d").
                daemon(true).build());
        //2.遍历加载任务细节
        CountDownLatch countDownLatch = new CountDownLatch(GlobalConfiguration.getGlobalProperity().getBuildTaskFilePathProperityList().size());
        List<Future<Boolean>> futureList = new ArrayList<>();
        for(final BuildTaskFilePathProperity filePathProperity: GlobalConfiguration.getGlobalProperity().getBuildTaskFilePathProperityList()){
            //多线程去添加
            Future<Boolean> generateTaskDetailFuture =executorService.submit(new InitThread(filePathProperity,countDownLatch));
            futureList.add(generateTaskDetailFuture);
//            try {
//                generateTask(generateTaskInfo);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
        countDownLatch.await();
        //查看是否成功初始化
        for(Future<Boolean> booleanFuture:futureList){
            if(!booleanFuture.get()){
                //清空池
                TableWithEntityRelPool.getEntityClassInfoMap().clear();
                throw new RuntimeException("初始化失败");
            }
        }
    }
    private static class InitThread implements Callable<Boolean> {
        private BuildTaskFilePathProperity filePathProperity;
        private CountDownLatch countDownLatch;
        InitThread(BuildTaskFilePathProperity filePathProperity, CountDownLatch countDownLatch) {
            this.filePathProperity = filePathProperity;
            this.countDownLatch = countDownLatch;
        }
        @Override
        public Boolean call() throws Exception {
            try {
                generateTask(filePathProperity);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }finally {
                countDownLatch.countDown();
            }
            return true;
        }
    }
    private static void  generateTask(BuildTaskFilePathProperity filePathProperity) throws SQLException {
        BuildTaskConfiguration buildTaskConfiguration = new BuildTaskConfiguration(filePathProperity);
        BuildTaskProperity buildTaskProperity;
        // 1. 解析任务细节
        buildTaskProperity = buildTaskConfiguration.analyzeGenerateTaskDetail();
        //2.生成类class文件
        ClassGenerateCore.generateFromTaskDetail(buildTaskProperity);
        //3. 将filepath添加到类加载器中
        MyClassLoader classLoader = MyClassLoader.getInstance();
        classLoader.addClassFile(buildTaskProperity.getTableToClassMapping().getClassFileDir());
        //4.将所有的表和类对应的信息用Map保存，这样方便动态取得
        TransfromUtil.addIntoTableWithEntityRel(buildTaskProperity);
    }

    /**
     * 自动拿到对象
     * @date   10:02 2019/10/28
     * @author  jiangliangzhong
     * @return  none
     */
    public static Object getRowValue(ResultSet resultSet, EntityClassInfo entityClassInfo) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> objClass;
        try {
            objClass = entityClassInfo.getMyClassLoader().loadClass(entityClassInfo.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：无法加载" +entityClassInfo+"类");
        }
        Object obj = createRowValueObject(objClass);
        ResultSetMetaData resultSetMetaData;
        resultSetMetaData = resultSet.getMetaData();
        int columnCnt = resultSetMetaData.getColumnCount();
        //遍历字段，优先数据库有的信息，所以遍历元数据中的信息
        for(int i =1;i<= columnCnt;i++) {
            //字段名，不是别名
            String columnName = resultSetMetaData.getColumnName(i);
            FieldProperty fieldProperty = entityClassInfo.getFieldMemberInfoByKey(columnName);
            //到这里，fieldMemberInfo，任何属性都不可能为null
            if (fieldProperty.getJavaType() == null) {
//                throw new RuntimeException("该字段对应的类型为null，无法设置属性");
                System.out.println(columnName + "字段对应的信息为null,故跳过此列");
                continue;
            }
            //getString对应
            objClass.getMethod(fieldProperty.getSetterName(), fieldProperty.getJavaType()).invoke(obj,
                    TfTypeHanderRegistry.getTypeHanderMapByJdbcType(fieldProperty.getJdbcType()).getResult(resultSet,i));
        }
        return obj;
    }
    private static Object createRowValueObject(Class<?> objClass){
        Constructor constructor = null;
        try {
            constructor = objClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：无法创建目标对象");
        }

    }
}
