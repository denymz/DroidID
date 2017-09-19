package cx.deny.unlockmac;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import com.mattprecious.swirl.SwirlView;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;

public class ScanActivity extends AppCompatActivity {

    String authCode;
    TextView result;
    SwirlView sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_scan);
        Reprint.initialize(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        authCode = preferences.getString("authCode", "");

        getSupportActionBar().setTitle("Authentication");
        getSupportActionBar().setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sw = (SwirlView)findViewById(R.id.sw);
            sw.setState(SwirlView.State.ON, true);
        }

        result = (TextView)findViewById(R.id.textViewStatus);

        Reprint.authenticate(new AuthenticationListener() {
            @Override
            public void onSuccess(int moduleTag) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sw.setState(SwirlView.State.OFF, true);
                }
                result.setText("Authenticated successfully!");
                SyncReference myFirebaseRef = WilddogSync.getInstance().getReference();
                myFirebaseRef = myFirebaseRef.child("users").child(authCode).child("unlockMac");
                myFirebaseRef.setValue(true);
                Reprint.cancelAuthentication();
            }

            @Override
            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int moduleTag, int errorCode) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sw.setState(SwirlView.State.ERROR, true);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sw.setState(SwirlView.State.ON);
                        }
                    }, 1000);
                }
                result.setText(errorMessage);
            }
        }, Reprint.DEFAULT_RESTART_COUNT);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

