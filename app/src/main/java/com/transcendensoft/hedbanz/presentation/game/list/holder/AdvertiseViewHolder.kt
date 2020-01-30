package com.transcendensoft.hedbanz.presentation.game.list.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.item_advertise.view.*
import timber.log.Timber

/**
 * Copyright 2018. Andrii Chernysh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * {@link android.support.v7.widget.RecyclerView.ViewHolder}
 * for view that represents advertise banner
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class AdvertiseViewHolder (mItemView: View) : RecyclerView.ViewHolder(mItemView) {
    private val adView = mItemView.adView

    fun bindAdvertise() {
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
        adView?.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Timber.i("OnAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                Timber.i("onAdFailedToLoad. Code: $errorCode")

            }

            override fun onAdOpened() {
                Timber.i("onAdOpened")
            }

            override fun onAdLeftApplication() {
                Timber.i("onAdLeftApplication")
            }

            override fun onAdClosed() {
                Timber.i("onAdClosed")
            }
        }
    }
}