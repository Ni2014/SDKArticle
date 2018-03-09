package code.allen.sdkarticle.observerToRx.anotherObserver.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by allenni on 2018/3/9.
 */

public class Test {
    public static void main(String[] args) {
        /*Observable.OnAttach onAttach = new Observable.OnAttach() {
            @Override
            public void call(Observer observer) {
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3));
                observer.update(list);
            }
        };*/

        Observable.OnAttach onAttach = observer -> {
            ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3));
            observer.update(list);
        };
        Observable observable = Observable.create(onAttach);

        /*Observer observer = new Observer<ArrayList<Integer>>() {
            @Override
            public void update(ArrayList<Integer> state) {
                for (Integer integer : state) {
                    System.out.println("get : " + integer);
                }
            }
        };*/

        Observer observer = (Observer<ArrayList<Integer>>) state -> {
            for (Integer integer : state) {
                System.out.println("get : " + integer);
            }
        };

        observable.attach(observer);
    }
}
