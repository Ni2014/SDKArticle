package code.allen.sdkarticle.howRx;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by allenni on 2018/3/12.
 */

public class Coder implements Comparable<Coder> {
    Bitmap avantar;
    int position;
    @Override
    public int compareTo(Coder another) {
        return Integer.compare(position,another.position);
    }
}
