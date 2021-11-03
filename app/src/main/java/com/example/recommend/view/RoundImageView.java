package com.example.recommend.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自定义圆角imageview
 */
public class RoundImageView extends AppCompatImageView {
    float FILLET = 18;
    float width, height;

    public RoundImageView(Context context) {
        this(context, null);
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= FILLET && height > FILLET) {
            Path path = new Path();
            //四个圆角
            path.moveTo(FILLET, 0);
            path.lineTo(width - FILLET, 0);
            path.quadTo(width, 0, width, FILLET);
            path.lineTo(width, height - FILLET);
            path.quadTo(width, height, width - FILLET, height);
            path.lineTo(12, height);
            path.quadTo(0, height, 0, height - FILLET);
            path.lineTo(0, FILLET);
            path.quadTo(0, 0, FILLET, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

}

