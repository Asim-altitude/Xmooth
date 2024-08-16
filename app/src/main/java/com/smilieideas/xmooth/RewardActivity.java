package com.smilieideas.xmooth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.google.common.collect.ImmutableList;
import com.smilieideas.xmooth.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class RewardActivity extends AppCompatActivity {
    private static final String TAG = "RewardActivity";


    String currentUid;
    int coins= 0;
    LinearLayout video,premium,cancel_btn;
    TextView watch_video;

    boolean unityReady = false, adAvailable= false;

    @Override
    public void onBackPressed() {
        finish();
    }

    int is_download_access = 0;
    SharedPreferences appPrefs;
    String email,id;
    LinearLayout premium_btn;
    TextView weekly_txt,item_price_txt,monthly_txt,yearly_txt,restore_btn,offer_txt,offer_txt1;

    boolean firstTime = false;

    String version = "diamond_version";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reward);


        firstTime = getIntent().getBooleanExtra("firstTime",false);

        appPrefs = getSharedPreferences(Constants.APP_PREFS,MODE_PRIVATE);


        is_download_access = getIntent().getIntExtra("down_access",0);

        item_price_txt = findViewById(R.id.item_price_txt);
        offer_txt = findViewById(R.id.offer_text);
        offer_txt1 = findViewById(R.id.offer_text_1);

        if (firstTime){
            offer_txt.setVisibility(View.VISIBLE);
            offer_txt1.setVisibility(View.VISIBLE);
        }else{
            offer_txt.setVisibility(View.GONE);
            offer_txt1.setVisibility(View.GONE);
        }

        cancel_btn = findViewById(R.id.cancel_btn);
        premium_btn = findViewById(R.id.premium_btn);
        premium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isItemReady){
                    launchPurchase();
                }else{
                    Toast.makeText(RewardActivity.this,"Item not ready. Wait for 5-10 seconds",Toast.LENGTH_SHORT).show();
                }
                /*if (firstTime){
                    startActivity(new Intent(RewardActivity.this, PurchaseUpdatedScreen.class)
                            .putExtra("version", "diamond_version"));
                    finish();
                }else {
                    startActivity(new Intent(RewardActivity.this, PurchaseUpdatedScreen.class)
                            .putExtra("version", "diamond_version"));
                    finish();
                }*/
            }
        });

        premium_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.black));
                    ((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.black));

                }else{
                    ((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.white));
                    ((TextView)((LinearLayout)v).getChildAt(1)).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.white));

                }
            }
        });

        cancel_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.black));

                }else{
                    ((TextView)((LinearLayout)v).getChildAt(0)).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.white));

                }
            }
        });
        restore_btn = findViewById(R.id.restore_btn);

        restore_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ((TextView)v).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.black));

                }else{
                    ((TextView)v).setTextColor(ContextCompat.getColor(RewardActivity.this,R.color.white));

                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        weekly_txt = findViewById(R.id.photo_version_btn);
        monthly_txt = findViewById(R.id.creator_version_btn);
        yearly_txt = findViewById(R.id.all_version_btn);


        restore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // checkDiamondAccess();
                queryPurchases(true);
            }
        });
       // restore_btn.setVisibility(View.GONE);

       weekly_txt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               try {
                   if (productDetails!= null) {
                       ProductDetails prod = null;
                       prod = getProduct("weekly_premium");

                       launchPurchase(prod,getOfferToken("weekly_premium"));
                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }


           }
       });

        monthly_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (productDetails!= null) {
                        ProductDetails prod = null;
                        prod = getProduct("monthly_premium");
                        launchPurchase(prod,getOfferToken("monthly_premium"));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        yearly_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if (productDetails!= null) {
                        ProductDetails prod = null;
                        prod = getProduct("premium_card");
                        launchPurchase(prod,getOfferToken("premium_card"));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



       // connectBillingClient();

       // restore_btn.setOnFocusChangeListener(onFocusChangeListener);
       // premium_btn.setOnFocusChangeListener(onFocusChangeListener);
       // cancel_btn.setOnFocusChangeListener(onFocusChangeListener);

        connectBillingClient();
    }

    ProgressDialog progressDialog;

    void showProgress(){
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Restoring purchase");
        progressDialog.show();
    }

    void hideProgress(){
        progressDialog.dismiss();
    }


    public void queryPurchases(Boolean consume){

        if (billingClient!=null) {
            showProgress();
            billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {

                @Override
                public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {

                    Log.e(TAG, "onQueryPurchasesResponse: "+list.size());
                    if (list.size() > 0){
                        appPrefs.edit().putBoolean(Constants.ISPREMIUM,true).apply();

                        try {

                            startActivity(new Intent(RewardActivity.this, MainActivity.class));
                            finish();

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }


    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b){
                zoomIn(view);
            }else{
                zoomOut(view);
            }
        }
    };

    void loadPurchases(String productType){
        try {
            if (billingClient.isReady()){

                showLoading("Restoring...");
                QueryPurchasesParams queryPurchasesParams = QueryPurchasesParams
                        .newBuilder().setProductType(productType).build();
                billingClient.queryPurchasesAsync(queryPurchasesParams, new PurchasesResponseListener() {
                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {

                        hideloading();
                        if (list!= null){
                            if (list.size() > 0){
                                for (int i=0;i<list.size();i++){
                                    if (list.get(i).getPurchaseState() == Purchase.PurchaseState.PURCHASED){
                                        handlePurchase(list.get(i));
                                    }
                                }

                            }else{
                                Toast.makeText(RewardActivity.this,"No purchases found",Toast.LENGTH_LONG).show();

                            }
                        }else {
                            Toast.makeText(RewardActivity.this,"No purchases found",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    String getOfferToken(String tag){
        String token = null;

        for (int i=0;i<productDetails.size();i++) {
            if (productDetails.get(i).getProductId().equalsIgnoreCase(tag)){
                token = offer_token.get(i);
                break;
            }
        }

        return token;
    }
    ProductDetails getProduct(String tag){
        ProductDetails prod = null;

        for (int i=0;i<productDetails.size();i++) {
            if (productDetails.get(i).getProductId().equalsIgnoreCase(tag)){
                prod = productDetails.get(i);
                break;
            }
        }

        return prod;
    }
    private BillingClient billingClient;
    boolean isConnected = false;
    void connectBillingClient(){

        showLoading("Connecting...");

        billingClient = BillingClient.newBuilder(RewardActivity.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                try{
                    hideloading();
                    if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.

                        if (!isConnected) {

                            isConnected = true;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    restore_btn.setVisibility(View.VISIBLE);

                                }
                            });
                            getProductDetails();

                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                isConnected = false;
            }
        });
    }


    List<ProductDetails> productDetails;
    List<String> productOfferTokens;

    List<String> product_ids,base_product_price,offer_product_price,offer_token;
    void getProductDetails() {

        product_ids = new ArrayList<>();
        base_product_price = new ArrayList<>();
        offer_product_price = new ArrayList<>();
        offer_token = new ArrayList<>();


        showLoading("Loading products...");
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("diamond_version")
                                                .setProductType(BillingClient.ProductType.INAPP)
                                                .build())).build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult,
                                                         @NonNull List<ProductDetails> productDetailsList) {
                        // check billingResult
                        // process returned productDetailsList

                        hideloading();
                        if (!productDetailsList.isEmpty()){
                            isItemReady = true;
                            productDetails = productDetailsList;
                            Log.e(TAG, "onProductDetailsResponse: "+productDetailsList.get(0).getOneTimePurchaseOfferDetails().getFormattedPrice());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    item_price_txt.setText(productDetailsList.get(0).getOneTimePurchaseOfferDetails().getFormattedPrice());

                                }
                            });
                        }

                      /*  if (productDetailsList.size() > 0) {
                            productDetails = productDetailsList;

                            for (ProductDetails productDetails : productDetailsList) {

                                offer_token.add(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken());

                                if (productDetails.getProductId().equals("monthly_premium")) {

                                    product_ids.add("monthly_premium");

                                    Log.e("monthly_premium", productDetails.getSubscriptionOfferDetails().get(0).getOfferToken());
                                    for (ProductDetails.PricingPhase pr : productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList()){
                                        Log.e(TAG, pr.getFormattedPrice() + " Per Month "+" RECURRENCE MODE"+pr.getRecurrenceMode());

                                        if (pr.getRecurrenceMode() == 1){
                                           // monthly_txt.setText(pr.getFormattedPrice().toString()+" Monthly");
                                            base_product_price.add(pr.getFormattedPrice().toString()+" Monthly");
                                        }else if (pr.getRecurrenceMode() == 2){
                                            // monthly_txt.setText(pr.getFormattedPrice().toString()+" Monthly");
                                            offer_product_price.add(pr.getFormattedPrice().toString()+" Monthly");

                                        }
                                    }
                                }


                                else if (productDetails.getProductId().equals("premium_card")) {

                                    product_ids.add("premium_card");
                                    Log.e("premium_card", productDetails.getSubscriptionOfferDetails().get(0).getOfferToken());

                                    for (ProductDetails.PricingPhase pr : productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList()){
                                        Log.e(TAG, pr.getFormattedPrice() + " Per Year "+" RECURRENCE MODE"+pr.getRecurrenceMode());

                                        if (pr.getRecurrenceMode() == 1){
                                            base_product_price.add(pr.getFormattedPrice().toString()+" Annual");
                                        }else if (pr.getRecurrenceMode() == 2){
                                            offer_product_price.add(pr.getFormattedPrice().toString()+" Annual");
                                        }

                                    }


                                }

                                else if (productDetails.getProductId().equals("weekly_premium")) {

                                    product_ids.add("weekly_premium");
                                    Log.e("weekly_premium", productDetails.getSubscriptionOfferDetails().get(0).getOfferToken());

                                    for (ProductDetails.PricingPhase pr : productDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList()){
                                        Log.e(TAG, pr.getFormattedPrice() + " Per Week "+" RECURRENCE MODE"+pr.getRecurrenceMode());

                                        if (pr.getRecurrenceMode() == 1){
                                             base_product_price.add(pr.getFormattedPrice().toString()+" Weekly");
                                        }else  if (pr.getRecurrenceMode() == 2){
                                            offer_product_price.add(pr.getFormattedPrice().toString()+" Weekly");
                                        }

                                    }


                                }
                            }

                        }*/

                       /* runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < product_ids.size(); i++) {
                                    if (product_ids.get(i).equalsIgnoreCase("weekly_premium")) {
                                        weekly_txt.setText(base_product_price.get(i).toString());
                                    } else if (product_ids.get(i).equalsIgnoreCase("premium_card")) {
                                        yearly_txt.setText(base_product_price.get(i).toString());
                                    } else if (product_ids.get(i).equalsIgnoreCase("monthly_premium")) {
                                        monthly_txt.setText(base_product_price.get(i).toString());
                                    }
                                }
                            }
                        });*/

                    }

                });
    }
    void launchPurchase(ProductDetails product,String offer_token){

        if (productDetails == null)
            return;

        ImmutableList productDetailsParamsList;
        String offerToken = "";


        productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(product)
                                .setOfferToken(offer_token)
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

// Launch the billing flow
        BillingResult billingResult = billingClient.launchBillingFlow(this, billingFlowParams);
    }
    void showLoading(String message){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.show();
        }else{
            progressDialog.show();
        }
    }

    boolean isItemReady = false;
    void launchPurchase(){

        if (!isItemReady){

            Toast.makeText(RewardActivity.this,"Loading data please wait",Toast.LENGTH_LONG).show();
            return;
        }

        if (productDetails == null)
            return;

        if (productDetails.size() == 0) {
            Toast.makeText(RewardActivity.this, "No products available to purchase. Try again", Toast.LENGTH_LONG).show();

            return;
        }

        ImmutableList productDetailsParamsList;
        String offerToken = "";

        productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails.get(0))
                                .build()
                );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();

        // Launch the billing flow
        BillingResult billingResult = billingClient.launchBillingFlow(this, billingFlowParams);
    }


    void hideloading(){
        if (progressDialog!=null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private float mItemScaleFactor = 1.2f;
    private int mItemScaleDuration = 500;
    private void zoomIn(View view) {
        if (view != null) {
            view.animate().scaleX(mItemScaleFactor).scaleY(mItemScaleFactor).setDuration(mItemScaleDuration).start();
        }
    }

    // 缩小
    private void zoomOut(View view) {
        if (view != null) {
            view.animate().scaleX(1).scaleY(1).setDuration(mItemScaleDuration).start();
        }
    }


    private PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
            // To be implemented in a later section.

            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                Toast.makeText(RewardActivity.this,"Cancelled by User",Toast.LENGTH_SHORT).show();
            } else {
                // Handle any other error codes.
                Toast.makeText(RewardActivity.this,"Error in purchase",Toast.LENGTH_SHORT).show();
              //  Constants.addEvent(PurcRewardActivityhaseUpdatedScreen.this,"InAppPurchase",billingResult.getDebugMessage(),billingResult.getResponseCode()+"");
            }
        }
    };

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                Log.e(TAG, "onAcknowledgePurchaseResponse: ACKNOWLEDGED");
                startActivity(new Intent(RewardActivity.this,MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(RewardActivity.this,MainActivity.class));
                finish();
            }
        }
    };

    public void handlePurchase(Purchase purchase) {
        try {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

                if (version.equalsIgnoreCase("diamond_version")){
                    appPrefs.edit().putBoolean(Constants.ISPREMIUM, true).apply();

                    try
                    {
                        if (!purchase.isAcknowledged()) {
                            AcknowledgePurchaseParams acknowledgePurchaseParams =
                                    AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(purchase.getPurchaseToken())
                                            .build();
                            billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);

                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            startActivity(new Intent(RewardActivity.this,MainActivity.class));
            finish();
        }
    }

}