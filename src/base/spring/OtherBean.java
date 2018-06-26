package base.spring;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("other-bean")
public class OtherBean {
    @Autowired
    private MyBean myBean;

    @Autowired
    private MyBean myBean2;

    public MyBean getMyBean() {
        return myBean;
    }

    public void setMyBean(MyBean myBean) {
        this.myBean = myBean;
    }

    @PostConstruct
    public void postConstructCallback(){
        System.out.println("after post construct callback");
    }

    public MyBean getMyBean2() {
        return myBean2;
    }

    public void setMyBean2(MyBean myBean2) {
        this.myBean2 = myBean2;
    }
}
