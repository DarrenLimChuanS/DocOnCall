package doc.on.call;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import doc.on.call.Fragments.DocOnCallFragment;
import doc.on.call.Fragments.PatientFragment;
import doc.on.call.Fragments.SettingFragment;
import doc.on.call.Utilities.ObscuredSharedPreference;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    // Shared Preference
    private ObscuredSharedPreference mSharedPreference;

    // Declare variables
    private LinearLayout navHome;
    private ImageView imgHome;
    private TextView tvHome;

    private LinearLayout navDocOnCall;

    private LinearLayout navSetting;
    private ImageView imgSetting;
    private TextView tvSetting;

    // Fragments
    private PatientFragment patientFragment;
    private DocOnCallFragment docOnCallFragment;
    private SettingFragment settingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Shared Preference
        mSharedPreference = ObscuredSharedPreference.getPref(this);
        Log.d(TAG, "Hello");
        Log.d(TAG, "Token: " + mSharedPreference.readJWTToken());
        Log.d(TAG, "Nonce: " + mSharedPreference.readNonce());

        // Fetch variables
        navHome = (LinearLayout)findViewById(R.id.navHome);
        imgHome = (ImageView)findViewById(R.id.imgHome);
        tvHome = (TextView)findViewById(R.id.tvHome);
        navDocOnCall = (LinearLayout)findViewById(R.id.navDocOnCall);
        navSetting = (LinearLayout)findViewById(R.id.navSetting);
        imgSetting = (ImageView)findViewById(R.id.imgSetting);
        tvSetting = (TextView)findViewById(R.id.tvSetting);

        patientFragment = new PatientFragment(this);
        docOnCallFragment = new DocOnCallFragment(this);
        settingFragment = new SettingFragment(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, patientFragment).commit();
        imgHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_blue));
        tvHome.setTextColor(getColor(R.color.blue));

        // Event Listener
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateHome();
            }
        });

        navDocOnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDocOnCall();
            }
        });

        navSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateSetting();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void inflateHome() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.patientFragment).commit();
        imgHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_blue));
        tvHome.setTextColor(getResources().getColor(R.color.blue));
        imgSetting.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting));
        tvSetting.setTextColor(getResources().getColor(R.color.grey));
    }

    public void inflateDocOnCall() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.docOnCallFragment).commit();
        imgHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        tvHome.setTextColor(getResources().getColor(R.color.grey));
        imgSetting.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting));
        tvSetting.setTextColor(getResources().getColor(R.color.grey));
    }

    public void inflateSetting() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, this.settingFragment).commit();
        imgHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        tvHome.setTextColor(getResources().getColor(R.color.grey));
        imgSetting.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting_blue));
        tvSetting.setTextColor(getResources().getColor(R.color.blue));
    }

}