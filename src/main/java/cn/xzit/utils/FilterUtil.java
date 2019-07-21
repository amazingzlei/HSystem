package cn.xzit.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterUtil {

    private static Map<String,String> mapper = new HashMap<String,String>();

    static{
        init();
    }

    /**
     * 初始化map
     */
    public static void init(){
        SAXReader reader = new SAXReader();
        try {
            InputStream in = FilterUtil.class.getResourceAsStream("/utils/filterUtils.xml");
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mapper");
            for (Element element : list) {
                mapper.put(element.attributeValue("type"), element.attributeValue("url"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据类型获取对应url
     * @param type 人员类型
     * @return 路径
     */
    public static String getUrl(String type){
        return mapper.get(type);
    }
}
