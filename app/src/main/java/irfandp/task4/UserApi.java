package irfandp.task4;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by User on 9/28/2016.
 */
public interface UserApi {

    @GET("/users")
    Call<Users> getUsers();

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") String user_id);

    @PUT("/users/{id}")
    Call<User> updateUser(@Path("id") int user_id, @Body User user);

    @POST("/users")
    Call<User> saveUser(@Body User user);

    @DELETE("/users/{id}")
    Call<User> deleteUser(@Path("id") String user_id);

}
