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
 * Created by Administrator on 2018/4/4.
 */

public class Wait_for_audit_Activity extends BaseActivity{

    private Button back_btn;
    private ImageView back;
    private TextView title;
    private Intent it = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_for_audit_activity);
        back_btn = (Button)findViewById(R.id.back_btn);
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        title.setText(R.string.waiting_audit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                it = new Intent(Wait_for_audit_Activity.this,MainsActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);

            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                it = new Intent(Wait_for_audit_Activity.this,MainsActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);

            }
        });


    }

}
