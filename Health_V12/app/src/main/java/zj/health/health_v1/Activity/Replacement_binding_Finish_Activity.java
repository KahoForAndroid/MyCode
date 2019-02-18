package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Replacement_binding_Finish_Activity extends BaseActivity {

    private Button next_btn;
    private ImageView back;
    private TextView title;
    private Intent it = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replacement_binding_finish_activity);
        next_btn = (Button)findViewById(R.id.next_btn);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.Replacement_binding_phone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent it = new Intent(Replacement_binding_Finish_Activity.this,Login_Activity.class);
               startActivity(it);

            }
        });


    }

}
