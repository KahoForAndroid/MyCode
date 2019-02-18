package zj.health.health_v1.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zj.health.health_v1.R;

/**
 * 用于搜索下的热门标签布局
 * Created by mayn on 2018/1/8.
 */

public class KahoLabelLayout extends ViewGroup {

    //保存所有的子View
    //泛型里的list每一个元素下标记录每一行一共有多少个子View
    private List<List<MyTextView>> AllChildView = new ArrayList<>();

    //记录每一行的高度
    private List<Integer> mlineHeight = new ArrayList<>();

    //存放子View
    private View child = null;

    public int ViewPosition = 0;

    private OnitemOnClickListener listener;

    //定义状态变量，根据不同的页面有不同的用法 0用于健康咨询 1用于体检报告数据提交页面
    public int ADDTYPE = 0;



    public KahoLabelLayout(Context context) {
        super(context);
    }
    public KahoLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KahoLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setOnitemOnClickListener(OnitemOnClickListener listener){
        this.listener = listener;
    }
    public void setType(int ADDTYPE){
        this.ADDTYPE = ADDTYPE;
    }
    public List<List<MyTextView>> getAllChildView(){
        return AllChildView;
    }
    public void requestOnClickView(int position){
        ViewPosition = position;
        requestLayout();
    }


    /**
     * 从父控件传进来宽高数值和测量模式
     * 测量模式例如宽度或高度设置成wrap_content或者自定义的数值
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //父控件的宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //对于父控件宽度的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //父控件的高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //对于父控件高度的测量模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //用于如果父控件传递的测量模式为wrap_content,则需要自己计算宽高
        int width = 0;
        int height = 0;

        //记录每一行的宽高
        int widthLine = 0;
        int heightLine = 0;

        //获取该控件下一共有多少个子View
        int chiltCount = getChildCount();

        //循环出所有的子View
        for(int i = 0;i < chiltCount; i ++){
            child = getChildAt(i);
            //根据源码分析，方法用于计算出当前循环的子view的宽高
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();

            //当前子view宽度
            int childWidth = child.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            //当前子view高度
            int childHeight = child.getMeasuredHeight()+layoutParams.topMargin+layoutParams.bottomMargin;

            //如果当前行的的总宽度加上子View的宽度超出父控件的宽度时将进行换行逻辑处理
            if(widthLine + childWidth > widthSize){
                //通过对比,获取最大值
                width = Math.max(width,widthLine);

                //这样处理的目的是如果添加当前子View会超出父控件的最大宽度，则将此子View放到下一行
                //新一行的目前宽度
                widthLine = childWidth;

                //当前父控件的总高度
                height += heightLine;
                heightLine = childHeight;
            }else{
                //如果不换行的话就在原有宽度上叠加上子View的宽度
                widthLine += childWidth;
                heightLine = Math.max(heightLine,childHeight);
            }
            if(i == chiltCount -1){
                width = Math.max(width,widthLine);
                height += heightLine;
            }


        }

        // 如果父控件设置的是wrap_content则用自己计算出的控件宽高
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY? widthSize:width,
                heightMode == MeasureSpec.EXACTLY?heightSize:height);
    }

   @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        AllChildView.clear();
        mlineHeight.clear();

        //获取父控件的宽度
        int width = getWidth();

        int widthLine = 0;
        int heightLine = 0;

        List<MyTextView> lineViews = new ArrayList<>();
        int childCount = getChildCount();
        for(int i = 0;i<childCount;i++){
            MyTextView child = (MyTextView) getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            //获得该子View原始的宽度和高度
            int childwidth = child.getMeasuredWidth();
            int childheight = child.getMeasuredHeight();

            if(ADDTYPE == 0){
                if(i == ViewPosition){
                    child.setClick(true);
                    child.setBackground(getResources().getDrawable(R.drawable.radius_blue));
                    child.setTextColor(getResources().getColor(R.color.white));
                }else {
                    child.setClick(false);
                    child.setBackground(getResources().getDrawable(R.drawable.radius_kaho));
                    child.setTextColor(getResources().getColor(R.color.black));
                }
            }else if(ADDTYPE == 2){
                    child.setClick(true);
                    child.setBackground(getResources().getDrawable(R.drawable.radius_blue));
                    child.setTextColor(getResources().getColor(R.color.white));
            }


            //如果当前行的总宽度大于父控件的宽度
            if(widthLine + childwidth + layoutParams.rightMargin + layoutParams.leftMargin > width){
                //新增一行
                mlineHeight.add(heightLine);
                //添加该行所有的子View到AllChildView
                AllChildView.add(lineViews);

                //新的一行的宽度重置
                widthLine = 0;
                //新的一行高度设置
                heightLine = childheight + layoutParams.topMargin + layoutParams.bottomMargin;
                lineViews = new ArrayList<>();
            }

            //在原有的宽度上再加上该子View的宽度
            widthLine += childwidth + layoutParams.rightMargin + layoutParams.leftMargin;
            //获取该行高度
            heightLine = Math.max(heightLine,childheight+layoutParams.topMargin+layoutParams.bottomMargin);
            lineViews.add(child);
            final int index = i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnitemClick(v,index);
                }
            });
        }
        //最后一行直接把View和行直接添加进list
        mlineHeight.add(heightLine);
        AllChildView.add(lineViews);

        //子View的顶部和左边所在的位置
        int left = 0;
        int top  = 0;

        int allLine = mlineHeight.size();

        for(int i = 0 ;i<allLine;i++){

            //获取当前行的所有子View
            lineViews = AllChildView.get(i);
            //获取当前行的高度
            heightLine = mlineHeight.get(i);

            for(int viewIndex = 0;viewIndex<lineViews.size();viewIndex++){
                View child = lineViews.get(viewIndex);

                if(child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

                int cLeft = left + layoutParams.leftMargin;
                int cTop = top + layoutParams.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                child.layout(cLeft,cTop,cRight,cBottom);

                left += child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            //遍历到下一个元素时代表换行，left变量需要重置
            left = 0;
            top += heightLine;

        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    public  interface OnitemOnClickListener{
        void OnitemClick(View view, int position);
    }
}
