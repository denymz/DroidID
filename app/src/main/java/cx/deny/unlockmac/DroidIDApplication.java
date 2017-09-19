package cx.deny.unlockmac;

import android.app.Application;

import com.github.ajalt.reprint.core.Reprint;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

public class DroidIDApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://wd5576909517wtazcd.wilddogio.com/").build();
            WilddogApp.initializeApp(this,options);
            Reprint.initialize(this);
            Parse.initialize(this, "9Sqtp48dHVbG0q8ebUQbSpz5ONmFE8SrdvLGUtOl", "L54fYaalPMbVdLtwXsDpry5fFoqbeNuJagD3aDQO");
            ParseInstallation.getCurrentInstallation().saveInBackground();
        }
}
