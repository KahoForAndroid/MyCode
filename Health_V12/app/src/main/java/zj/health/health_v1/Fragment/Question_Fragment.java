package zj.health.health_v1.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.Adapter.Prescription_Manage_List_Adapter;
import zj.health.health_v1.Adapter.Question_Fragment_Adapter;
import zj.health.health_v1.Model.QuestionModel;
import zj.health.health_v1.R;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Question_Fragment extends Fragment{

    private View view;
    private RecyclerView question_recy;
    private EditText search_edit;
    private ImageView search_img;
    private Question_Fragment_Adapter adapter;
    private List<QuestionModel> questionModels = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view == null){
            view = inflater.inflate(R.layout.question_fragment,container,false);
        }else{
            initView();
            BindListener();
            setData();
            return inflater.inflate(R.layout.question_fragment,container,false);
        }
        initView();
        BindListener();
        setData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(){
        search_edit = (EditText)view.findViewById(R.id.search_edit);
        search_img = (ImageView)view.findViewById(R.id.search_img);
        question_recy = (RecyclerView)view.findViewById(R.id.question_recy);
    }
    private void BindListener(){

    }
    private void setData(){
        adapter = new Question_Fragment_Adapter(getActivity());
        question_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        question_recy.setAdapter(adapter);

    }
}
