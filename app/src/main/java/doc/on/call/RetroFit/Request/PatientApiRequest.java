package doc.on.call.RetroFit.Request;

import com.google.gson.JsonObject;
import doc.on.call.Model.Patient;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PatientApiRequest {

    /**
     * ============================== START OF LOGIN ==============================
     */
    @POST("api/user/login")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> loginPatient(@Body JsonObject jsonObject);

    @POST("api/user/logout")
    Call<ResponseBody> logoutPatient();

    @POST("api/patient/register")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> registerPatient(@Body JsonObject jsonObject);

    @POST("api/user/otp/validate")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> validatePatient(@Body JsonObject jsonObject);

    @POST("api/patient/verify")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> verifyPatient(@Body JsonObject jsonObject);
    /**
     * ============================== END OF LOGIN ==============================
     */
    /**
     * ============================== START OF PATIENT ==============================
     */
    @GET("api/testing/patients")
    @Headers({"No-Authentication: true"})
    Call<List<Patient>> getAllPatients();

    @GET("api/patient")
    Call<Patient> getPatient();
    /**
     * ============================== END OF PATIENT ==============================
     */
}