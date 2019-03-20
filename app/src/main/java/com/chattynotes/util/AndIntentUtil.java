package com.chattynotes.util;

import android.content.Context;
import android.content.Intent;
import com.chattynotes.mvp.activities.MainActivity;

public final class AndIntentUtil {

    public static void restartApp(Context ctx) {
        Intent in = new Intent(ctx, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(in);
    }
}
