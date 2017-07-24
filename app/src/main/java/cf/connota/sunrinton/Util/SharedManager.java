package cf.connota.sunrinton.Util;

import android.content.SharedPreferences;

/**
 * Created by Conota on 2017-07-25.
 */

public class SharedManager {
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;

    public SharedManager() {

    }

    public static void save() {
        editor.commit();
    }
}
