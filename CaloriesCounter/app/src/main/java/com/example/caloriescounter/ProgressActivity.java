package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.models.WaterProgressView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.example.caloriescounter.network.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressActivity extends BaseActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private ImageRequester imageRequester;
    private ImageView imageRed;
    private ImageView imageBlack;
    private ImageView imageBronze;
    private ImageView imageSilver;
    private ImageView imageGold;
    private final String BASE_URL = NetworkService.getBaseUrl();
    private WaterProgressView waterProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_progress);
        this.getSupportActionBar().setTitle("Досягнення");
        imageRequester = ImageRequester.getInstance();

        imageRed = findViewById(R.id.imageRed);
        imageBlack = findViewById(R.id.imageBlack);
        imageBronze = findViewById(R.id.imageBronze);
        imageSilver = findViewById(R.id.imageSilver);
        imageGold = findViewById(R.id.imageGold);

        imageRed.setColorFilter(Color.argb(204, 255, 255, 255));
        imageBlack.setColorFilter(Color.argb(204, 255, 255, 255));
        imageBronze.setColorFilter(Color.argb(204, 255, 255, 255));

        imageSilver.setColorFilter(Color.argb(204, 255, 255, 255));
        imageGold.setColorFilter(Color.argb(204, 255, 255, 255));


        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getWaterProgress()
                .enqueue(new Callback<WaterProgressView>() {
                    @Override
                    public void onResponse(@NonNull Call<WaterProgressView> call, @NonNull Response<WaterProgressView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            waterProgress = response.body();
                            if (waterProgress.getWaterVolume() > 50000)
                                imageRed.setColorFilter(Color.argb(0, 255, 255, 255));
                            if (waterProgress.getWaterVolume() > 100000)
                                imageBlack.setColorFilter(Color.argb(0, 255, 255, 255));
                            if (waterProgress.getWaterVolume() > 150000)
                                imageBronze.setColorFilter(Color.argb(0, 255, 255, 255));
                            if (waterProgress.getWaterVolume() > 250000)
                                imageSilver.setColorFilter(Color.argb(0, 255, 255, 255));
                            if (waterProgress.getWaterVolume() > 500000)
                                imageGold.setColorFilter(Color.argb(0, 255, 255, 255));
                        } else {
                            String errorMessage;
                            try {
                                assert response.errorBody() != null;
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                errorMessage = response.message();
                                e.printStackTrace();
                            }
                            waterProgress = null;
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    errorMessage, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterProgressView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }


}