package code.allen.sdkarticle.observerToRx.anotherObserver.v1;

/**
 * Created by allenni on 2018/3/9.
 */

public interface Observer<T> {
    void update(T state);
}