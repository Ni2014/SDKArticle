package code.allen.sdkarticle.howRx;

import java.net.URI;
import java.util.List;

/**
 * Created by allenni on 2018/3/12.
 */

public interface Api {
    List<Coder> queryCoders(String condition);
    URI store(Coder coder);
}
