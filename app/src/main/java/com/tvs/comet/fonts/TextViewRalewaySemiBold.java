package com.tvs.comet.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by UITOUX10 on 10/12/17.
 */

public class TextViewRalewaySemiBold extends AppCompatTextView{
    public TextViewRalewaySemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.ttf"));
    }
}
