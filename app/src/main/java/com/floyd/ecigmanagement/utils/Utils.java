package com.floyd.ecigmanagement.utils;

import android.app.Activity;
import android.widget.Toast;

import com.floyd.ecigmanagement.enums.Level;
import es.dmoral.toasty.Toasty;


public class Utils {

    public static void displayToastyToaster(final Activity activity, final Level level, final String message) {
        activity.runOnUiThread(new Thread(new Runnable() {
            public void run() {
                switch (level) {
                    case SUCCESS:
                        Toasty.success(activity, message, Toast.LENGTH_SHORT, false).show();
                        break;
                    case INFO:
                        Toasty.info(activity, message, Toast.LENGTH_SHORT, false).show();
                        break;
                    case WARNING:
                        Toasty.warning(activity, message, Toast.LENGTH_SHORT, false).show();
                        break;
                    case ERROR:
                        Toasty.error(activity, message, Toast.LENGTH_SHORT, false).show();
                        break;
                    default:
                        Toasty.normal(activity, message).show();
                        break;
                }

            }
        }));
    }

}
