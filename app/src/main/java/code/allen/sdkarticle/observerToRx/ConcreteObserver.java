package code.allen.sdkarticle.observerToRx;

/**
 * Created by allenni on 2018/3/9.
 */

public class ConcreteObserver implements Observer {
    @Override
    public void update(String state) {
        System.out.println("changd :" + state);
    }
}
