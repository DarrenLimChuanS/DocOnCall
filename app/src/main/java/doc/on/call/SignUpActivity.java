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


import doc.on.call.Repository.PatientRepository;

import static doc.on.call.Utilities.Commons.isAddressValid;
import static doc.on.call.Utilities.Commons.isAgeValid;
import static doc.on.call.Utilities.Commons.isEmailValid;
import static doc.on.call.Utilities.Commons.isFullNameValid;
import static doc.on.call.Utilities.Commons.isNRICValid;
import static doc.on.call.Utilities.Commons.isPasswordValid;
import static doc.on.call.Utilities.Commons.isPhoneValid;
import static doc.on.call.Utilities.Commons.isUsernameValid;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();

    // Declare variables
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    ImageView imgPassword;
    TextView btnSignUp;
    TextView btnSignUpBack;
    LinearLayout llAccountInputs;

    ProgressBar pbLoading;

    EditText etFullName;
    EditText etNRIC;
    EditText etAge;
    EditText etPhone;
    EditText etAddress;
    TextView btnSignUp2;
    TextView btnSignUp2Back;
    LinearLayout llInformationInputs;

    PatientRepository mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Fetch variables
        etEmail = (EditText)findViewById(R.id.etEmail);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        imgPassword = (ImageView)findViewById(R.id.imgPassword);
        btnSignUp = (TextView)findViewById(R.id.btnSignUp);
        btnSignUpBack = (TextView)findViewById(R.id.btnSignUpBack);
        llAccountInputs = (LinearLayout)findViewById(R.id.llAccountInputs);

        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        etFullName = (EditText)findViewById(R.id.etFullName);
        etNRIC = (EditText)findViewById(R.id.etNRIC);
        etAge = (EditText)findViewById(R.id.etAge);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etAddress = (EditText)findViewById(R.id.etAddress);
        btnSignUp2 = (TextView)findViewById(R.id.btnSignUp2);
        btnSignUp2Back = (TextView)findViewById(R.id.btnSignUp2Back);
        llInformationInputs = (LinearLayout)findViewById(R.id.llInformationInputs);

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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueRegister();
            }
        });

        btnSignUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin();
            }
        });

        btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPatient();
            }
        });

        btnSignUp2Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToAccountInput();
            }
        });
    }

    /**
     * ================================= START OF FUNCTIONS =================================
     */
    public void continueRegister() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(!isEmailValid(email)) {
            etEmail.requestFocus();
            etEmail.setError(getString(R.string.error_email));
        } else if (!isUsernameValid(username)) {
            etUsername.requestFocus();
            etUsername.setError(getString(R.string.error_username));
        } else if (!isPasswordValid(password)) {
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.error_password));
        } else {
            etEmail.setEnabled(false);
            etUsername.setEnabled(false);
            etPassword.setEnabled(false);
            imgPassword.setEnabled(false);
            btnSignUp.setEnabled(false);
            btnSignUpBack.setEnabled(false);
            llAccountInputs.setVisibility(View.GONE);
            llInformationInputs.setVisibility(View.VISIBLE);
            etFullName.setEnabled(true);
            etNRIC.setEnabled(true);
            etAge.setEnabled(true);
            etPhone.setEnabled(true);
            etAddress.setEnabled(true);
            btnSignUp2.setEnabled(true);
            btnSignUp2Back.setEnabled(true);
        }
    }

    public void backToLogin() {
        Intent signIn = new Intent(this, SignInActivity.class);
        startActivity(signIn);
    }

    public void registerPatient() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullname = etFullName.getText().toString().trim();
        String nric = etNRIC.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        if(!isFullNameValid(fullname)) {
            etFullName.requestFocus();
            etFullName.setError(getString(R.string.error_fullname));
        } else if (!isNRICValid(nric)) {
            etNRIC.requestFocus();
            etNRIC.setError(getString(R.string.error_nric));
        } else if (!isAgeValid(age)) {
            etAge.requestFocus();
            etAge.setError(getString(R.string.error_age));
        } else if (!isPhoneValid(phone)) {
            etPhone.requestFocus();
            etPhone.setError(getString(R.string.error_phone));
        } else if (!isAddressValid(address)) {
            etAddress.requestFocus();
            etAddress.setError(getString(R.string.error_address));
        } else {
            int yearOfBirth = Integer.parseInt(age);
            int phoneNumber = Integer.parseInt(phone);
            etFullName.setEnabled(false);
            etNRIC.setEnabled(false);
            etAge.setEnabled(false);
            etPhone.setEnabled(false);
            etAddress.setEnabled(false);
            btnSignUp2.setEnabled(false);
            btnSignUp2Back.setEnabled(false);
            pbLoading.setVisibility(View.VISIBLE);
            mPatient.registerPatient(email, username, password, fullname, nric, yearOfBirth, phoneNumber, address);
        }
    }

    public void backToAccountInput() {
        etEmail.setEnabled(true);
        etUsername.setEnabled(true);
        etPassword.setEnabled(true);
        imgPassword.setEnabled(true);
        btnSignUp.setEnabled(true);
        btnSignUpBack.setEnabled(true);
        llAccountInputs.setVisibility(View.VISIBLE);
        llInformationInputs.setVisibility(View.GONE);
        etFullName.setEnabled(false);
        etNRIC.setEnabled(false);
        etAge.setEnabled(false);
        etPhone.setEnabled(false);
        etAddress.setEnabled(false);
        btnSignUp2.setEnabled(false);
        btnSignUp2Back.setEnabled(false);
    }
    /**
     * ================================= END OF FUNCTIONS =================================
     */
}
