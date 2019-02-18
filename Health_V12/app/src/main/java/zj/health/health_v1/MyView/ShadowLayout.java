package zj.health.health_v1.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/5/3.
 */

public class ShadowLayout extends RelativeLayout{

    private Paint mPaint = null;

    public ShadowLayout(Context context) {
        super(context);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色
        mPaint.setShadowLayer(10F, 15F, 15F, Color.GRAY);
        RectF rect = new RectF(0 , 0, 200, 200);
//        canvas.drawRect(rect, (float)75, (float)75, mPaint);
    }
}
