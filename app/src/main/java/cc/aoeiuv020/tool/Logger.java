package cc.aoeiuv020.tool;

import android.content.Context;
import android.util.Log;

/**
 * Created by AoEiuV020 on 2016/04/11 - 02:53:47
 */
public class Logger {
    public static final String TAG = "aoeiuv020 Logger";
    public static final boolean DEBUG = true;

    private Logger() {
    }

    public static void s(Context context, String name, String format, Object... parms) {
        String log = String.format(format + "\n", parms);
        boolean b = false;
        if (DEBUG) {
            b = Stream.write(context, name, log);
            Log.v(TAG, String.format("write <%s> %d : %s", name, log.length(), b));
        }
    }

    public static void v(String format, Object... parms) {
        if (DEBUG)
            Log.v(TAG, String.format(format + "\n", parms));
    }

    public static void e(Throwable throwable) {
        v("e " + throwable);
        if (DEBUG)
            throw new RuntimeException(throwable);
    }
}
