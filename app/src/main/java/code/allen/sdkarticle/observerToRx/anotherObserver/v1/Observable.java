package code.allen.sdkarticle.observerToRx.anotherObserver.v1;

/**
 * Created by allenni on 2018/3/9.
 */

public class Observable<T> {

    protected OnAttach onAttach;

    public Observable(OnAttach onAttach) {
        this.onAttach = onAttach;
    }

    public static <T>Observable<T> create(OnAttach<T> onAttach){
        return new Observable(onAttach);
    }
    public void attach(Observer<T> observer){
        onAttach.call(observer);
    }

    public interface OnAttach<T>{
        void call(Observer<? super T> observer);
    }
}
