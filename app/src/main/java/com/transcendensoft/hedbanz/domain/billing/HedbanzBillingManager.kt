package com.transcendensoft.hedbanz.domain.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import timber.log.Timber

/**
 * Copyright 2017. Andrii Chernysh
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
 * Manager that processes all needed staff,
 * related with inApp purchases
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 *         Developed by <u>Transcendensoft</u>
 */
class HedbanzBillingManager(context: Context) : PurchasesUpdatedListener {
    private val billingClient = BillingClient.newBuilder(context).setListener(this).build()
    private var isConnected: Boolean = false

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        Timber.i("OnPurchasesUpdated. Response code: " +
                "${getBillingResponseCodeString(responseCode)} " +
                "Purchases: ${purchases?.joinToString(separator = ", ")}")
    }

    fun initBilling(onStartInitialization: () -> Unit,
                    onSetupFinished: (responseCode: Int, responseMessage: String) -> Unit,
                    onServiceDisconnected: () -> Unit) {
        onStartInitialization()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(responseCode: Int) {
                Timber.i("BILLING. onBillingSetupFinished. Response code: " +
                        "${getBillingResponseCodeString(responseCode)} ")
                isConnected = true
                onSetupFinished(responseCode, getBillingResponseCodeString(responseCode))
            }

            override fun onBillingServiceDisconnected() {
                Timber.i("BILLING. onBillingServiceDisconnected")
                isConnected = false
                onServiceDisconnected()
            }
        })
    }

    private fun queryAvailable(type: String,
                               onStart: () -> Unit,
                               onComplete: (responseCode: Int, responseMessage: String,
                                            skuDetailsList: List<SkuDetails>) -> Unit,
                               onError: (errorMessage: String) -> Unit) {
        val params = SkuDetailsParams.newBuilder()
        //TODO complete this strings when google play account will be ready
        val skuList = listOf("todo 1", "todo 2")
        params.setSkusList(skuList).setType(type)

        onStart()
        if (!isConnected) {
            onError(getBillingResponseCodeString(BillingClient.BillingResponse.SERVICE_DISCONNECTED))
        } else {
            billingClient.querySkuDetailsAsync(params.build()) { responseCode, skuDetailsList ->
                Timber.i("BILLING. querySkuDetailsAsync. Response: " +
                        "${getBillingResponseCodeString(responseCode)}. Sku list: " +
                        skuDetailsList.joinToString(separator = ", "))
                onComplete(responseCode, getBillingResponseCodeString(responseCode), skuDetailsList)
            }
        }
    }

    fun queryAvailablePurchases(onStart: () -> Unit,
                                onComplete: (responseCode: Int, responseMessage: String,
                                             skuDetailsList: List<SkuDetails>) -> Unit,
                                onError: (errorMessage: String) -> Unit) {
        queryAvailable(BillingClient.SkuType.INAPP, onStart, onComplete, onError)
    }

    fun queryAvailableSubscriptions(onStart: () -> Unit,
                                    onComplete: (responseCode: Int, responseMessage: String,
                                                 skuDetailsList: List<SkuDetails>) -> Unit,
                                    onError: (errorMessage: String) -> Unit) {
        queryAvailable(BillingClient.SkuType.SUBS, onStart, onComplete, onError)
    }

    private fun query(type: String,
                      onStart: () -> Unit,
                      onComplete: (responseCode: Int, responseMessage: String,
                                   skuDetailsList: List<Purchase>) -> Unit,
                      onError: (errorMessage: String) -> Unit) {
        onStart()
        if (!isConnected) {
            onError(getBillingResponseCodeString(BillingClient.BillingResponse.SERVICE_DISCONNECTED))
        } else {
            billingClient.queryPurchaseHistoryAsync(type) { responseCode, purchasesList ->
                Timber.i("BILLING. queryPurchaseHistoryAsync. Response: " +
                        "${getBillingResponseCodeString(responseCode)}. Sku list: " +
                        purchasesList.joinToString(separator = ", "))
                onComplete(responseCode, getBillingResponseCodeString(responseCode), purchasesList)
            }
        }
    }

    fun queryPurchases(onStart: () -> Unit,
                       onComplete: (responseCode: Int, responseMessage: String,
                                    purchaseList: List<Purchase>) -> Unit,
                       onError: (errorMessage: String) -> Unit) {
        query(BillingClient.SkuType.INAPP, onStart, onComplete, onError)
    }

    fun querySubscriptions(onStart: () -> Unit,
                           onComplete: (responseCode: Int, responseMessage: String,
                                        purchaseList: List<Purchase>) -> Unit,
                           onError: (errorMessage: String) -> Unit) {
        query(BillingClient.SkuType.SUBS, onStart, onComplete, onError)
    }

    fun consumePurchase(purchaseToken: String,
                        onStart: () -> Unit,
                        onComplete: (responseCode: Int, responseMessage: String) -> Unit,
                        onError: (errorMessage: String) -> Unit) {
        if(isConnected) {
            onStart()
            billingClient.consumeAsync(purchaseToken) { responseCode, _ ->
                onComplete(responseCode, getBillingResponseCodeString(responseCode))
            }
        } else {
            onError(getBillingResponseCodeString(BillingClient.BillingResponse.SERVICE_UNAVAILABLE))
        }
    }

    private fun purchase(activity: Activity, skuId: String, type: String): Int{
        val flowParams = BillingFlowParams.newBuilder()
                .setSku(skuId)
                .setType(type)
                .build()

        return billingClient.launchBillingFlow(activity, flowParams)
    }

    fun purchaseSubscription(activity: Activity, skuId: String){
        purchase(activity, skuId, BillingClient.SkuType.SUBS)
    }

    fun purchaseItem(activity: Activity, skuId: String){
        purchase(activity, skuId, BillingClient.SkuType.INAPP)
    }

    fun getBillingResponseCodeString(responseCode: Int) =
            when (responseCode) {
                BillingClient.BillingResponse.BILLING_UNAVAILABLE -> "Billing unavailable." +
                        " Version for the Billing API is not supported for the requested type"
                BillingClient.BillingResponse.DEVELOPER_ERROR -> "Developer error." +
                        " Incorrect arguments have been sent to the Billing API"
                BillingClient.BillingResponse.ERROR -> "Error. An error occurs" +
                        " during the API action being executed"
                BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED -> "Feature not supported." +
                        " Requested action is not supported by play services on the current device"
                BillingClient.BillingResponse.ITEM_ALREADY_OWNED -> "ITEM_ALREADY_OWNED." +
                        " User attempts to purchases an item that they already own"
                BillingClient.BillingResponse.ITEM_NOT_OWNED -> "ITEM_NOT_OWNED." +
                        " User attempts to consume an item that they do not currently own"
                BillingClient.BillingResponse.ITEM_UNAVAILABLE -> "ITEM_UNAVAILABLE." +
                        " User attempts to purchases a product that is not available for purchase"
                BillingClient.BillingResponse.OK -> "OK. Request is successful"
                BillingClient.BillingResponse.SERVICE_DISCONNECTED -> "SERVICE_DISCONNECTED." +
                        " Play service is not connected at the point of the request"
                BillingClient.BillingResponse.SERVICE_UNAVAILABLE -> "SERVICE_UNAVAILABLE." +
                        " An error occurs in relation to the devices network connectivity"
                BillingClient.BillingResponse.USER_CANCELED -> "USER cancelled." +
                        " User cancels the request that is currently taking place"
                else -> "Unknown response code"
            }
}