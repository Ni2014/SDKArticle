package code.allen.sdkarticle.observerToRx;

/**
 * Created by allenni on 2018/3/9.
 */

public class ConcreteSubject extends Subject {
    public void change(String state){
        this.notifyObservers(state);
    }
}
