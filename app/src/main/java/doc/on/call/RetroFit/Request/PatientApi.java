package doc.on.call.RetroFit.Request;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PatientApi {

    @GET("api/patient")
    Call<PatientResponse> getPatients(
            @Query("email") String email
    );
}
