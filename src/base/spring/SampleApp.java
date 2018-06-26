package base.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SampleApp {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-cfg.xml");

        OtherBean bean = (OtherBean)ctx.getBean("other-bean");

        System.out.println("value=" + bean.getMyBean().getValue());

        System.out.println("value in another MyBean=" + bean.getMyBean2().getValue());

        System.out.println("Both MyBean's equal : " + (bean.getMyBean2() == bean.getMyBean()));
    }
}
