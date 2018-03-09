package code.allen.sdkarticle.observerToRx;

/**
 * Created by allenni on 2018/3/9.
 */

public class Client {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        Observer observer = new ConcreteObserver();
        subject.attach(observer);
        subject.change("coder");
    }
}
