package doc.on.call.Repository;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chaos.view.PinView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import doc.on.call.Fragments.DocOnCallFragment;
import doc.on.call.Fragments.PatientFragment;
import doc.on.call.HomeActivity;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.RetroFit.Request.PatientApiRequest;
import doc.on.call.RetroFit.Request.RetrofitRequest;
import doc.on.call.SignInActivity;
import doc.on.call.Utilities.ObscuredSharedPreference;
import doc.on.call.VerifyActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static doc.on.call.Utilities.Commons.showMessage;
import static doc.on.call.Utilities.Constants.HTTP_BAD;
import static doc.on.call.Utilities.Constants.HTTP_OK;
import static doc.on.call.Utilities.Constants.HTTP_UNAUTHORIZED;
import static doc.on.call.Utilities.Constants.PREF_NONCE;
import static doc.on.call.Utilities.Constants.PREF_TOKEN;
import static doc.on.call.Utilities.Constants.SPLASH_TIME_OUT;

public class PatientRepository {
    private static final String TAG = PatientRepository.class.getSimpleName();
    private Context context;
    private ObscuredSharedPreference mSharedPreference;
    private PatientApiRequest patientApiRequest;

    public PatientRepository(Context context) {
        this.context = context;
        this.patientApiRequest = (PatientApiRequest) RetrofitRequest.getRetrofitInstance(context).create(PatientApiRequest.class);
        this.mSharedPreference = ObscuredSharedPreference.getPref(context);
    }

    /**
     * ============================== START OF LOGIN ==============================
     */
    /**
     * PatientApiRequest for Register Patient
     */
    public void registerPatient(String email, String username, String password, String fullname, String nric, int age, int phone, String address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", address);
        jsonObject.addProperty("age", Integer.valueOf(age));
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("name", fullname);
        jsonObject.addProperty("nric", nric);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("phone", Integer.valueOf(phone));
        jsonObject.addProperty("username", username);
        patientApiRequest.registerPatient(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println(response.body());
                        switch(response.code()) {
                            case HTTP_OK:
                                //Added from here
                                try {
                                    String resendToken = new JSONObject(response.body().string()).getString("token");
                                    if (!resendToken.isEmpty()) {
                                        System.out.println("resendToken: "+resendToken);
                                        mSharedPreference.writeRegisterationResendToken(resendToken);
                                        //TODO see if wanna put the code (toggle...to context.start) here
                                        //Added this because registration now returns a token
                                        //That token is used if u wanna resend verification token
                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                                //to here
                                toggleRegisterDetailsState(false);
                                showMessage(context.getString(R.string.message_register_success), context);
                                //Intent signIn = new Intent(context, SignInActivity.class);
                                //context.startActivity(signIn);
                                break;
                            default:
                                try {
                                    if (response.errorBody().string().equals(context.getResources().getString(R.string.message_register_input_conflict))) {
                                        toggleRegisterDetailsState(false);
                                        toggleRegisterState(true);
                                        ((Activity) context).findViewById(R.id.llAccountInputs).setVisibility(View.VISIBLE);
                                        ((Activity) context).findViewById(R.id.llInformationInputs).setVisibility(View.GONE);
                                        EditText etUsername = (EditText) ((Activity) context).findViewById(R.id.etUsername);
                                        EditText etEmail = (EditText) ((Activity) context).findViewById(R.id.etEmail);
                                        etUsername.requestFocus();
                                        etUsername.setError(context.getResources().getString(R.string.message_register_input_conflict));
                                        etEmail.requestFocus();
                                        etEmail.setError(context.getResources().getString(R.string.message_register_input_conflict));
                                    } else {
                                        toggleRegisterDetailsState(true);
                                        showMessage(context.getString(R.string.message_register_invalid), context);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleRegisterDetailsState(true);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    public void resendRegistrationToken(String email){
        JsonObject jsonObject = new JsonObject();
        System.out.println(this.mSharedPreference.readRegisterationResendToken());
        jsonObject.addProperty("token", this.mSharedPreference.readRegisterationResendToken());
        jsonObject.addProperty("email", email);
        patientApiRequest.resendRegistrationToken(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                try {
                                    String resendToken = new JSONObject(response.body().string()).getString("token");
                                    if (!resendToken.isEmpty()) {
                                        mSharedPreference.writeRegisterationResendToken(resendToken);
                                    }
                                    showMessage(context.getString(R.string.message_register_resend), context);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case HTTP_BAD:
                                System.out.println(response.body());
                                break;
                            default:
                                showMessage(context.getString(R.string.message_network_timeout), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * Function to handle Register Details State
     */
    private void toggleRegisterDetailsState(boolean status) {
        if (status) {
            ((Activity) this.context).findViewById(R.id.etFullName).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etNRIC).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etAge).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etPhone).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etAddress).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSignUp2).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSignUp2Back).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        } else {
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        }
    }

    /**
     * Function to handle Register State
     */
    private void toggleRegisterState(boolean status) {
        if (status) {
            ((Activity) this.context).findViewById(R.id.etEmail).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etUsername).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etPassword).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.imgPassword).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSignUp).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSignUpBack).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        } else {
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        }
    }

    /**
     * PatientApiRequest for Verify Patient
     */
    public void verifyPatient(String username, String password, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("username", username);
        this.patientApiRequest.verifyPatient(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch (response.code()) {
                            case HTTP_OK:
                                toggleVerifyState(false);
                                showMessage(context.getString(R.string.message_verify_success), context);
                                Intent signIn = new Intent(context, SignInActivity.class);
                                context.startActivity(signIn);
                                break;
                            default:
                                toggleVerifyState(true);
                                showMessage(context.getString(R.string.message_verify_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleVerifyState(true);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
        });
    }

    /**
     * Function to handle Verify State
     */
    private void toggleVerifyState(boolean status) {
        if (status) {
            ((Activity) this.context).findViewById(R.id.etUsername).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etPassword).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.imgPassword).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.etToken).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnVerify).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnBack).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        } else {
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        }
    }

    /**
     * PatientApiRequest for Login Patient
     */
    public void loginPatient(String username, String password, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginUser", "patient");
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("captchaToken", token);
        patientApiRequest.loginPatient(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                try {
                                    String nonce = new JSONObject(response.body().string()).getString("nonce");
                                    if (!nonce.isEmpty()) {
                                        mSharedPreference.writeNonce(nonce);
                                        PinView pinView = (PinView) ((Activity)context).findViewById(R.id.pinView);
                                        pinView.getText().clear();
                                        pinView.setLineColor(context.getResources().getColor(R.color.colorAccent));
                                        ((Activity)context).findViewById(R.id.llLoginInputs).setVisibility(View.GONE);
                                        toggleLoginState(false);
                                        ((Activity)context).findViewById(R.id.llOtpInputs).setVisibility(View.VISIBLE);
                                        showMessage(context.getString(R.string.message_login_success), context);
                                    } else {
                                        toggleLoginState(true);
                                        showMessage(context.getString(R.string.message_network_timeout), context);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case HTTP_UNAUTHORIZED:
                                // Verified or invalid login
                                try {
                                    if (response.errorBody().string().equals(context.getResources().getString(R.string.message_login_unverified))) {
                                        showMessage(context.getString(R.string.message_login_unverified), context);
                                        // Not verified, direct to verify screen
                                        Intent verifyPatient = new Intent(context, VerifyActivity.class);
                                        context.startActivity(verifyPatient);
                                        toggleLoginState(false);
                                    } else {
                                        toggleLoginState(true);
                                        showMessage(context.getString(R.string.message_login_invalid), context);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                // Invalid login
                                toggleLoginState(true);
                                showMessage(context.getString(R.string.message_network_timeout), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleLoginState(true);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * Function to handle Login State
     */
    private void toggleLoginState(boolean status) {
        if (status) {
            ((Activity) context).findViewById(R.id.etUsername).setEnabled(true);
            ((Activity) context).findViewById(R.id.etPassword).setEnabled(true);
            ((Activity) context).findViewById(R.id.imgPassword).setEnabled(true);
            ((Activity) context).findViewById(R.id.btnSignIn).setEnabled(true);
            ((Activity) context).findViewById(R.id.btnSignUp).setEnabled(true);
            ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        } else {
            ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        }
    }

    /**
     * PatientApiRequest for Validate Patient
     */
    public void validatePatient(String otp) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nonce", this.mSharedPreference.readNonce());
        jsonObject.addProperty("otp", otp);
        patientApiRequest.validatePatient(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        PinView pinView = (PinView) ((Activity)context).findViewById(R.id.pinView);
                        switch(response.code()) {
                            case HTTP_OK:
                                try {
                                    String token = new JSONObject(response.body().string()).getString("token");
                                    if (!token.isEmpty()) {
                                        mSharedPreference.writeJWTToken(token);
                                        mSharedPreference.removeSharedPreference(PREF_NONCE);
                                        pinView.setLineColor(context.getResources().getColor(R.color.green));
                                        showMessage(context.getString(R.string.message_validate_success), context);
                                        Intent home = new Intent(context, HomeActivity.class);
                                        context.startActivity(home);
                                        toggleValidateState(false);
                                    } else {
                                        toggleValidateState(true);
                                        showMessage(context.getString(R.string.message_network_timeout), context);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                pinView.setLineColor(context.getResources().getColor(R.color.red));
                                toggleValidateState(true);
                                showMessage(context.getString(R.string.message_validate_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleValidateState(true);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * Function to handle Validate State
     */
    private void toggleValidateState(boolean status) {
        if (status) {
            ((Activity) this.context).findViewById(R.id.pinView).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSubmit).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnBack).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        } else {
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        }
    }

    /**
     * PatientApiRequest for Patient Reset Password
     */
    public void resetPasswordSendOTP(String username){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        Log.d(TAG, jsonObject.toString());
        patientApiRequest.resetPassword(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                try {
                                    String nonce = new JSONObject(response.body().string()).getString("nonce");
                                    if (!nonce.isEmpty()) {
                                        mSharedPreference.writeNonce(nonce);
                                        PinView pinView = (PinView) ((Activity)context).findViewById(R.id.pinView);
                                        pinView.getText().clear();
                                        pinView.setLineColor(context.getResources().getColor(R.color.colorAccent));
                                        ((Activity)context).findViewById(R.id.llResetInputs).setVisibility(View.GONE);
                                        ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
                                        ((Activity)context).findViewById(R.id.llOtpInputs).setVisibility(View.VISIBLE);
                                        showMessage(context.getString(R.string.message_password_reset_otp_sent), context);
                                    } else {
                                        ((Activity) context).findViewById(R.id.etUsername).setEnabled(true);
                                        ((Activity) context).findViewById(R.id.btnResetPassword).setEnabled(true);
                                        ((Activity) context).findViewById(R.id.btnBack).setEnabled(true);
                                        ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
                                        showMessage(context.getString(R.string.message_network_timeout), context);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                ((Activity) context).findViewById(R.id.etUsername).setEnabled(true);
                                ((Activity) context).findViewById(R.id.btnResetPassword).setEnabled(true);
                                ((Activity) context).findViewById(R.id.btnBack).setEnabled(true);
                                ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
                                showMessage(context.getString(R.string.message_password_reset_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        ((Activity) context).findViewById(R.id.etUsername).setEnabled(true);
                        ((Activity) context).findViewById(R.id.btnResetPassword).setEnabled(true);
                        ((Activity) context).findViewById(R.id.btnBack).setEnabled(true);
                        ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * PatientApiRequest for Patient Validate Reset Password
     */
    public void validateResetPassword(String otp){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nonce", this.mSharedPreference.readNonce());
        jsonObject.addProperty("otp", otp);
        patientApiRequest.validateResetPassword(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        PinView pinView = (PinView) ((Activity)context).findViewById(R.id.pinView);
                        switch(response.code()) {
                            case HTTP_OK:
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                pinView.setLineColor(context.getResources().getColor(R.color.green));
                                showMessage(context.getString(R.string.message_password_reset_success), context);
                                Intent signIn = new Intent(context, SignInActivity.class);
                                context.startActivity(signIn);
                                toggleValidateResetState(false);
                                break;
                            default:
                                pinView.setLineColor(context.getResources().getColor(R.color.red));
                                toggleValidateResetState(true);
                                showMessage(context.getString(R.string.message_validate_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleValidateResetState(true);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * Function to handle Validate Reset State
     */
    private void toggleValidateResetState(boolean status) {
        if (status) {
            ((Activity) this.context).findViewById(R.id.pinView).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSubmit).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnBack2).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        } else {
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
        }
    }

    /**
     * PatientApiRequest for Logout Patient
     */
    public void logoutPatient() {
        patientApiRequest.logoutPatient()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Intent signIn = new Intent(context, SignInActivity.class);
                        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_logout_success), context);
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                            default:
                                showMessage(context.getString(R.string.message_logout_no_session), context);
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * PatientApiRequest for Get Patient to check if user is logged in
     */
    public void checkLoggedIn() {
        patientApiRequest.getPatient()
                .enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        Log.d(TAG, "Checking logged in");
                        switch(response.code()) {
                            case HTTP_OK:
                                if (response.body() != null) {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            Intent home = new Intent(context, HomeActivity.class);
                                            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(home);
                                        }
                                    }, (long) SPLASH_TIME_OUT);
                                }
                                break;
                            default:
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        Intent signIn = new Intent(context, SignInActivity.class);
                                        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(signIn);
                                    }
                                }, (long) SPLASH_TIME_OUT);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable th) {
                    }
                });
    }
    /**
     * ============================== END OF LOGIN ==============================
     */

    /**
     * ============================== START OF PATIENT ==============================
     */
    /**
     * PatientApiRequest for Get All Patients
     */
    public LiveData<List<Patient>> getAllPatients() {
        final MutableLiveData<List<Patient>> patientList = new MutableLiveData<>();
        patientApiRequest.getAllPatients()
                .enqueue(new Callback<List<Patient>>() {
                    @Override
                    public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                        Intent signIn = new Intent(context, SignInActivity.class);
                        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d(TAG, "onResponse response:: " + response);
                        switch(response.code()) {
                            case HTTP_OK:
                                if (response.body() != null) {
                                    patientList.setValue(response.body());
                                    Log.d(TAG, "Patient list is: " + patientList.getValue().toString());
                                }
                                break;
                            default:
                                showMessage(context.getString(R.string.message_logout_no_session), context);
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Patient>> call, Throwable th) {
                        patientList.setValue(null);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
        return patientList;
    }

    /**
     * PatientApiRequest for Get Patient
     */
    public LiveData<Patient> getPatient() {
        final MutableLiveData<Patient> patient = new MutableLiveData<>();
        patientApiRequest.getPatient()
                .enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        Intent signIn = new Intent(context, SignInActivity.class);
                        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        Log.d(TAG, "onResponse response:: " + response);
                        switch(response.code()) {
                            case HTTP_OK:
                                if (response.body() != null) {
                                    patient.setValue(response.body());
                                    Log.d(TAG, "Patient is: " + patient.getValue().toString());
                                }
                                break;
                            default:
                                showMessage(context.getString(R.string.message_logout_no_session), context);
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable th) {
                        patient.setValue(null);
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
        });
        return patient;
    }

    /**
     * PatientApiRequest for Create Appointment
     */
    public void createAppointment(String appointmentDateTime, String datetimeCreated, String issue, String patientDetail) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appointmentDateTime", appointmentDateTime);
        jsonObject.addProperty("datetimeCreated", datetimeCreated);
        jsonObject.addProperty("issue", issue);
        jsonObject.addProperty("patientDetail", patientDetail);
        patientApiRequest.createAppointment(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_appointment_success), context);
                                DocOnCallFragment docOnCallFragment = new DocOnCallFragment(context);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, docOnCallFragment).commit();
                                break;
                            default:
                                showMessage(context.getString(R.string.message_appointment_invalid), context);
                                ((FragmentActivity) context).findViewById(R.id.etIssue).setEnabled(true);
                                ((FragmentActivity) context).findViewById(R.id.btnDate).setEnabled(true);
                                ((FragmentActivity) context).findViewById(R.id.btnTime).setEnabled(true);
                                ((FragmentActivity) context).findViewById(R.id.btnCreate).setEnabled(true);
                                ((FragmentActivity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    /**
     * PatientApiRequest for Toggling Details Permission
     */
    public void respondToDetailsPermission(String appointmentId, final Boolean acceptPermission, final String doctorName){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appointmentId", appointmentId);
        jsonObject.addProperty("acceptPermission", acceptPermission);
        patientApiRequest.respondToDetailsPermission(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                if (acceptPermission) {
                                    String successOnMessage = context.getString(R.string.message_respond_doctor_success_on, doctorName);
                                    showMessage(successOnMessage, context);
                                } else {
                                    String successOffMessage = context.getString(R.string.message_respond_doctor_success_off, doctorName);
                                    showMessage(successOffMessage, context);
                                }
                                break;
                            default:
                                showMessage(context.getString(R.string.message_respond_doctor_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    // work
    public void updatePatient(String address, String email, String phone){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", address.trim());
        jsonObject.addProperty("email", email.trim());
        jsonObject.addProperty("phone", phone.trim());
        Log.d(TAG, jsonObject.toString());
        patientApiRequest.updatePatient(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_account_updated_success), context);
                                break;
                            default:
                                showMessage(context.getString(R.string.message_account_update_invalid), context);
                                break;
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }


    // work
    public void changePassword(String username, String oldPassword, String newPassword){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("role", "patient");
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("oldPassword", oldPassword);
        jsonObject.addProperty("newPassword", newPassword);
        Log.d(TAG, jsonObject.toString());
        patientApiRequest.changePassword(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_change_password_success), context);
                                break;
                            default:
                                showMessage(context.getString(R.string.message_change_password_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    // work
    public void deletePatient(){
        Log.d(TAG, "deletePatient");
        patientApiRequest.deletePatient()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                try {
                                    String nonce = new JSONObject(response.body().string()).getString("nonce");
                                    if (!nonce.isEmpty()) {
                                        mSharedPreference.writeNonce(nonce);
                                        showMessage(context.getString(R.string.message_delete_account_otp_sent), context);
                                    } else {
                                        toggleLoginState(true);
                                        showMessage(context.getString(R.string.message_network_timeout), context);
                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                showMessage(context.getString(R.string.message_delete_account_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    // work
    public void validateDeleteAccount(String otp){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nonce", this.mSharedPreference.readNonce());
        jsonObject.addProperty("otp", otp);
        Log.d(TAG, jsonObject.toString());
        patientApiRequest.validateDeleteAccount(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                showMessage(context.getString(R.string.message_delete_account_success), context);
                                logoutPatient();
                                //TODO log user out and clear all the share pref
                                break;
                            default:
                                showMessage(context.getString(R.string.message_delete_account_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }

    // work
    public void deleteAppointment(String appointmentId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appointmentId", appointmentId);
        Log.d(TAG, jsonObject.toString());
        patientApiRequest.deleteAppointment(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_delete_appointment_success), context);
                                PatientFragment patientFragment = new PatientFragment(context);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, patientFragment).commit();
                                break;
                            default:
                                showMessage(context.getString(R.string.message_delete_appointment_invalid), context);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout), context);
                    }
                });
    }


    /**
     * ============================== END OF PATIENT ==============================
     */
}