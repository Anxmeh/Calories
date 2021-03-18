package com.example.caloriescounter.network;

import android.database.Observable;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.AddUserDailyWeightViewModel;
import com.example.caloriescounter.models.AddVitaminView;
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
import com.example.caloriescounter.models.SetWaterTimeView;
import com.example.caloriescounter.models.UserDailyWeight;
import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.models.UserVitaminsDailyView;
import com.example.caloriescounter.models.UserVitaminsView;
import com.example.caloriescounter.models.Vitamin;
import com.example.caloriescounter.models.VitaminDailyCheckView;
import com.example.caloriescounter.models.WaterProgressView;
import com.example.caloriescounter.models.WaterSettingsView;
import com.example.caloriescounter.models.WaterTimeView;
import com.example.caloriescounter.models.WaterView;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JSONPlaceHolderApi {

    @POST("/api/account/login")
    Call<Tokens> login(@Body LoginView model);

    @POST("/api/account/logingoogle")
    Call<Tokens> loginGoogle(@Body LoginGoogleView model);

    @POST("/api/account/register")
    Call<Tokens> register(@Body RegisterView model);

    @POST("/api/account/signout")
    Call<Void> signout();

    @GET("/api/products/products")
    Call<List<Product>> getProducts();

    @POST("/api/products/addproduct")
    Call<Product> addProduct(@Body AddProductView model);

    @POST("/api/water/dailywater")
    Call<WaterView> getWater(@Body Date dateOfDrink);

    @POST("/api/water/addwater")
    Call<WaterView> addWater(@Body WaterView model);

    @GET("/api/water/progresswater")
    Call<WaterProgressView> getWaterProgress();

    @GET("/api/watersettings/watersettings")
    Call<WaterTimeView> getWaterSettings();

    @POST("/api/watersettings/setdailyvolume")
    Call<WaterTimeView> setdailyvolume(@Body int volume);

    @POST("/api/watersettings/setbegin")
    Call<WaterTimeView> setWaterBegin(@Body SetWaterTimeView model);

    @POST("/api/watersettings/setend")
    Call<WaterTimeView> setWaterEnd(@Body SetWaterTimeView model);

    @POST("/api/products/addproducttodish")
    Call<Product> addProductToDish(@Body DishIngredientsView model);

    @POST("/api/products/removeproductindish")
    Call<Dish> removeProductInDish(@Body Ingredients model);

    @POST("/api/products/removeproduct")
    Call<Void> removeProduct(@Body long productId);

    @GET("/api/dish/calculatedish")
    Call<Dish> calculateDish();

    @GET("/api/dish/productsindish")
    Call<List<Ingredients>> getProductsinDish();

    @POST("/api/daily/dailymenu")
    Call<List<DailyMenuView>> getProductsDailyMenu(@Body Date dateOfMeal);

    @POST("/api/daily/adddailyproduct")
    Call<DailyMenuView> addDailyProduct(@Body DailyMenuView model);

    @POST("/api/daily/removedailyproduct")
    Call<List<DailyMenuView>> removeDailyProduct(@Body RemoveDailyView model);

    @POST("/api/profile/info")
    Call<UserView> profile();

    @POST("/api/profile/update")
    Call<UserView> update(@Body UserView profile);

    @POST("/api/profile/update-photo")
    Call<UserView> updatePhoto(@Body Photo photo);

    @POST("/api/settings/settings")
    Call<UserSettingsView> settings();

    @POST("/api/settings/updatesettings")
    Call<UserSettingsView> updateSettings(@Body UserSettingsView userSettings);

    @POST("/api/settings/setusercalories")
    Call<UserSettingsView> updateCalories(@Body double userCalories);

    @GET("/api/vitamins/allvitamins")
    Call<List<Vitamin>> getAllVitamins();

    @POST("/api/vitamins/addvitamin")
    Call<List<Vitamin>> addVitamin(@Body AddVitaminView model);

    @GET("/api/vitamins/myvitamins")
    Call<List<UserVitaminsView>> getUserVitamins();

    @POST("/api/vitamins/addmyvitamin")
    Call<UserVitaminsView> addUserVitamin(@Body UserVitaminsView model);

    @POST("/api/vitamins/removemyvitamin")
    Call<List<UserVitaminsView>>removeUserVitamin(@Body long vitaminId);

    @POST("/api/vitamins/сhangemyvitamin")
    Call<UserVitaminsView> changeUserVitamin(@Body UserVitaminsView model);

    @POST("/api/vitamins/dailyvitamins")
    Call<List<UserVitaminsDailyView>> getDailyVitamins(@Body Date dateOfVitamin);

    @POST("/api/vitamins/checkvitamin")
    Call<VitaminDailyCheckView> checkDailyVitamin(@Body VitaminDailyCheckView model);

    @GET("/api/userdailyweight/userdailyweights")
    Call<List<UserDailyWeight>> getDailyWeight();

    @POST("/api/userdailyweight/userweight")
    Call<AddUserDailyWeightViewModel> getUserWeight(@Body Date date);

    @POST("/api/userdailyweight/adduserdailyweight")
    Call<List<UserDailyWeight>> addDailyWeight();

    @POST("/api/userdailyweight/edituserdailyweight")
    Call<AddUserDailyWeightViewModel> editDailyWeight(@Body AddUserDailyWeightViewModel model);
}

