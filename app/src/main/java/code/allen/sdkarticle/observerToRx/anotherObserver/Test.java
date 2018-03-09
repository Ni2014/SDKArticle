package code.allen.sdkarticle.observerToRx.anotherObserver;

/**
 * Created by allenni on 2018/3/9.
 */

public class Test {
    public static void main(String[] args) {
        Observable observable = new Observable(new Observable.OnAttach() {
            @Override
            public void call(Observer observer) {
                observer.update("newCoder");
            }
        });

        observable.attach(new Observer() {
            @Override
            public void update(String state) {
                System.out.println("changed : " + state);
            }
        });
    }
}
