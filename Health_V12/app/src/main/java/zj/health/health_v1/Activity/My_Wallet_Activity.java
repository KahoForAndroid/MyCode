package zj.health.health_v1.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zj.health.health_v1.Adapter.My_Patient_Adapter;
import zj.health.health_v1.Adapter.My_Wallet_Adapter;
import zj.health.health_v1.Base.BaseActivity;
import zj.health.health_v1.Base.Constant;
import zj.health.health_v1.Model.BalanceListModel;
import zj.health.health_v1.Model.CounselingListModel;
import zj.health.health_v1.MyView.CreateDialog;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.StringUtil;
import zj.health.health_v1.Utils.view.PostUtils;

/**
 * 我的钱包页面
 * Created by Administrator on 2018/4/10.
 */

public class My_Wallet_Activity extends BaseActivity implements View.OnClickListener{

    private RecyclerView consumption_recycle;
    private RelativeLayout Cash_withdrawal_rela,Recharge_rela,Purchase_equipment_rela;
    private ImageView back;
    private My_Wallet_Adapter adapter = null;
    private List<BalanceListModel> balanceListModelList = new ArrayList<>();
    private TextView recommend_friend_num_text,friends_text,money_text;
    private Handler getReferral_countHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        if(!StringUtil.isEmpty(jsonObject1.optString("referralNum"))){
                            recommend_friend_num_text.setText(jsonObject1.optString("referralNum")+"人");
                        }else{
                            recommend_friend_num_text.setText("0人");
                        }
                        if(!StringUtil.isEmpty(jsonObject1.optString("friendReferralNum"))){
                            friends_text.setText(jsonObject1.optString("friendReferralNum")+"人");
                        }else{
                            friends_text.setText("0人");
                        }
                    }else{
                        Toast.makeText(My_Wallet_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Wallet_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Wallet_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };


    private Handler getBalanceHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.optString("data"));
                        int Balance = jsonObject1.optInt("balance");
                        money_text.setText("￥"+changeF2Y(Balance));
                    }else{
                        Toast.makeText(My_Wallet_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Wallet_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Wallet_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };

    private Handler getTransactionHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if(jsonObject.optString("msg").equals("success")){
                        balanceListModelList = new Gson().fromJson(jsonObject.optString("data"),new TypeToken<List<BalanceListModel>>(){}.getType());
                        Collections.reverse(balanceListModelList);
                        adapter.setBalanceListModelList(balanceListModelList);
                    }else{
                        Toast.makeText(My_Wallet_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(msg.what == Constant.UserErrorCode){
                Toast.makeText(My_Wallet_Activity.this, R.string.user_error_message, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(My_Wallet_Activity.this, R.string.http_error, Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet_activity);
        initView();
        BindListener();
        setData();
    }
    private void initView(){
        consumption_recycle = (RecyclerView)findViewById(R.id.consumption_recycle);
        back = (ImageView)findViewById(R.id.back);
        recommend_friend_num_text = (TextView)findViewById(R.id.recommend_friend_num_text);
        friends_text = (TextView)findViewById(R.id.friends_text);
        money_text = (TextView)findViewById(R.id.money_text);
        Cash_withdrawal_rela = (RelativeLayout)findViewById(R.id.Cash_withdrawal_rela);
        Recharge_rela = (RelativeLayout)findViewById(R.id.Recharge_rela);
        Purchase_equipment_rela = (RelativeLayout)findViewById(R.id.Purchase_equipment_rela);
    }
    private void BindListener(){
        back.setOnClickListener(this);
        Cash_withdrawal_rela.setOnClickListener(this);
        Recharge_rela.setOnClickListener(this);
        Purchase_equipment_rela.setOnClickListener(this);
    }
    private void setData(){
        adapter = new My_Wallet_Adapter(this,balanceListModelList);
        consumption_recycle.setLayoutManager(new LinearLayoutManager(this));
        consumption_recycle.setAdapter(adapter);
        getReferralCount();
        getBalance();
        getTransaction();
    }
    private void getReferralCount(){
        new PostUtils().Get(Constant.getReferral_count,true,getReferral_countHandler,this);
    }
    private void getBalance(){
        new PostUtils().Get(Constant.get_balance,true,getBalanceHandler,this);
    }
    private void getTransaction(){
        new PostUtils().Get(Constant.get_transaction,true,getTransactionHandler,this);
    }
    /**
     * 分转元，转换为bigDecimal在toString
     * @return
     */
    public static String changeF2Y(int price) {
        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.Cash_withdrawal_rela:
                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
            case R.id.Recharge_rela:
                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
            case R.id.Purchase_equipment_rela:
                Toast.makeText(this, getString(R.string.coming_opening), Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }
}
