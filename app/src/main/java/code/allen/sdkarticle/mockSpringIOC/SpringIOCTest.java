package code.allen.sdkarticle.mockSpringIOC;

/**
 * Created by allenni on 2018/3/8.
 */

public class SpringIOCTest {
    public static void main(String[] args) throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        CoderService service = (CoderService) context.getBean("coderService");
        Coder coder = new Coder("allen","guangzhou");
        service.add(coder);
    }
}
