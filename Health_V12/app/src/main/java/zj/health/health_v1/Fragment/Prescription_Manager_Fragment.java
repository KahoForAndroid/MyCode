package zj.health.health_v1.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zj.health.health_v1.Adapter.Prescription_Fragment_Adapter;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Prescription_Manager_Fragment extends Fragment {

    private View view = null;
    private RecyclerView prescription_recy;
    private Prescription_Fragment_Adapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rescription_ragment,container,false);
        initView();
        return view;
    }
    private void initView(){
        prescription_recy = (RecyclerView)view.findViewById(R.id.prescription_recy);

//        adapter = new Prescription_Fragment_Adapter(getActivity());
//        prescription_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
//        prescription_recy.setAdapter(adapter);
    }
}

