package com.transcendensoft.hedbanz.view.custom.transform;
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

import android.support.v4.view.ViewPager;
import android.view.View;

import static android.view.View.GONE;

/**
 * Transformer for vertical view pager.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
public class VerticalPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        if (position <= -1) {
            view.setAlpha(0);
            view.setVisibility(GONE);
        }else if(position <= 0){
            view.setVisibility(View.VISIBLE);
            view.setAlpha(position + 1);
            view.setTranslationX(view.getWidth() * -position);

            float yPosition = position * view.getHeight()* 0.05f;
            view.setTranslationY(yPosition);
        } else if (position <= 1) {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(1 - position);
            view.setTranslationX(view.getWidth() * -position);

            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        } else {
            view.setVisibility(GONE);
            view.setAlpha(0);
        }
    }
}
