package code.allen.sdkarticle.mockSpringIOC;

/**
 * Created by allenni on 2018/3/8.
 */

public class CoderServiceImpl implements CoderService {
    CoderDao coderDao = null;

    public CoderDao getCoderDao() {
        return coderDao;
    }

    public void setCoderDao(CoderDao coderDao) {
        this.coderDao = coderDao;
    }

    @Override
    public void add(Coder coder) {
        coderDao.add(coder);
    }
}
