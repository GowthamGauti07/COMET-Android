package com.tvs.comet.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by UITOUX10 on 10/16/17.
 */

public class TextViewOpenSansRegular extends AppCompatTextView {
    public TextViewOpenSansRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf"));
    }
}
