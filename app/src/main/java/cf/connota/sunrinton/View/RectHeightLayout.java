package cf.connota.sunrinton.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Conota on 2017-07-25.
 */

public class RectHeightLayout extends LinearLayout {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    public RectHeightLayout(Context context) {
        super(context);
    }

    public RectHeightLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectHeightLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
