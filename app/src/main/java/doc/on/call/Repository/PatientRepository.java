package doc.on.call.Repository;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.chaos.view.PinView;
import com.google.gson.JsonObject;

import doc.on.call.HomeActivity;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.RetroFit.Request.PatientApiRequest;
import doc.on.call.RetroFit.Request.RetrofitRequest;
import doc.on.call.SignInActivity;
import doc.on.call.Utilities.Constants;
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
    public void loginPatient(String username, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginUser", "patient");
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
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
                                        ((Activity)context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
                                        ((Activity)context).findViewById(R.id.llOtpInputs).setVisibility(View.VISIBLE);
                                        showMessage(context.getString(R.string.message_login_success));
                                    } else {
                                        enableLoginState();
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
                                        enableLoginState();
                                    } else {
                                        enableLoginState();
                                        showMessage(context.getString(R.string.message_login_invalid));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                // Invalid login
                                enableLoginState();
                                showMessage(context.getString(R.string.message_network_timeout));
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        enableLoginState();
                        showMessage(context.getString(R.string.message_network_timeout));
                    }
                });
    }

    private void enableLoginState() {
            ((Activity) context).findViewById(R.id.etUsername).setEnabled(true);
            ((Activity) context).findViewById(R.id.etPassword).setEnabled(true);
            ((Activity) context).findViewById(R.id.imgPassword).setEnabled(true);
            ((Activity) context).findViewById(R.id.btnSignIn).setEnabled(true);
            ((Activity) context).findViewById(R.id.btnSignUp).setEnabled(true);
            ((Activity) context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
    }

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
                                        enableValidateState();
                                    } else {
                                        enableValidateState();
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
                                enableValidateState();
                                showMessage(context.getString(R.string.message_validate_invalid));
                                break;
                        }
                    }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable th) {
                    enableValidateState();
                    showMessage(context.getString(R.string.message_network_timeout));
                }
                });
    }

    private void enableValidateState() {
            ((Activity) this.context).findViewById(R.id.pinView).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnSubmit).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.btnBack).setEnabled(true);
            ((Activity) this.context).findViewById(R.id.pbLoading).setVisibility(View.GONE);
    }
//
//    private void toggleRegisterDetailsState(boolean z) {
//        if (z) {
//            ((Activity) this.context).findViewById(R.id.etName).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etNRIC).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etAge).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etPhone).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etAddress).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.btnSignUp2).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.btnSignUp2Back).setEnabled(true);
//            ((Activity) this.context).findViewById(2131296540).setVisibility(8);
//            return;
//        }
//        ((Activity) this.context).findViewById(2131296540).setVisibility(8);
//    }
//
//    private void toggleRegisterState(boolean z) {
//        if (z) {
//            ((Activity) this.context).findViewById(R.id.etEmail).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etUsername).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etPassword).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.imgPassword).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.btnSignUp).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.btnSignUpBack).setEnabled(true);
//            ((Activity) this.context).findViewById(2131296540).setVisibility(8);
//            return;
//        }
//        ((Activity) this.context).findViewById(2131296540).setVisibility(8);
//    }
//

//
//    private void toggleVerifyState(boolean z) {
//        if (z) {
//            ((Activity) this.context).findViewById(R.id.etUsername).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etPassword).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.imgPassword).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.etVerify).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.btnVerify).setEnabled(true);
//            ((Activity) this.context).findViewById(R.id.btnBack).setEnabled(true);
//            ((Activity) this.context).findViewById(2131296540).setVisibility(8);
//            return;
//        }
//        ((Activity) this.context).findViewById(2131296540).setVisibility(8);
//    }

    public void logoutPatient() {
        patientApiRequest.logoutPatient()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch(response.code()) {
                            case HTTP_OK:
                                showMessage(context.getString(R.string.message_logout_success));
                                Intent signIn = new Intent(context, SignInActivity.class);
                                context.startActivity(signIn);
                                mSharedPreference.removeSharedPreference(PREF_NONCE);
                                mSharedPreference.removeSharedPreference(PREF_TOKEN);
                                break;
                            default:
                                showMessage(context.getString(R.string.message_network_timeout));
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable th) {
                        showMessage(context.getString(R.string.message_network_timeout));
                    }
                });
    }

    public void registerPatient(String str, String str2, String str3, String str4, String str5, int i, int i2, String str6) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("address", str6);
//        jsonObject.addProperty("age", Integer.valueOf(i));
//        jsonObject.addProperty("email", str);
//        jsonObject.addProperty("name", str4);
//        jsonObject.addProperty("nric", str5);
//        jsonObject.addProperty("password", str3);
//        jsonObject.addProperty("phone", Integer.valueOf(i2));
//        jsonObject.addProperty("username", str2);
//        this.patientApiRequest.registerPatient(jsonObject).enqueue(new Callback<ResponseBody>() {
//            public void onFailure(Call<ResponseBody> call, Throwable th) {
//                PatientRepository.this.toggleRegisterDetailsState(true);
//                PatientRepository patientRepository = PatientRepository.this;
//                patientRepository.showMessage(patientRepository.context.getString(R.string.message_network_timeout));
//            }
//
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() != Constants.HTTP_OK) {
//                    try {
//                        if (response.errorBody().string().equals(PatientRepository.this.context.getResources().getString(R.string.message_register_input_conflict))) {
//                            PatientRepository.this.toggleRegisterDetailsState(false);
//                            PatientRepository.this.toggleRegisterState(true);
//                            ((Activity) PatientRepository.this.context).findViewById(R.id.accountInputs).setVisibility(0);
//                            ((Activity) PatientRepository.this.context).findViewById(R.id.informationInputs).setVisibility(8);
//                            EditText editText = (EditText) ((Activity) PatientRepository.this.context).findViewById(R.id.etUsername);
//                            EditText editText2 = (EditText) ((Activity) PatientRepository.this.context).findViewById(R.id.etEmail);
//                            editText.requestFocus();
//                            editText.setError(PatientRepository.this.context.getResources().getString(R.string.message_register_input_conflict));
//                            editText2.requestFocus();
//                            editText2.setError(PatientRepository.this.context.getResources().getString(R.string.message_register_input_conflict));
//                            return;
//                        }
//                        PatientRepository.this.toggleRegisterDetailsState(true);
//                        PatientRepository.this.showMessage(PatientRepository.this.context.getString(R.string.message_register_invalid));
//                        return;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                }
//                PatientRepository.this.toggleRegisterDetailsState(false);
//                PatientRepository.this.context.startActivity(new Intent(PatientRepository.this.context, SignInActivity.class));
//                PatientRepository patientRepository = PatientRepository.this;
//                patientRepository.showMessage(patientRepository.context.getString(R.string.message_register_success));
//            }
//        });
    }



    public void verifyPatient(String str, String str2, String str3) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("password", str2);
//        jsonObject.addProperty("token", str3);
//        jsonObject.addProperty("username", str);
//        this.patientApiRequest.verifyPatient(jsonObject).enqueue(new Callback<ResponseBody>() {
//            public void onFailure(Call<ResponseBody> call, Throwable th) {
//                PatientRepository.this.toggleVerifyState(true);
//                PatientRepository patientRepository = PatientRepository.this;
//                patientRepository.showMessage(patientRepository.context.getString(R.string.message_network_timeout));
//            }
//
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                PatientRepository patientRepository;
//                if (response.code() != Constants.HTTP_OK) {
//                    PatientRepository.this.toggleVerifyState(true);
//                    patientRepository = PatientRepository.this;
//                    patientRepository.showMessage(patientRepository.context.getString(R.string.message_verify_invalid));
//                    return;
//                }
//                PatientRepository.this.toggleVerifyState(false);
//                PatientRepository.this.context.startActivity(new Intent(PatientRepository.this.context, SignInActivity.class));
//                patientRepository = PatientRepository.this;
//                patientRepository.showMessage(patientRepository.context.getString(R.string.message_verify_success));
//            }
//        });
    }

    public LiveData<List<Patient>> getAllPatients() {
        final MutableLiveData mutableLiveData = new MutableLiveData();
//        this.patientApiRequest.getAllPatients().enqueue(new Callback<List<Patient>>() {
//            public void onFailure(Call<List<Patient>> call, Throwable th) {
//                mutableLiveData.setValue(null);
//            }
//
//            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
//                String access$800 = PatientRepository.TAG;
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("onResponse response: ");
//                stringBuilder.append(response);
//                Log.d(access$800, stringBuilder.toString());
//                Log.d(PatientRepository.TAG, "=================================");
//                access$800 = PatientRepository.TAG;
//                stringBuilder = new StringBuilder();
//                stringBuilder.append("Message ");
//                stringBuilder.append(response.message());
//                Log.d(access$800, stringBuilder.toString());
//                Log.d(PatientRepository.TAG, "=================================");
//                access$800 = PatientRepository.TAG;
//                stringBuilder = new StringBuilder();
//                stringBuilder.append("Body: ");
//                stringBuilder.append(response.body());
//                Log.d(access$800, stringBuilder.toString());
//                Log.d(PatientRepository.TAG, "=================================");
//                String access$8002 = PatientRepository.TAG;
//                StringBuilder stringBuilder2 = new StringBuilder();
//                stringBuilder2.append("BodyFirstPatient: ");
//                stringBuilder2.append(((Patient) ((List) response.body()).get(5)).toString());
//                Log.d(access$8002, stringBuilder2.toString());
//                Log.d(PatientRepository.TAG, "=================================");
//                access$800 = PatientRepository.TAG;
//                stringBuilder = new StringBuilder();
//                stringBuilder.append("Code: ");
//                stringBuilder.append(response.code());
//                Log.d(access$800, stringBuilder.toString());
//                if (response.body() != null) {
//                    mutableLiveData.setValue(response.body());
//                }
//            }
//        });
        return mutableLiveData;
    }

    public LiveData<Patient> getPatient(String str) {
        final MutableLiveData mutableLiveData = new MutableLiveData();
//        this.patientApiRequest.getPatient(str).enqueue(new Callback<Patient>() {
//            public void onFailure(Call<Patient> call, Throwable th) {
//                Log.d(PatientRepository.TAG, "Failed");
//                mutableLiveData.setValue(null);
//            }
//
//            public void onResponse(Call<Patient> call, Response<Patient> response) {
//                String access$800 = PatientRepository.TAG;
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("onResponse response:: ");
//                stringBuilder.append(response);
//                Log.d(access$800, stringBuilder.toString());
//                if (response.body() != null) {
//                    mutableLiveData.setValue(response.body());
//                    String access$8002 = PatientRepository.TAG;
//                    StringBuilder stringBuilder2 = new StringBuilder();
//                    stringBuilder2.append("Patient: ");
//                    stringBuilder2.append(((Patient) response.body()));
//                    Log.d(access$8002, stringBuilder2.toString());
//                }
//            }
//        });
        return mutableLiveData;
    }

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