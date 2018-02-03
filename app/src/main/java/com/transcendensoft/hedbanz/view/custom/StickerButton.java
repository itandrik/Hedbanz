package com.transcendensoft.hedbanz.view.custom;
/**
 * Copyright 2017. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.util.AndroidUtils;

/**
 * Custom button for navigation.
 * It contains push pin, sticker background and text
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class StickerButton extends View {
    private String mText;
    private int mWidth;
    private int mHeight;
    private Context mContext;

    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private RectF mBackgroundRect;

    public StickerButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initAttributes(context, attrs);
        initPaints();
    }

    private void initAttributes(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StickerButton, 0, 0);
        try {
            mText = ta.getString(R.styleable.StickerButton_text);
        } finally {
            ta.recycle();
        }
    }

    private void initPaints() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        int colorLight = ContextCompat.getColor(mContext, R.color.colorPrimary);
        int colorDark = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);

        mBackgroundPaint.setShader(new LinearGradient(0, 0, mWidth, mHeight, colorLight,
                colorDark, Shader.TileMode.CLAMP));

        mTextPaint = new Paint();
        mTextPaint.setTextSize(AndroidUtils.convertSpToPixels(18, mContext));
        mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.textPrimary));
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mBackgroundRect = new RectF(0, 0, mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cornerRadius = (int) AndroidUtils.convertDpToPixel(8, mContext);

        canvas.drawRoundRect(mBackgroundRect, cornerRadius, cornerRadius, mBackgroundPaint);
        canvas.drawText(mText, mWidth / 2, mHeight / 2, mTextPaint);
        drawPushPin(canvas);
    }

    private void drawPushPin(Canvas canvas) {
        Drawable drawable = VectorDrawableCompat.create(
                getResources(), R.drawable.ic_push_pin, null);
        if (drawable != null) {
            int size = (int) AndroidUtils.convertDpToPixel(32, mContext);
            drawable.setBounds(0, 0, size, size);
            canvas.translate(mWidth / 2, -AndroidUtils.convertDpToPixel(16, mContext));
            drawable.draw(canvas);
        }
    }
}
