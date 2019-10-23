package classgen.core;

import classgen.ass.*;
import classgen.database.DataBaseConfig;
import classgen.exception.MissingAttributeException;
import classgen.superinfo.EntitySuperInfo;
import classgen.superinfo.InterfaceInfo;
import classgen.superinfo.MethodInfo;
import classgen.superinfo.SuperClassInfo;
import classgen.xmldeal.XmlUtil;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 类{@code TaskDetailConfiguration}: 生成任务详情xml文件加载
 * 每次加载一个任务
 *
 * @author jiangliangzhong
 * @date 10:57 2019/10/22
 */

public class TaskDetailConfiguration {
    private static Logger logger;
    private GenerateTaskInfo generateTaskInfo;
    static{
        Logger.getLogger(TaskDetailConfiguration.class.getName());
    }
    public TaskDetailConfiguration(GenerateTaskInfo generateTaskInfo) {
        if( generateTaskInfo.getTableClassAssInfoFilePath() == null||generateTaskInfo.getTableClassAssInfoFilePath().isEmpty()){
            throw new RuntimeException("异常：任务加载文件路径不能为null或者爲空");
        }
        this.generateTaskInfo = generateTaskInfo;
    }
    /**
     * 从文件中解析任务细节
     * @date   11:03 2019/10/22
     * @author  jiangliangzhong
     * @return  任务细节类
     */
    public GenerateTaskDetail analyzeGenerateTaskDetail() throws SQLException {
        EntitySuperInfo entitySuperInfo = this.anylyzeEntitySuperInfo();
        TableClassAssInfo tableClassAssInfo = this.anylyzeTableClassAssInfo();
        return new GenerateTaskDetail(entitySuperInfo,tableClassAssInfo);
    }
    /**
     * 解析继承信息
     * @date   11:04 2019/10/22
     * @author  jiangliangzhong
     * @return  继承信息
     */
    private EntitySuperInfo anylyzeEntitySuperInfo()  {
        if(this.generateTaskInfo.getSuperInfoFilePath() == null || this.generateTaskInfo.getSuperInfoFilePath().isEmpty()){
            return  null;
        }
        XmlUtil xmlUtil;
        try {
            xmlUtil = new XmlUtil(this.generateTaskInfo.getSuperInfoFilePath());
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：SuperInfoFile文件路径错误，请重新设置");
        }
        String superClassName = null;
        List<InterfaceInfo> interfaceInfos = new ArrayList<>();
        for(Element e1: xmlUtil.getRoot().elements()){
            //拿到超类信息
            if("superClass".equals(e1.getName())){
                superClassName = e1.attributeValue("className");
            }else if("interfaces".equals(e1.getName())){

                //遍历接口信息
                for(Element e2:e1.elements()){
                    //拿到接口信息
                    if("interface".equals(e2.getName())){
                        List<MethodInfo> methodInfoList = new ArrayList<>();
                        //遍历方法集合
                        for(Element e3: e2.elements()){
                            //拿到方法信息
                            if("method".equals(e3.getName())){
                                //默认为空,如果为空，则不实现该接口
                                String code="";
                                for(Element e4:e3.elements()){
                                    if("code".equals(e4.getName())){
                                        code = e4.getText();
                                    }
                                }
                                if(code.isEmpty()){
                                    logger.warn("警告：" +e3.attributeValue("MName")+"方法实现为空字符串,如果没有返回值，需要使用return;");
                                }
                                methodInfoList.add(new MethodInfo(e3.attributeValue("MName"), code));
                            }
                        }
                        interfaceInfos.add(new InterfaceInfo(methodInfoList, e2.attributeValue("IName")));
                    }
                }
            }
        }
        if(superClassName == null){
            return new EntitySuperInfo(null,interfaceInfos);
        }
        return new EntitySuperInfo(new SuperClassInfo(superClassName),interfaceInfos);
    }
    /**
     * 解析表和类关联信息
     * @date   11:04 2019/10/22
     * @author  jiangliangzhong
     * @return  表和类关联信息集合
     */
    private TableClassAssInfo anylyzeTableClassAssInfo() throws SQLException {
        XmlUtil xmlUtil;
        try {
            xmlUtil = new XmlUtil(this.generateTaskInfo.getTableClassAssInfoFilePath());
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：TableClassAssInfoFile文件路径错误，请重新设置");
        }
        //设置数据库表信息,如果没有则会自动设置为null
        String url = xmlUtil.getRoot().attributeValue("url");
        String userName = xmlUtil.getRoot().attributeValue("userName");
        String password = xmlUtil.getRoot().attributeValue("password");
        String driverClassName = xmlUtil.getRoot().attributeValue("driverClassName");
        String classFileDir = "System.getProperty(\"user.dir\")+\"\\\\class-file\"";

        List<ClassParam> classParamList = new ArrayList<>();
        //遍历tables和classFileDir
        for(Element e1: xmlUtil.getRoot().elements()){
            //得到类文件所在路径,默认路径
            if("classFileDir".equals(e1.getName())){
                classFileDir = e1.getText();
            }else if("tables".equals(e1.getName())){
                //遍历table和class的关系
                for(Element e2 :e1.elements()){
                    String schema = e2.attributeValue("schema");
                    if(schema == null){
                        throw new MissingAttributeException("异常：缺失schema属性");
                    }
                    String tableName = e2.attributeValue("tableName");
                    if(tableName == null){
                        throw new MissingAttributeException("异常：缺失tableName属性");
                    }
                    String[] tableInfos = tableName.split(".");
                    //防止用户误将全称写入表名
                    tableName = tableInfos.length ==0 ? tableName :tableInfos[tableInfos.length -1];

                    String className = e2.attributeValue("className");
                    if(className == null){
                        throw new MissingAttributeException("异常：缺失className属性");
                    }
                    //parseBoolean会将除“true”之外的内容全部赋值为false
                    boolean autoMatch = Boolean.parseBoolean(e2.attributeValue("autoMatch"));
                    //保存字段和属性对应关系
                    List<FieldParam> fieldParamList = new ArrayList<>();
                    //自动生成对应关系
                    if(autoMatch){
                        DataBaseConfig dataBaseConfig = new DataBaseConfig(driverClassName,userName,password,url);
                        Connection connection = dataBaseConfig.getConnection();
                        //自动得到字段对应信息
                        FieldAnaylzeUtil.fieldAnalyze(connection,fieldParamList,schema,tableName,driverClassName);
                    }
                    else{
                        //手动生成
                        //遍历field
                        for(Element e3:e2.elements()){
                            if("field".equals(e3.getName())){
                                //对应java类型
                                Class<?> classType;
                                try {
                                    String classTyepName = e3.attributeValue("classType");
                                    if(classTyepName == null || "".equals(classTyepName)){
                                        throw  new RuntimeException("属性类型名不能为空");
                                    }
                                    classType = Class.forName(FieldAnaylzeUtil.basicType(classTyepName));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                    throw new MissingAttributeException("异常：classFieldName错误，导致无法加载对应类");
                                }
                                String fieldName = e3.attributeValue("fieldName");
                                if(fieldName == null|| fieldName.isEmpty()){
                                    throw new MissingAttributeException("异常：fieldName属性不能为空");
                                }
                                String classFieldName = e3.attributeValue("classFieldName");
                                if(classFieldName == null || classFieldName.isEmpty()){
                                    classFieldName = FieldAnaylzeUtil.formatFieldName(fieldName);
                                }
                                String methodName = classFieldName.substring(0,1).toUpperCase() + classFieldName.substring(1);
                                String getMethodName;
                                if(classType == boolean.class){
                                    getMethodName = "is"+ methodName;
                                }else{
                                    getMethodName = "get"+methodName;
                                }
                                String setMethodName = "set"+methodName;
                                fieldParamList.add(new FieldParam(fieldName,classType,classFieldName,setMethodName,getMethodName));
                            }
                        }
                    }
                    classParamList.add(new ClassParam(tableName,schema,className, autoMatch, fieldParamList));
                }
            }
        }
        return new TableClassAssInfo(classParamList,classFileDir,url,userName,password,driverClassName);
    }


}
