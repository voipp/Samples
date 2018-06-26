package base.spring;

import java.util.Arrays;
import java.util.Random;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(MyAnnotation.class) && field.getType().equals(String.class))
            .forEach(field -> {

                field.setAccessible(true);

                try {
                    field.set(bean, String.valueOf(new Random().nextInt()));
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException("");
                }
            });

        return bean;
    }

    @Override public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
