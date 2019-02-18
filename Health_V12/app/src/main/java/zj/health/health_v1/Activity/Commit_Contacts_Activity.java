package zj.health.health_v1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/28.
 */

public class Commit_Contacts_Activity extends BaseActivity{

    private ImageView back;
    private TextView phone_number_text,title;
    private Button upload_btn;
    private  int a = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commit_contacts_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        phone_number_text = (TextView)findViewById(R.id.phone_number_text);
        upload_btn = (Button)findViewById(R.id.upload_btn);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a == 0){
                    a = 1;
                    upload_btn.setText(getString(R.string.checked_contactsfriend));
                }else{
                    Intent it = new Intent(Commit_Contacts_Activity.this,Add_address_list_Activity.class);
                    startActivity(it);
                }
            }
        });
    }
    private void setData(){
        title.setText(getString(R.string.upload_contacts));
        phone_number_text.setText(getString(R.string.bind_phone_text)+"13544941184");
    }
}
