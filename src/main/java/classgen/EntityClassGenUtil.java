package classgen;

import classgen.adpaters.TransfromUtil;
import classgen.ass.GenerateTaskDetail;
import classgen.ass.GenerateTaskInfo;
import classgen.core.ClassGenerateCore;
import classgen.core.GlobalConfiguration;
import classgen.core.MyClassLoader;
import classgen.core.TaskDetailConfiguration;
import classgen.exception.CannotInitGenTool;
import classgen.mapass.TableWithEntityRelPool;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

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
        if(GlobalConfiguration.getGlobalConfigInfo() == null){
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
        CountDownLatch countDownLatch = new CountDownLatch(GlobalConfiguration.getGlobalConfigInfo().getGenerateTaskList().size());
        List<Future<Boolean>> futureList = new ArrayList<>();
        for(final GenerateTaskInfo generateTaskInfo: GlobalConfiguration.getGlobalConfigInfo().getGenerateTaskList()){
            //多线程去添加
//            Future<Boolean> generateTaskDetailFuture =executorService.submit(new InitThread(generateTaskInfo,countDownLatch));
//            futureList.add(generateTaskDetailFuture);
            try {
                generateTask(generateTaskInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        countDownLatch.await();
//        //查看是否成功初始化
//        for(Future<Boolean> booleanFuture:futureList){
//            if(!booleanFuture.get()){
//                //清空池
//                TableWithEntityRelPool.getEntityClassInfoMap().clear();
//                throw new RuntimeException("初始化失败");
//            }
//        }
    }
    private static class InitThread implements Callable<Boolean> {
        private GenerateTaskInfo generateTaskInfo;
        private CountDownLatch countDownLatch;
        InitThread(GenerateTaskInfo generateTaskInfo, CountDownLatch countDownLatch) {
            this.generateTaskInfo = generateTaskInfo;
            this.countDownLatch = countDownLatch;
        }
        @Override
        public Boolean call() throws Exception {
            try {
                generateTask(generateTaskInfo);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }finally {
                countDownLatch.countDown();
            }
            return true;
        }
    }
    private static void  generateTask(GenerateTaskInfo taskInfo) throws SQLException {
        TaskDetailConfiguration taskDetailConfiguration = new TaskDetailConfiguration(taskInfo);
        GenerateTaskDetail generateTaskDetail;

        // 1. 解析任务细节
        generateTaskDetail = taskDetailConfiguration.analyzeGenerateTaskDetail();
        //2.生成类class文件
        ClassGenerateCore.generateFromTaskDetail(generateTaskDetail);
        //3. 将filepath添加到类加载器中
        MyClassLoader classLoader = MyClassLoader.getInstance();
        classLoader.addClassFile(generateTaskDetail.getTableClassAssInfo().getClassFileDir());
        //4.将所有的表和类对应的信息用Map保存，这样方便动态取得
        TransfromUtil.addIntoTableWithEntityRel(generateTaskDetail);
    }
}
