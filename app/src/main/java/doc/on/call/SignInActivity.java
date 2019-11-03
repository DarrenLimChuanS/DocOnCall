package doc.on.call;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import doc.on.call.Repository.PatientRepository;

import static doc.on.call.Utilities.Commons.showMessage;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

    // Fetch NDK
    static {
        System.loadLibrary("native-lib");
    }
    public static native String getSiteAPIKey();

    // Declare variables
    EditText etUsername;
    EditText etPassword;
    ImageView imgPassword;
    TextView btnSignIn;
    TextView btnSignUp;
    LinearLayout llLoginInputs;

    ProgressBar pbLoading;

    PinView pinView;
    TextView btnSubmit;
    TextView btnBack;
    LinearLayout llOtpInputs;

    PatientRepository mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Fetch variables
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        imgPassword = (ImageView) findViewById(R.id.imgPassword);
        btnSignIn = (TextView) findViewById(R.id.btnSignIn);
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        llLoginInputs = (LinearLayout) findViewById(R.id.llLoginInputs);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        pinView = (PinView) findViewById(R.id.pinView);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        btnBack = (TextView) findViewById(R.id.btnBack);
        llOtpInputs = (LinearLayout) findViewById(R.id.llOtpInputs);

        mPatient = new PatientRepository(this);

        // Event Listeners
        imgPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPatient();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPatient();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOTP();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin();
            }
        });
    }

    /**
     * ================================= START OF FUNCTIONS =================================
     */
    public void loginPatient() {
        final String username = this.etUsername.getText().toString().trim();
        final String password = this.etPassword.getText().toString().trim();
        if (!isUsernameValid(username)) {
            etUsername.requestFocus();
            etUsername.setError(getString(R.string.error_username));
        } else if (!isPasswordValid(password)) {
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.error_password));
        } else {
            etUsername.setEnabled(false);
            etPassword.setEnabled(false);
            imgPassword.setEnabled(false);
            btnSignIn.setEnabled(false);
            btnSignUp.setEnabled(false);
            pbLoading.setVisibility(View.VISIBLE);

            // Recaptcha user-side token generation end
            SafetyNet.getClient(SignInActivity.this).verifyWithRecaptcha(getSiteAPIKey())
                    .addOnSuccessListener(SignInActivity.this,
                            new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                                @Override
                                public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                    // Indicates communication with reCAPTCHA service was
                                    // successful.
                                    String userResponseToken = response.getTokenResult();
                                    if (!userResponseToken.isEmpty()) {
                                        Log.e("token", userResponseToken);
                                        // Validate the user response token by sending to backend-server, send token to server as well (implement when server verification is done)
                                        mPatient.loginPatient(username, password, userResponseToken);
                                    }
                                }
                            })
                    .addOnFailureListener(SignInActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof ApiException) {
                                // An error occurred when communicating with the
                                // reCAPTCHA service. Refer to the status code to
                                // handle the error appropriately.
                                ApiException apiException = (ApiException) e;
                                int statusCode = apiException.getStatusCode();
                                Log.e("token", "Error: " + CommonStatusCodes
                                        .getStatusCodeString(statusCode));
                                // show error to tell users of captcha verification failed
                                showMessage(getString(R.string.message_login_captcha_failed), SignInActivity.this);
                            } else {
                                // A different, unknown type of error occurred.
                                Log.e("token", "Error: " + e.getMessage());
                                // show error to tell users of captcha verification failed
                                showMessage(getString(R.string.message_login_captcha_failed), SignInActivity.this);
                            }
                            etUsername.setEnabled(true);
                            etPassword.setEnabled(true);
                            imgPassword.setEnabled(true);
                            btnSignIn.setEnabled(true);
                            btnSignUp.setEnabled(true);
                            pbLoading.setVisibility(View.INVISIBLE);
                        }
                    }).addOnCanceledListener(SignInActivity.this, new OnCanceledListener() {
                //NOT WORKING NEED CHECK AGAIN
                @Override
                public void onCanceled() {
                    showMessage("Captcha Verification Canceled!", SignInActivity.this);
                    etUsername.setEnabled(true);
                    etPassword.setEnabled(true);
                    imgPassword.setEnabled(true);
                    btnSignIn.setEnabled(true);
                    btnSignUp.setEnabled(true);
                    pbLoading.setVisibility(View.INVISIBLE);
                }
            });
            // Recaptcha user-side token generation end
        }
    }

    public void registerPatient() {
        Intent signUp = new Intent(this, SignUpActivity.class);
        startActivity(signUp);
    }

    public void submitOTP() {
        String otp = pinView.getText().toString().trim();
        if (isOtpValid(otp)) {
            pinView.setEnabled(false);
            btnSubmit.setEnabled(false);
            btnBack.setEnabled(false);
            pbLoading.setVisibility(View.VISIBLE);
            mPatient.validatePatient(otp);
        } else {
            pinView.requestFocus();
            pinView.setError(getString(R.string.error_otp));
        }
    }

    public void backToLogin() {
        etUsername.setEnabled(true);
        etPassword.setEnabled(true);
        imgPassword.setEnabled(true);
        btnSignIn.setEnabled(true);
        btnSignUp.setEnabled(true);
        etUsername.getText().clear();
        etPassword.getText().clear();
        llOtpInputs.setVisibility(View.GONE);
        llLoginInputs.setVisibility(View.VISIBLE);
    }
    /**
     * ================================= END OF FUNCTIONS =================================
     */

    /**
     * ================================= START OF VALIDATIONS =================================
     */
    boolean isUsernameValid(String username) {
        return !username.isEmpty() ? true : false;
    }

    boolean isPasswordValid(String password) {
        return !password.isEmpty() ? true : false;
    }

    boolean isOtpValid(String otp) {
        return !otp.isEmpty() ? true : false;
    }
    /**
     * ================================= END OF VALIDATIONS =================================
     */
}
