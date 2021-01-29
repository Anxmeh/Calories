package com.example.caloriescounter.network;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.DailyMenuView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.LoginGoogleView;
import com.example.caloriescounter.models.LoginView;
import com.example.caloriescounter.models.Photo;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.RegisterView;
import com.example.caloriescounter.models.RemoveDailyView;
import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.models.Vitamin;
import com.example.caloriescounter.models.WaterSettingsView;
import com.example.caloriescounter.models.WaterView;

import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JSONPlaceHolderApi {
    @POST("/api/account/login")
    Call<Tokens> login(@Body LoginView model);

    @POST("/api/account/logingoogle")
    Call<Tokens> loginGoogle(@Body LoginGoogleView model);

    @POST("/api/account/register")
    Call<Tokens> register(@Body RegisterView model);

    @GET("/api/products/products")
    Call<List<Product>> getProducts();

    @POST("/api/products/addproduct")
    Call<Product> addProduct(@Body AddProductView model);

    @POST("/api/water/dailywater")
    Call<WaterView> getWater(@Body Date dateOfDrink);

    @POST("/api/water/addwater")
    Call<WaterView> addWater(@Body WaterView model);

    @GET("/api/watersettings/watersettings")
    Call<WaterSettingsView> getWaterSettings();


    /////////
//    @POST("/api/products/removeproduct")
//    Call<List<Product>> removeProduct(@Body DishIngredientsView model);
    ////////

    @POST("/api/products/addproducttodish")
    Call<Product> addProductToDish(@Body DishIngredientsView model);

//    @POST("/api/products/removeproductindish")
//    Call<Product> removeProductInDish(@Body Ingredients model);

    @POST("/api/products/removeproductindish")
    Call<Dish> removeProductInDish(@Body Ingredients model);

    @POST("/api/products/removeproduct")
    Call<List<Product>> removeProduct(@Body long productId);

    @GET("/api/dish/calculatedish")
    Call<Dish> calculateDish();

    @GET("/api/dish/productsindish")
    Call<List<Ingredients>> getProductsinDish();

    @POST("/api/daily/dailymenu")
    Call<List<DailyMenuView>> getProductsDailyMenu(@Body Date dateOfMeal);

    @POST("/api/daily/adddailyproduct")
    Call<DailyMenuView> addDailyProduct(@Body DailyMenuView model);

//    @POST("/api/daily/removedailyproduct")
//    Call<DailyMenuView> removeDailyProduct(@Body RemoveDailyView model );

    @POST("/api/daily/removedailyproduct")
    Call<List<DailyMenuView>> removeDailyProduct(@Body RemoveDailyView model);


    //
    @POST("/api/profile/info")
    Call<UserView> profile();

    //
    @POST("/api/profile/update")
    Call<UserView> update(@Body UserView profile);

    //
    @POST("/api/profile/update-photo")
    Call<UserView> updatePhoto(@Body Photo photo);

    //
    @POST("/api/settings/settings")
    Call<UserSettingsView> settings();

    @POST("/api/settings/updatesettings")
    Call<UserSettingsView> updateSettings(@Body UserSettingsView userSettings);

    @POST("/api/settings/setusercalories")
    Call<UserSettingsView> updateCalories(@Body double userCalories);


    @GET("/api/vitamins/allvitamins")
    Call<List<Vitamin>> getAllVitamins();
    @GET("/api/vitamins/myvitamins")
    Call<List<UserVitaminsView>> getUserVitamins();
    @POST("/api/vitamins/addmyvitamin")
    Call<UserVitaminsView> addUserVitamin(@Body UserVitaminsView model);
    @POST("/api/vitamins/сhangemyvitamin")
    Call<UserVitaminsView> changeUserVitamin(@Body UserVitaminsView model);


//
//    @POST("/api/library/addbook")
//    Call<Book> addBook(@Body Book book);
//
//    @Multipart
//    @POST("/api/file/upload")
//    Call uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
//
//    @Multipart
//    @POST("retrofit_example/upload_multiple_files.php")
//    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
//                                       @Part MultipartBody.Part file2);file2

}

