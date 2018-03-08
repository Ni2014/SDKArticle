package code.allen.sdkarticle.mockSpringIOC;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by allenni on 2018/3/8.
 */

public class ClassPathXmlApplicationContext implements  BeanFactory {


    private Map<String,Object> beans = new HashMap<>();

    public ClassPathXmlApplicationContext() throws JDOMException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(this.getClass().getResourceAsStream("beans.xml"));
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren("bean");
        for (int i = 0; i < list.size(); i++) {
            Element element = list.get(i);
            String id = element.getAttributeValue("id");
            String clazz = element.getAttributeValue("class");
            Object instance = Class.forName(clazz).newInstance();
            beans.put(id,instance);
            for (Element propertyElement : (List<Element>) element.getChildren("property")){
                String name = propertyElement.getAttributeValue("name");
                String bean = propertyElement.getAttributeValue("bean");
                Object beanObj = beans.get(bean);
                String methodName = "set"+ name.substring(0, 1).toUpperCase()+ name.substring(1);
                Method method = instance.getClass().getMethod(methodName,beanObj.getClass().getInterfaces()[0]);
                method.invoke(instance,beanObj);
            }
        }

    }
    @Override
    public Object getBean(String id) {
        return beans.get(id);
    }
}
