package code.allen.sdkarticle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import code.allen.sdkarticle.annotationIOC.ViewInjectHelper;
import code.allen.sdkarticle.annotationIOC.annotations.ContentView;
import code.allen.sdkarticle.annotationIOC.annotations.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.btn)
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        ViewInjectHelper.inject(this);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast("demo");
                }
            });

    }


    public void toast(String string){
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
    }
}
