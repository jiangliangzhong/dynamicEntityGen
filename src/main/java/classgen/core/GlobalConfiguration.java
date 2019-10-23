package classgen.core;


import classgen.ass.GenerateTaskInfo;
import classgen.ass.GlobalConfigInfo;
import classgen.xmldeal.XmlUtil;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 类{@code GlobalConfiguration}: 用于加载起始配置文件
 * @author jiangliangzhong
 * @date 21:35 2019/10/21
 */
public final class GlobalConfiguration {
    private static XmlUtil xmlUtil;
    private static Logger logger;
    @Getter
    private static GlobalConfigInfo globalConfigInfo;
    /*  加载全局配置文件 */
    static{
        logger = Logger.getLogger(GlobalConfiguration.class.getName());
        try {
            xmlUtil = new XmlUtil(System.getProperty("user.dir") +"/conf/global-conf.xml");
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.warn("初始时，没有加载全局配置文件，稍后请人为添加");
        }
        analyzeGlobalConfigFile();
    }
    /**
     * 加载全局配置文件
     * @date   21:57 2019/10/21
     * @author  jiangliangzhong
     * @param filePath 文件路径
     */
    public static void loadGlobalConfiguration(String filePath){
        try {
            xmlUtil = new XmlUtil(filePath);
        } catch (DocumentException e) {
            logger.error("异常，无法加载全局配置文件");
            throw new RuntimeException(e);
        }
        //加载全局配置文件
        analyzeGlobalConfigFile();
    }
    /**
     * 解析全局配置文件
     * @date   10:24 2019/10/22
     * @author  jiangliangzhong
     */
    private static void analyzeGlobalConfigFile(){
        if(xmlUtil == null){
            throw new RuntimeException("异常：xml工具为null");
        }
        List<GenerateTaskInfo> generateTasks = new ArrayList<>();
        Iterator iterator = xmlUtil.getRoot().elementIterator();
        while(iterator.hasNext()){
            Element element = (Element) iterator.next();
            if("tasks".equals(element.getName())){
                //遍历task
                for(Element e:element.elements()){
                    String superInfoFilePath = e.attributeValue("superInfoFilePath");
                    String tableClassAssInfoFilePath = e.attributeValue("tableClassAssInfoFilePath");
                    generateTasks.add(new GenerateTaskInfo(superInfoFilePath, tableClassAssInfoFilePath));
                }
            }
        }
        globalConfigInfo = new GlobalConfigInfo(generateTasks);
    }

}
