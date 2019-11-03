package doc.on.call.Repository;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.chaos.view.PinView;
import com.google.gson.JsonObject;

import doc.on.call.Fragments.DocOnCallFragment;
import doc.on.call.HomeActivity;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.RetroFit.Request.PatientApiRequest;
import doc.on.call.RetroFit.Request.RetrofitRequest;
import doc.on.call.SignInActivity;
import doc.on.call.Utilities.ObscuredSharedPreference;
import java.io.IOException;
import java.util.List;

import doc.on.call.VerifyActivity;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        switch(response.code()) {
                            case HTTP_OK:
                                toggleRegisterDetailsState(false);
                                showMessage(context.getString(R.string.message_register_success));
                                Intent signIn = new Intent(context, SignInActivity.class);
                                context.startActivity(signIn);
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
                                        showMessage(context.getString(R.string.message_register_invalid));
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
                        showMessage(context.getString(R.string.message_network_timeout));
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
                                showMessage(context.getString(R.string.message_verify_success));
                                Intent signIn = new Intent(context, SignInActivity.class);
                                context.startActivity(signIn);
                                break;
                            default:
                                toggleVerifyState(true);
                                showMessage(context.getString(R.string.message_verify_invalid));
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleVerifyState(true);
                        showMessage(context.getString(R.string.message_network_timeout));
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
                                        showMessage(context.getString(R.string.message_login_success));
                                    } else {
                                        toggleLoginState(true);
                                        showMessage(context.getString(R.string.message_network_timeout));
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
                                        showMessage(context.getString(R.string.message_login_unverified));
                                        // Not verified, direct to verify screen
                                        Intent verifyPatient = new Intent(context, VerifyActivity.class);
                                        context.startActivity(verifyPatient);
                                        toggleLoginState(false);
                                    } else {
                                        toggleLoginState(true);
                                        showMessage(context.getString(R.string.message_login_invalid));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                // Invalid login
                                toggleLoginState(true);
                                showMessage(context.getString(R.string.message_network_timeout));
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleLoginState(true);
                        showMessage(context.getString(R.string.message_network_timeout));
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
                                        showMessage(context.getString(R.string.message_validate_success));
                                        Intent home = new Intent(context, HomeActivity.class);
                                        context.startActivity(home);
                                        toggleValidateState(false);
                                    } else {
                                        toggleValidateState(true);
                                        showMessage(context.getString(R.string.message_network_timeout));
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
                                showMessage(context.getString(R.string.message_validate_invalid));
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        toggleValidateState(true);
                        showMessage(context.getString(R.string.message_network_timeout));
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
                                showMessage(context.getString(R.string.message_logout_success));
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                            default:
                                showMessage(context.getString(R.string.message_logout_no_session));
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout));
                    }
                });
    }

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
                                showMessage(context.getString(R.string.message_logout_no_session));
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Patient>> call, Throwable th) {
                        patientList.setValue(null);
                        showMessage(context.getString(R.string.message_network_timeout));
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
                                showMessage(context.getString(R.string.message_logout_no_session));
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable th) {
                        patient.setValue(null);
                        showMessage(context.getString(R.string.message_network_timeout));
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
        Log.d(TAG, jsonObject.toString());
        patientApiRequest.createAppointment(jsonObject)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_appointment_success));
                                DocOnCallFragment docOnCallFragment = new DocOnCallFragment(context);
                                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, docOnCallFragment).commit();
                                break;
                            default:
                                showMessage(context.getString(R.string.message_appointment_invalid));
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
                        showMessage(context.getString(R.string.message_network_timeout));
                    }
                });
    }
    /**
     * ============================== END OF PATIENT ==============================
     */
    /**
     * ============================== START OF UTILITIES ==============================
     */
    private void showMessage(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }
    /**
     * ============================== END OF UTILITIES ==============================
     */
}