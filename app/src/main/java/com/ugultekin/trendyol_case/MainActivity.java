package com.ugultekin.trendyol_case;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.ugultekin.trendyol_case.models.Product;
import com.ugultekin.trendyol_case.models.Social;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    ImageView img_view_product;
    TextView text_view_brand, text_view_desc, text_view_like_count,
            text_view_rate_bar, text_view_rate_bar_comment, text_view_price;
    RatingBar rating_bar;
    Handler handler = new Handler();
    Runnable refresh;

    DonutProgress donut_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initViews();
        // We set product data to textviews
        getJsonDataProduct();

        // We are refreshing social datas
        refresh = new Runnable() {
            public void run() {
                try {
                    getJsonDataSocial();

                    // Showing refresh timer
                    new CountDownTimer(20000, 1000) {
                        int i = 20;
                        public void onTick(long millisUntilFinished) {
                            donut_progress.setProgress(i);
                            i--;
                        }
                        public void onFinish() {
                        }
                    }.start();

                    Log.d(TAG, "run: It's working");
                } catch (Exception e) {
                    Log.d(TAG, "run: It's not working");
                }

                handler.postDelayed(refresh, 20000);
            }
        };
        handler.post(refresh);
    }

    private void initViews() {
        img_view_product = findViewById(R.id.img_view_product);
        text_view_brand = findViewById(R.id.text_view_brand);
        text_view_desc = findViewById(R.id.text_view_desc);
        text_view_like_count = findViewById(R.id.text_view_like_count);
        text_view_rate_bar = findViewById(R.id.text_view_rate_bar);
        text_view_rate_bar_comment = findViewById(R.id.text_view_rate_bar_comment);
        text_view_price = findViewById(R.id.text_view_price);
        rating_bar = findViewById(R.id.rating_bar);
        donut_progress = (DonutProgress) findViewById(R.id.donut_progress);
        donut_progress.setMax(20);
    }

    // We get product data from product.json
    public void getJsonDataProduct() {
        Product product = new Product();

        try {
            //Load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.product)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();
            product = gson.fromJson(jsonBuilder.toString(), Product.class);

            // money format
            DecimalFormat formatter = new DecimalFormat("#,###.00");


            // loading image
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.errorimage)
                    .error(R.drawable.errorimage)
                    .dontTransform()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);

            Glide.with(this)
                    .load(product.getImage())
                    .apply(options)
                    .into(img_view_product);

            text_view_brand.setText(product.getName());
            text_view_desc.setText(product.getDesc());
            text_view_price.setText(formatter.format(product.getPrice().getValue()) + " " + product.getPrice().getCurrency());


        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        }
    }

    // We get social data from social.json
    public void getJsonDataSocial() {
        Social social = new Social();

        try {
            //Load File
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.social)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null; ) {
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();
            social = gson.fromJson(jsonBuilder.toString(), Social.class);

            text_view_rate_bar.setText(social.getCommentCounts().getAverageRating().toString());
            rating_bar.setRating(social.getCommentCounts().getAverageRating().floatValue());
            int sumComment = social.getCommentCounts().getAnonymousCommentsCount() + social.getCommentCounts().getMemberCommentsCount();
            text_view_rate_bar_comment.setText("(" + sumComment + " yorum)");

            text_view_like_count.setText(social.getLikeCount().toString());


        } catch (FileNotFoundException e) {
            Log.e("jsonFile", "file not found");
        } catch (IOException e) {
            Log.e("jsonFile", "ioerror");
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
