package code.allen.sdkarticle.mockSpringIOC;

/**
 * Created by allenni on 2018/3/8.
 */

public class CoderDaoImpl implements CoderDao{
    @Override
    public void add(Coder coder) {
        System.out.println(coder.getName() + "saving...");
    }
}
