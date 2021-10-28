package com.example.recommend;

/**
 * Created by Haoran Lin on 2021/10/26.
 */
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class CustomDialog extends Dialog{


    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,layout,style, Gravity.CENTER);
    }


    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity, int anim){
        super(context,style);

        setContentView(layout);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.drawable.city_item_mask);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }


    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity){
        this(context,width,height,layout,style,gravity,R.style.pop_anim_style);
    }

}
