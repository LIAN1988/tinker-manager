package com.dx168.patchsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;

/**
 * Created by jianjun.lin on 2016/12/2.
 */

public class DebugReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle data = intent.getExtras();
        if (data == null) {
            return;
        }
        final String packageName = data.getString("packageName");
        if (!TextUtils.equals(packageName, context.getPackageName())) {
            return;
        }
        int what = data.getInt("what");
        if (what == 1) {
            PatchManager.getInstance().queryAndApplyPatch(new SimplePatchListener() {
                @Override
                public void onApplySuccess() {
                    Intent intent = new Intent("com.dx168.patchtool.RECEIVE_APPLY_PATCH_RESULT");
                    intent.putExtra("packageName", packageName);
                    intent.putExtra("success", true);
                    context.sendBroadcast(intent);
                }

                @Override
                public void onApplyFailure(String msg) {
                    Intent intent = new Intent("com.dx168.patchtool.RECEIVE_APPLY_PATCH_RESULT");
                    intent.putExtra("packageName", packageName);
                    intent.putExtra("success", false);
                    context.sendBroadcast(intent);
                }
            });
        } else if (what == 2) {
            RestartService.start(context);
            System.exit(0);
            Process.killProcess(Process.myPid());
        }
    }
}
