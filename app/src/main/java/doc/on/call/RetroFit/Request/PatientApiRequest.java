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

    @POST("api/user/otp/validate")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> validatePatient(@Body JsonObject jsonObject);

    @POST("api/user/logout")
    Call<ResponseBody> logoutPatient();

    @POST("api/patient/register")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> registerPatient(@Body JsonObject jsonObject);

    @POST("api/patient/register/resend")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> resendRegistrationToken(@Body JsonObject jsonObject);

    @POST("api/patient/verify")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> verifyPatient(@Body JsonObject jsonObject);
    /**
     * ============================== END OF LOGIN ==============================
     */
    /**
     * ============================== START OF PATIENT ==============================
     */
    @GET("api/patient")
    Call<Patient> getPatient();

    @POST("api/patient/appointment/schedule")
    Call<ResponseBody> createAppointment(@Body JsonObject jsonObject);

    @POST("api/patient/appointment/delete")
    Call<ResponseBody> deleteAppointment(@Body JsonObject jsonObject);

    @POST("api/patient/appointment/extra/respond")
    Call<ResponseBody> respondToDetailsPermission(@Body JsonObject jsonObject);

    @POST("api/patient/update")
    Call<ResponseBody> updatePatient(@Body JsonObject jsonObject);

    @POST("api/patient/password/change")
    Call<ResponseBody> changePassword(@Body JsonObject jsonObject);

    @POST("api/patient/password/reset/otp")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> resetPassword(@Body JsonObject jsonObject);

    @POST("api/patient/password/reset/validate")
    @Headers({"No-Authentication: true"})
    Call<ResponseBody> validateResetPassword(@Body JsonObject jsonObject);

    @POST("api/patient/account/delete/otp")
    Call<ResponseBody> deletePatient();

    @POST("api/patient/account/delete/validate")
    Call<ResponseBody> validateDeletePatient(@Body JsonObject jsonObject);
    /**
     * ============================== END OF PATIENT ==============================
     */
}