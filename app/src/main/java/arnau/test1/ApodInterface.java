package arnau.test1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by arnau on 15-Mar-18.
 */
public interface ApodInterface {
    @GET("/apod?api_key=vBU0ClxLwm9PiUE7JKxIBY4fsAYJ032gDsFjG1RG")
    Call<APODdata> getAPOD(@Query("date") String date);
}