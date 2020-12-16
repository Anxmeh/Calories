package com.example.caloriescounter.network;

import com.example.caloriescounter.models.AddProductView;
import com.example.caloriescounter.models.Dish;
import com.example.caloriescounter.models.DishIngredientsView;
import com.example.caloriescounter.models.Ingredients;
import com.example.caloriescounter.models.LoginView;
import com.example.caloriescounter.models.Product;
import com.example.caloriescounter.models.RegisterView;

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

    @POST("/api/account/register")
    Call<Tokens> register(@Body RegisterView model);

       @GET("/api/products/products")
   Call<List<Product>> getProducts();

    @POST("/api/products/addproduct")
    Call<Product> addProduct(@Body AddProductView model);

    @POST("/api/products/removeproduct")
    Call<Product> removeProduct(@Body DishIngredientsView model);

    @POST("/api/products/addproducttodish")
    Call<Product> addProductToDish(@Body DishIngredientsView model);

    @POST("/api/products/removeproductindish")
    Call<Product> removeProductInDish(@Body DishIngredientsView model);

    @GET("/api/dish/calculatedish")
    Call<Dish> calculateDish();

    @GET("/api/dish/productsindish")
    Call<List<Ingredients>> getProductsinDish();
//
//    @POST("/api/profile/info")
//    Call<UserView> profile();
//
//    @POST("/api/profile/update")
//    Call<UserView> update(@Body UserView profile);
//
//    @POST("/api/profile/update-photo")
//    Call<UserView> updatePhoto(@Body Photo photo);
//

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

