package zj.health.health_v1.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zj.health.health_v1.Model.CalendarDateListModel;
import zj.health.health_v1.Model.RemindCalendarModel;
import zj.health.health_v1.R;
import zj.health.health_v1.Utils.CalendarUtil;
import zj.health.health_v1.Utils.DateUtil;
import zj.health.health_v1.Utils.PaintUtil;
import zj.health.health_v1.Utils.StringUtil;

/**
 * Created by Administrator on 2018/4/8.
 * 自定义日历控件
 */

public class CalendarView extends View{

    private String TAG = "CalendarView";

    /** 各部分背景*/
    private int mBgMonth, mBgWeek, mBgDay, mBgPre;
    /** 标题的颜色、大小*/
    private int mTextColorMonth;
    private float mTextSizeMonth;
    private int mMonthRowL, mMonthRowR;
    private float mMonthRowSpac;
    private float mMonthSpac;
    /** 星期的颜色、大小*/
    private int mTextColorWeek, mSelectWeekTextColor;
    private float mTextSizeWeek;
    /** 日期文本的颜色、大小*/
    private int mTextColorDay,todayColor;
    private float mTextSizeDay;
    /** 任务次数文本的颜色、大小*/
    private int mTextColorPreFinish, mTextColorPreUnFinish, mTextColorPreNull;
    private float mTextSizePre;
    /** 选中的文本的颜色*/
    private int mSelectTextColor;
    /** 选中背景*/
    private int mSelectBg, mCurrentBg;
    private float mSelectRadius, mCurrentBgStrokeWidth;
    private float[] mCurrentBgDashPath;

    /** 行间距*/
    private float mLineSpac;
    /** 字体上下间距*/
    private float mTextSpac;

    private Paint mPaint;
    private Paint bgPaint;
    private Paint eventPaint;

    private float titleHeight, weekHeight, dayHeight, preHeight, oneHeight;
    private int columnWidth;       //每列宽度

    private Date month; //当前的月份
    private boolean isCurrentMonth;       //展示的月份是否是当前月
    private int currentDay, selectDay, lastSelectDay;    //当前日期 、 选中的日期 、上一次选中的日期（避免造成重复回调请求）

    private int dayOfMonth;    //月份天数
    private int firstIndex;    //当月第一天位置索引
    private int todayWeekIndex;//今天是星期几
    private int firstLineNum, lastLineNum; //第一行、最后一行能展示多少日期
    private int lineNum;      //日期行数
    private String[] WEEK_STR = new String[]{"日", "一", "二", "三", "四", "五", "六", };
    private Map<String ,String> map = new HashMap<>();
    /**绘制月份*/
    private int rowLStart, rowRStart, rowWidth;
    private List<RemindCalendarModel> list = new ArrayList<>();

    public CalendarView(Context context) {
        this(context,null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, 0);

        mBgMonth = a.getColor(R.styleable.CalendarView_mBgMonth, Color.TRANSPARENT);
        mBgWeek = a.getColor(R.styleable.CalendarView_mBgWeek, Color.TRANSPARENT);
        mBgDay = a.getColor(R.styleable.CalendarView_mBgDay, Color.TRANSPARENT);
        mBgPre = a.getColor(R.styleable.CalendarView_mBgPre, Color.TRANSPARENT);

        mMonthRowL = a.getResourceId(R.styleable.CalendarView_mMonthRowL, R.drawable.return_left);
        mMonthRowR = a.getResourceId(R.styleable.CalendarView_mMonthRowR, R.drawable.return_right);
        mMonthRowSpac = a.getDimension(R.styleable.CalendarView_mMonthRowSpac, 20);
        mTextColorMonth = a.getColor(R.styleable.CalendarView_mTextColorMonth, Color.BLACK);
        mTextSizeMonth = a.getDimension(R.styleable.CalendarView_mTextSizeMonth, 100);
        mMonthSpac = a.getDimension(R.styleable.CalendarView_mMonthSpac, 20);
        mTextColorWeek = a.getColor(R.styleable.CalendarView_mTextColorWeek, Color.BLACK);
        mSelectWeekTextColor = a.getColor(R.styleable.CalendarView_mSelectWeekTextColor, Color.BLACK);
        todayColor = a.getColor(R.styleable.CalendarView_todayColor,Color.RED);

        mTextSizeWeek = a.getDimension(R.styleable.CalendarView_mTextSizeWeek, 70);
        mTextColorDay = a.getColor(R.styleable.CalendarView_mTextColorDay, Color.GRAY);
        mTextSizeDay = a.getDimension(R.styleable.CalendarView_mTextSizeDay, 70);
        mTextColorPreFinish = a.getColor(R.styleable.CalendarView_mTextColorPreFinish, Color.BLUE);
        mTextColorPreUnFinish = a.getColor(R.styleable.CalendarView_mTextColorPreUnFinish, Color.BLUE);
        mTextColorPreNull  = a.getColor(R.styleable.CalendarView_mTextColorPreNull, Color.BLUE);
        mTextSizePre = a.getDimension(R.styleable.CalendarView_mTextSizePre, 40);
        mSelectTextColor = a.getColor(R.styleable.CalendarView_mSelectTextColor, Color.YELLOW);
        mCurrentBg = a.getColor(R.styleable.CalendarView_mCurrentBg, Color.GRAY);

        try {
            int dashPathId = a.getResourceId(R.styleable.CalendarView_mCurrentBgDashPath, R.array.CalendarView_currentDay_bg_DashPath);
            int[] array = getResources().getIntArray(dashPathId);
            mCurrentBgDashPath = new float[array.length];
            for(int i=0;i<array.length;i++){
                mCurrentBgDashPath[i]=array[i];
            }
        }catch (Exception e){
            e.printStackTrace();
            mCurrentBgDashPath = new float[]{2, 3, 2, 3};
        }
        mSelectBg = a.getColor(R.styleable.CalendarView_mSelectBg, Color.YELLOW);
        mSelectRadius = a.getDimension(R.styleable.CalendarView_mSelectRadius, 20);
        mCurrentBgStrokeWidth = a.getDimension(R.styleable.CalendarView_mCurrentBgStrokeWidth, 5);
        mLineSpac = a.getDimension(R.styleable.CalendarView_mLineSpac, 20);
        mTextSpac = a.getDimension(R.styleable.CalendarView_mTextSpac, 20);
        a.recycle();  //注意回收

        //进行高宽大小设置
        initCompute();
    }

    public void setList(List<RemindCalendarModel> list){
        this.list = list;
        invalidate();
    }


    private void initCompute(){
        mPaint = new Paint();
        bgPaint = new Paint();
        eventPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        bgPaint.setAntiAlias(true);//抗锯齿
        eventPaint.setAntiAlias(true);//抗锯齿

        //标题字体大小
        mPaint.setTextSize(mTextSizeMonth);
        //标题的高度
        titleHeight = PaintUtil.getFontHeight(mPaint) + 2*mMonthSpac;

        //星期的字体大小
        mPaint.setTextSize(mTextSizeWeek);
        //获得星期字体高度
        weekHeight = PaintUtil.getFontHeight(mPaint)*2;

        //日期的字体大小
        mPaint.setTextSize(mTextSizeDay);
        //日期的字体高度
        dayHeight = PaintUtil.getFontHeight(mPaint);

        //次数的字体大小
        mPaint.setTextSize(mTextSizePre);
        //次数的高度
        preHeight = PaintUtil.getFontHeight(mPaint);
        //一行的高度 = 行间距 + 日期的字体高度 + 字间距 + 次数字体高度
        oneHeight = mLineSpac + dayHeight + mTextSpac + preHeight;

        //获得当前年月
        String cDateStr = DateUtil.getMonthStr(new Date());
        setMonth(cDateStr);
    }


    /**
     * 设置日历月份
     * @param Month 月份参数
     */
    public void setMonth(String Month){

        //获得设置的月份
        month = DateUtil.str2Date(Month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //获得今天是多少号
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        //获得今天是属于这个月的第几个星期
        todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        //获得当前日期的date对象
        Date cM = DateUtil.str2Date(DateUtil.getMonthStr(new Date()));
        //判断设置的日期月份是否为当前月
        if(cM.getTime() == month.getTime()){
            isCurrentMonth = true;
            //如果是当前月的话就默认选中当前日期
            selectDay = currentDay;
        }else{
            isCurrentMonth = false;
            selectDay = currentDay;
        }
        Log.d(TAG, "设置月份："+month+"   今天"+currentDay+"号, 是否为当前月："+isCurrentMonth);
        calendar.setTime(month);
        //返回指定月份的总天数
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //获得当月1号在星期几开始
        firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        lineNum = 1;
        //当月的第一个星期行有多少天
        firstLineNum = 7 - firstIndex;
        lastLineNum = 0;
        //计算出除了第一行日期数外还剩多少天
        int shengyu = dayOfMonth - firstLineNum;
        //计算出剩下的日期还能平均分成多少行
        while (shengyu>7){
            lineNum++;
            shengyu -= 7;
        }
        //按7列为一行，如果除不尽还有剩下的数就作为最后一行
        if(shengyu>0){
            lineNum++;
            lastLineNum = shengyu;
        }
        Log.i(TAG, DateUtil.getMonthStr(month)+"一共有"+dayOfMonth+"天,第一天的索引是："+firstIndex+"   有"+lineNum+
                "行，第一行"+firstLineNum+"个，最后一行"+lastLineNum+"个");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获得宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获得每一列的宽度
        columnWidth = widthSize/7;
        //控件的总高度 = 标题的高度 + 星期行的高度 + （行数 * 每一行的高度）
        float height = titleHeight + weekHeight + (lineNum * oneHeight);
        //该方法决定的当前控件的大小  getSuggestedMinimumWidth :默认提供的最小宽度
        //widthMeasureSpec:测量宽度 height:高度
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec), (int) height);
    }


    private void drawMonth(Canvas canvas){
        //月份背景
        bgPaint.setColor(mBgMonth);
        RectF rect = new RectF(0,0,getWidth(),titleHeight);
        canvas.drawRect(rect,bgPaint);
        //绘制月份
        mPaint.setTextSize(mTextSizeMonth);
        //画笔颜色
        mPaint.setColor(mTextColorMonth);
        //获得日期字体长度
        float textLen = PaintUtil.getFontLength(mPaint, DateUtil.getMonthStr(month));
        //绘制字体居中
        float textStart = (getWidth() - textLen)/2;
        canvas.drawText(DateUtil.getMonthStr(month),textStart,mMonthSpac+PaintUtil.getFontLeading(mPaint),mPaint);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),mMonthRowL);

        int height = bitmap.getHeight();
        rowWidth = bitmap.getWidth();
//        rowLStart = (int) (textStart - 2 * mMonthRowSpac - rowWidth);
        rowLStart = getLeft()+50;
        //绘制左箭头位置
        canvas.drawBitmap(bitmap, rowLStart , (titleHeight - height)/2, new Paint());
        bitmap = BitmapFactory.decodeResource(getResources(), mMonthRowR);
//        rowRStart = (int)(textStart+textLen);
        rowRStart = getRight()-100;
        int wieth = getWidth();
        //绘制右箭头位置
        canvas.drawBitmap(bitmap, rowRStart, (titleHeight - height)/2, new Paint());
    }

    /**绘制绘制星期*/
    private void drawWeek(Canvas canvas){
        //背景
        bgPaint.setColor(mBgWeek);
        //星期行的位置
        RectF rect = new RectF(0,titleHeight,getWidth(),titleHeight+weekHeight);
        canvas.drawRect(rect,bgPaint);
        //绘制星期：七天
        mPaint.setTextSize(mTextSizeWeek);

        for(int i = 0;i<WEEK_STR.length;i++){
//            if(todayWeekIndex == i && isCurrentMonth){
//                mPaint.setColor(mSelectWeekTextColor);
//            }else{
//                mPaint.setColor(mTextColorWeek);
//            }
            mPaint.setColor(mTextColorWeek);
            int len = (int)PaintUtil.getFontLength(mPaint, WEEK_STR[i]);
            int x = i * columnWidth + (columnWidth - len)/2;
            canvas.drawText(WEEK_STR[i], x, titleHeight + PaintUtil.getFontLeading(mPaint)*2 - PaintUtil.getFontLeading(mPaint)/2, mPaint);
        }
    }

    /**绘制日期和次数*/
    private void drawDayAndPre(Canvas canvas){
        float top = titleHeight+weekHeight;
        for(int line = 0;line<lineNum;line++){
            if(line == 0){
                drawDayAndPre(canvas, top, firstLineNum, 0, firstIndex);
            }else if(line == lineNum-1){
                top += oneHeight;
                drawDayAndPre(canvas, top, lastLineNum, firstLineNum+(line-1)*7, 0);
            }else{
                top += oneHeight;
                drawDayAndPre(canvas, top, 7, firstLineNum+(line-1)*7, 0);
            }
        }
    }

    /**
     * 绘制某一行的日期
     * @param canvas
     * @param top 顶部坐标
     * @param count 此行需要绘制的日期数量（不一定都是7天）
     * @param overDay 已经绘制过的日期，从overDay+1开始绘制
     * @param startIndex 此行第一个日期的星期索引
     */
    private void drawDayAndPre(Canvas canvas, float top,
                               int count, int overDay, int startIndex){
//        Log.e(TAG, "总共"+dayOfMonth+"天  有"+lineNum+"行"+ "  已经画了"+overDay+"天,下面绘制："+count+"天");
        //背景
        float topPre = top + mLineSpac + dayHeight;
        bgPaint.setColor(mBgDay);
        RectF rect = new RectF(0, top, getWidth(), topPre);
        canvas.drawRect(rect, bgPaint);

        bgPaint.setColor(mBgPre);
        rect = new RectF(0, topPre, getWidth(), topPre + mTextSpac + dayHeight);
        canvas.drawRect(rect, bgPaint);

        mPaint.setTextSize(mTextSizeDay);
        float dayTextLeading = PaintUtil.getFontLeading(mPaint);
        mPaint.setTextSize(mTextSizePre);
        float preTextLeading = PaintUtil.getFontLeading(mPaint);
//        Log.v(TAG, "当前日期："+currentDay+"   选择日期："+selectDay+"  是否为当前月："+isCurrentMonth);
        for(int i = 0; i<count; i++){
            int left = (startIndex + i)*columnWidth;
            int day = (overDay+i+1);

            mPaint.setTextSize(mTextSizeDay);



            //选中的日期，如果是本月，选中日期正好是当天日期，下面的背景会覆盖上面绘制的虚线背景
            if(selectDay == day){
                //选中的日期字体白色，橙色背景
                mPaint.setColor(mSelectTextColor);
                bgPaint.setColor(mSelectBg);
                bgPaint.setStyle(Paint.Style.STROKE);
                bgPaint.setStrokeWidth(StringUtil.dp2px(getContext(),2));
                //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                canvas.drawCircle(left+columnWidth/2, top + mLineSpac +dayHeight/2, mSelectRadius, bgPaint);
            }else{
                mPaint.setColor(mTextColorDay);
            }

            //如果是当前月，当天日期需要做处理
            if(isCurrentMonth && currentDay == day){
                mPaint.setColor(todayColor);
                bgPaint.setColor(mSelectBg);
                bgPaint.setStyle(Paint.Style.STROKE);  //空心
                PathEffect effect = new DashPathEffect(mCurrentBgDashPath, 1);
                bgPaint.setStrokeWidth(StringUtil.dp2px(getContext(),2));
                //绘制空心圆背景
//                canvas.drawCircle(left+columnWidth/2, top + mLineSpac +dayHeight/2,
//                        mSelectRadius-mCurrentBgStrokeWidth, bgPaint);
            }
//            else if(!isCurrentMonth && currentDay == day){
//                bgPaint.setColor(mSelectBg);
//                bgPaint.setStyle(Paint.Style.STROKE);
//                bgPaint.setStrokeWidth(StringUtil.dp2px(getContext(),2));
//                //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
//                canvas.drawCircle(left+columnWidth/2, top + mLineSpac +dayHeight/2, mSelectRadius, bgPaint);
//            }
            //绘制完后将画笔还原，避免脏笔
            bgPaint.setPathEffect(null);
            bgPaint.setStrokeWidth(0);
            bgPaint.setStyle(Paint.Style.FILL);

            int len = (int)PaintUtil.getFontLength(mPaint, day+"");
            int x = left + (columnWidth - len)/2;
            canvas.drawText(day+"", x, top + mLineSpac + dayTextLeading, mPaint);

            if(list.size() > 0){
                boolean isDraw = false;
                for (int k = 0;k<list.size();k++){
                    for (int j = 0;j<list.get(k).getReminderDaily().size();j++) {
                        String dateString = DateUtil.getDateStrYmd2(new Date(Long.parseLong(list.get(k).getReminderDaily().get(j).getEventTime())));
                        boolean istake_all = false;
                        for (int l = 0;l<list.get(k).getReminderDaily().get(j).getEvents().size();l++){
                            if(list.get(k).getReminderDaily().get(j).getEvents().get(l).isTake()){
                                istake_all = true;
                            }
                        }
                        int days = Integer.parseInt(dateString);
                        if(day == days){
                            if(istake_all){
                                eventPaint.setStrokeWidth(5);
                                eventPaint.setColor(Color.GREEN);
                                eventPaint.setStyle(Paint.Style.FILL);
                            }else{
                                eventPaint.setStrokeWidth(5);
                                eventPaint.setColor(Color.RED);
                                eventPaint.setStyle(Paint.Style.FILL);
                            }
                            //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                            canvas.drawCircle((float) (left+(columnWidth*0.75)), (float) (top + mLineSpac +(dayHeight*0.1)), StringUtil.dp2px(getContext(),4), eventPaint);
//                            return;
                            isDraw = true;
                            break;
                        }
                        if(isDraw){
                            break;
                        }
                    }

                }
//                if(!StringUtil.isEmpty(list.get(day-1).getImg())){
//                    eventPaint.setStrokeWidth(5);
//                    eventPaint.setColor(Color.RED);
//                    eventPaint.setStyle(Paint.Style.FILL);
//                    //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
//                    canvas.drawCircle((float) (left+(columnWidth*0.75)), (float) (top + mLineSpac +(dayHeight*0.1)), StringUtil.dp2px(getContext(),4), eventPaint);
//                }
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMonth(canvas);
        drawWeek(canvas);
        drawDayAndPre(canvas);
    }


    /****************************事件处理↓↓↓↓↓↓↓****************************/
    //焦点坐标
    private PointF focusPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, false);
                break;
            case MotionEvent.ACTION_MOVE:
//                focusPoint.set(event.getX(), event.getY());
//                touchFocusMove(focusPoint, false);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, true);
                break;
        }
        return true;
    }

    /**焦点滑动*/
    public void touchFocusMove(final PointF point, boolean eventEnd) {
        Log.e(TAG, "点击坐标：("+point.x+" ，"+point.y+"),事件是否结束："+eventEnd);
        /**标题和星期只有在事件结束后才响应*/
        if(point.y<=titleHeight){
            //事件在标题上
            if(eventEnd && listener!=null){
                if(point.x>=rowLStart && point.x<(rowLStart+2*mMonthRowSpac+rowWidth)){
//                    Log.w(TAG, "点击左箭头");
                    listener.onLeftRowClick();
                }else if(point.x>rowRStart && point.x<(rowRStart + 2*mMonthRowSpac+rowWidth)){
//                    Log.w(TAG, "点击右箭头");
                    listener.onRightRowClick();
                }else if(point.x>rowLStart && point.x <rowRStart){
                    listener.onTitleClick(DateUtil.getMonthStr(month), month);
                }
            }
        }else if(point.y<=(titleHeight+weekHeight)){
            //事件在星期部分
            if(eventEnd && listener!=null){
                //根据X坐标找到具体的焦点日期
                int xIndex = (int)point.x / columnWidth;
                Log.e(TAG, "列宽："+columnWidth+"  x坐标余数："+(point.x / columnWidth));
                if((point.x / columnWidth-xIndex)>0){
                    xIndex += 1;
                }
                if(listener!=null){
                    listener.onWeekClick(xIndex-1, WEEK_STR[xIndex-1]);
                }
            }
        }else{
            /**日期部分按下和滑动时重绘，只有在事件结束后才响应*/
            touchDay(point, eventEnd);
        }
    }

    //控制事件是否响应
    private boolean responseWhenEnd = false;
    /**事件点在 日期区域 范围内*/
    private void touchDay(final PointF point, boolean eventEnd){
        //根据Y坐标找到焦点行
        boolean availability = false;  //事件是否有效
        //日期部分
        float top = titleHeight+weekHeight+oneHeight;
        int foucsLine = 1;
        while(foucsLine<=lineNum){
            if(top>=point.y){
                availability = true;
                break;
            }
            top += oneHeight;
            foucsLine ++;
        }
        if(availability){
            //根据X坐标找到具体的焦点日期
            int xIndex = (int)point.x / columnWidth;
            if((point.x / columnWidth-xIndex)>0){
                xIndex += 1;
            }
//            Log.e(TAG, "列宽："+columnWidth+"  x坐标余数："+(point.x / columnWidth));
            if(xIndex<=0)
                xIndex = 1;   //避免调到上一行最后一个日期
            if(xIndex>7)
                xIndex = 7;   //避免调到下一行第一个日期
//            Log.e(TAG, "事件在日期部分，第"+foucsLine+"/"+lineNum+"行, "+xIndex+"列");
            if(foucsLine == 1){
                //第一行
                if(xIndex<=firstIndex){
                    Log.e(TAG, "点到开始空位了");
                    setSelectedDay(selectDay, true);
                }else{
                    setSelectedDay(xIndex-firstIndex, eventEnd);
                }
            }else if(foucsLine == lineNum){
                //最后一行
                if(xIndex>lastLineNum){
                    Log.e(TAG, "点到结尾空位了");
                    setSelectedDay(selectDay, true);
                }else{
                    setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex, eventEnd);
                }
            }else{
                setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex, eventEnd);
            }
        }else{
            //超出日期区域后，视为事件结束，响应最后一个选择日期的回调
            setSelectedDay(selectDay, true);
        }
    }

    /****************************事件处理↑↑↑↑↑↑↑****************************/


    /**设置选中的日期*/
    private void setSelectedDay(int day, boolean eventEnd){
        Log.w(TAG, "选中："+day+"  事件是否结束"+eventEnd);
        selectDay = day;
        invalidate();
        if(listener!=null && eventEnd && responseWhenEnd && lastSelectDay!=selectDay) {
            lastSelectDay = selectDay;
            listener.onDayClick(selectDay, DateUtil.getMonthStrYM(month) + "-"+selectDay);
        }
        responseWhenEnd = !eventEnd;
    }

    /****************************事件处理↑↑↑↑↑↑↑****************************/


    @Override
    public void invalidate() {
        requestLayout();
        super.invalidate();
    }

    /***********************接口API↓↓↓↓↓↓↓**************************/
    public void setRenwu(String month){
        setMonth(month);
        invalidate();
    }



    /**月份增减*/
    public void monthChange(int change){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        calendar.add(Calendar.MONTH, change);
        setMonth(DateUtil.getMonthStr(calendar.getTime()));
        invalidate();
    }

    public Long getMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        return calendar.getTime().getTime();
    }

    public String getDateStr(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        String YMstr = DateUtil.getMonthStrYM(calendar.getTime());
        return DateUtil.getDateStrYmd(DateUtil.str2Dates(YMstr+"-"+selectDay));
    }

    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        return DateUtil.getDateStrYmd(calendar.getTime());
    }
    public List<RemindCalendarModel> getList(){
        return list;
    }

    private onClickListener listener;
    public void setOnClickListener(onClickListener listener){
        this.listener = listener;
    }
    public interface onClickListener{

        public abstract void onLeftRowClick();
        public abstract void onRightRowClick();
        public abstract void onTitleClick(String monthStr, Date month);
        public abstract void onWeekClick(int weekIndex, String weekStr);
        public abstract void onDayClick(int day, String dayStr);
    }



    /***********************接口API↑↑↑↑↑↑↑**************************/

}
