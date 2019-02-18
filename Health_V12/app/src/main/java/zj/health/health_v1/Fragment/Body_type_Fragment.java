package zj.health.health_v1.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/17.
 */

public class Body_type_Fragment extends Fragment{

    private View view;
    private int TYPE;//1血压 2体重 3体温 4心率 5血糖
    private Bundle bundle = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.body_type_fragment,container,false);
        bundle = getArguments();
        TYPE = bundle.getInt("type");
        String a = "";
        return view;
    }

}
