package zj.health.health_v1.Utils;

import android.graphics.Paint;

/**
 * Created by Administrator on 2018/4/8.
 */

public class PaintUtil {

    /**
     * Kaho 2018.4.8
     * @param paint 画笔对象
     * @param contnet text内容
     * @return 返回该画笔对象和传入内容的长度
     */
    public static float getFontLength(Paint paint,String contnet){
        return paint.measureText(contnet);
    }

    /**
     * Kaho 2018.4.8
     * @param paint 画笔对象
     * @return 返回该画笔对象的文字高度
     */
    public static float getFontHeight(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    /**
     * Kaho 2018.4.8
     * @param paint 画笔对象
     * @return 返回画笔对象离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint){
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.leading - fontMetrics.ascent;
    }
}
