package code.allen.sdkarticle.observerToRx.anotherObserver;

/**
 * Created by allenni on 2018/3/9.
 */

public class Observable {

    protected OnAttach onAttach;

    public Observable(OnAttach onAttach){
        this.onAttach = onAttach;
    }

    public void attach(Observer observer){
        onAttach.call(observer);
    }

    public interface OnAttach{
        void call(Observer observer);
    }
}
