package classgen.xmldeal;

import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * 类{@code XmlUtil}: 解析xml
 *
 * @author jiangliangzhong
 * @date 16:27 2019/10/19
 */
@Data
public class XmlUtil {
    private String filePath;
    private SAXReader saxReader;
    private Document document ;
    private Element root;
    public XmlUtil(String filePath) throws DocumentException {
        this.filePath = filePath;
        saxReader = new SAXReader();
        document = saxReader.read(new File(filePath));
        root = document.getRootElement();
    }
    /**
     * 得到动态生成的类文件
     * @date   16:41 2019/10/19
     * @author  jiangliangzhong
     * @return  none
     */
    public String getClassFileDir(){
        Iterator iterator = root.elementIterator();
        while(iterator.hasNext()){
            Element childElement = (Element) iterator.next();
            if("classFileDir".equals(childElement.getName())){
                return childElement.getText();
            }
        }
        throw new RuntimeException("没有找到相应的classFileDir标签");
    }

}
