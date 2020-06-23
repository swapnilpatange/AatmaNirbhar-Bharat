package com.aatmanirbhar.bharat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class TypefacedTextView extends android.support.v7.widget.AppCompatTextView {
    private Context context;
    public void setFontName(String fontName) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
        setTypeface(typeface);
    }

    String fontName;
    public TypefacedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        if (isInEditMode())
            return;
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
        styledAttrs.recycle();

        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            setTypeface(typeface);
        }
    }


}
