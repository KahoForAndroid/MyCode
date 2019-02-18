package zj.health.health_v1.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Adapter.Contacts_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Model.Contacts;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.pinyin.PinyinComparator;
import zj.health.health_v1.Utils.pinyin.PinyinUtils;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Add_address_list_Activity extends BaseActivity{

    private List<Map<String, String>> list;
    private ImageView back;
    private TextView title;
    private RecyclerView Contact_recy;
    private Contacts_Adapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 123){
                list = (List<Map<String, String>>) msg.obj;
                if(list.size()>0){
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_list_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        back = (ImageView)findViewById(R.id.back);
        title = (TextView)findViewById(R.id.title);
        Contact_recy = (RecyclerView)findViewById(R.id.Contact_recy);
    }
    private void BindListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setData(){
        title.setText(getString(R.string.Mobile_phone_contact));
        list = new ArrayList<>();
        Contact_recy.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Contacts_Adapter(this,list);
        Contact_recy.setAdapter(adapter);
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            getContactThread();
        }else{
            CheckPermission();
        }
    }

    private void getContactThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               getContactInfo();
            }
        }).start();
    }

    /**
     * 读取手机里的联系人信息
     *
     * @return
     */
    private void getContactInfo() {
        // 把所有的联系人放到list
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        // 得到一个内容解析器
        ContentResolver resolver = getContentResolver();
        // 获取联系人表对应的内容提供者url raw_contacts表和data表
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
                null, null, null);

        // 获取contact_id 获取联系人id
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);

            if (contact_id != null) {
                // 具体的某个联系人
                Map<String, String> map = new HashMap<String, String>();

                // 如果不为空 查询对应data表的联系人信息
                Cursor dataCursor = resolver.query(datauri, new String[] {
                                "data1", "mimetype" }, "contact_id=?",
                        new String[] { contact_id }, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    System.out.println("data1 ==" + data1 + "mimetype == "
                            + mimetype);

                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        System.out.println("电话:" + data1);
                        map.put("phone", data1);
                    } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        System.out.println("姓名:" + data1);
                        map.put("name", data1);
                    }
                }

                list.add(map);
                // 释放游标
                dataCursor.close();
            }

        }
        cursor.close();
//        getData(list)
//        Collections.sort(list, new PinyinComparator());
        Message message = handler.obtainMessage();
        message.obj = list;
        message.what = 123;
        handler.sendMessage(message);

    }

    private void CheckPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "deny for what???", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "show the request popupwindow", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        1023);
            }
        } else {
            Toast.makeText(this, "agreed", Toast.LENGTH_SHORT).show();
            getContactThread();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1023: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "allow", Toast.LENGTH_SHORT).show();
                    getContactThread();
                } else {
                    Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private List<Contacts> getData(List<Contacts> data) {
        List<Contacts> listarray = new ArrayList<Contacts>();
        String pinyin = null;
        for (int i = 0; i < data.size(); i++) {
            if(StringUtil.isEmpty(data.get(i).getName())){
                pinyin = PinyinUtils.getPingYin(data.get(i).getPhone());
            }else{
                pinyin = PinyinUtils.getPingYin(data.get(i).getName());
            }
            String Fpinyin = pinyin.substring(0, 1).toUpperCase();

            Contacts person = data.get(i);
            //person.setLoginuser_sname(data.get(i).getLoginuser_sname());
            person.setPinYin(pinyin);
            // 正则表达式，判断首字母是否是英文字母
            if (Fpinyin.matches("[A-Z]")) {
                person.setFirstPinYin(Fpinyin);
            }else{
                person.setFirstPinYin("#");
            }

            listarray.add(person);
        }
        return listarray;

    }


}


