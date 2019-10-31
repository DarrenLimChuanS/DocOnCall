package doc.on.call;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaos.view.PinView;

import doc.on.call.Repository.PatientRepository;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();

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
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        imgPassword = (ImageView)findViewById(R.id.imgPassword);
        btnSignIn = (TextView)findViewById(R.id.btnSignIn);
        btnSignUp = (TextView)findViewById(R.id.btnSignUp);
        llLoginInputs = (LinearLayout)findViewById(R.id.llLoginInputs);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        pinView = (PinView)findViewById(R.id.pinView);
        btnSubmit = (TextView)findViewById(R.id.btnSubmit);
        btnBack = (TextView)findViewById(R.id.btnBack);
        llOtpInputs = (LinearLayout)findViewById(R.id.llOtpInputs);

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
        String username = this.etUsername.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
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

            mPatient.loginPatient(username, password);
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
