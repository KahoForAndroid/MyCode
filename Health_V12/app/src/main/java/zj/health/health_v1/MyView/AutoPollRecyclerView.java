package zj.health.health_v1.MyView;

        import android.content.Context;
        import android.support.annotation.Nullable;
        import android.support.v7.widget.RecyclerView;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.ViewConfiguration;

        import java.lang.ref.WeakReference;

/**
 * 具有走马灯效果的recycleView
 * Created by Administrator on 2018/4/9.
 */

public class AutoPollRecyclerView extends RecyclerView{

    public static final long TIME_AUTO_POLL = 16;
//    public static final long TIME_AUTO_POLL_1 = 4000;
    private AutoPollTask autoPollTask;
    private int index=0;
    private boolean running = true; //标示是否正在自动轮询
    private boolean canRun = true;//标示是否可以自动轮询,可在不需要的是否置false
    private final int mTouchSlop;
    public int lastY=0;

    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        autoPollTask = new AutoPollTask(this);
        //获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    static class AutoPollTask implements Runnable{
        private final WeakReference<AutoPollRecyclerView> mReference;

        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }

        @Override
        public void run() {
            AutoPollRecyclerView pollRecyclerView = mReference.get();
            if(pollRecyclerView!=null && pollRecyclerView.running && pollRecyclerView.canRun){
                pollRecyclerView.scrollBy(2,2);
                pollRecyclerView.postDelayed(pollRecyclerView.autoPollTask,pollRecyclerView.TIME_AUTO_POLL);
            }
        }
    }

    public void start(){
        if(running){
            stop();
            canRun = true;
            running = true;
            postDelayed(autoPollTask,TIME_AUTO_POLL);
        }
    }
    public void stop(){
        running = false;
        removeCallbacks(autoPollTask);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getRawY();
                if(running){
                    stop();
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
                int nowY = (int) ev.getRawY();
                if(nowY - lastY>mTouchSlop){
                    smoothScrollToPosition(index==0?0:--index);
                    if(canRun){
                        start();
                        return true;
                    }else if(lastY-nowY>mTouchSlop){
                        smoothScrollToPosition(++index);
                        if(canRun){
                            start();
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!running){
                    running =true;
                    start();
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
