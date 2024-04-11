package com.debin.textspeech.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;

import com.debin.textspeech.R;
import com.debin.textspeech.util.PermissionUtils;
import com.debin.textspeech.util.RootUtil;
import com.debin.textspeech.util.ShellUtils;
import com.debin.textspeech.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * 权限基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected boolean permissionGranted = false;

    public abstract void init();

    /**
     * 请求权限
     */
    public void setUpSplash() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        final View decorView = getWindow().getDecorView();
//        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int i) {
//                decorView.setSystemUiVisibility(uiOptions);
//            }
//        });

        ThreadUtils.runOnUiThread(() -> requestPermission(), 1000);
    }

    private PermissionUtils.PermissionGrant mGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            permissionGranted = true;
            init();
        }

        @Override
        public void onPermissionCancel() {
            Toast.makeText(BaseActivity.this.getApplicationContext(), getString(R.string.permission_request), Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    private void requestPermission() {
        String[] permissions = null;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions = packageInfo.requestedPermissions;
        } catch (Exception e) {
        }

        if (!isNeedRequestPermission(permissions)) {
            mGrant.onPermissionGranted(0);
            return;
        }

        // Root方式获取权限
        if (RootUtil.checkRoot() && RootUtil.requestRootPermission()) {
            String packageName = getPackageName();

            List<String> commands = new ArrayList<>();
            for (String permission : permissions) {
                commands.add("pm grant " + packageName + " " + permission);
            }
            ShellUtils.execCmd(commands, true, false);

            if (!isNeedRequestPermission(permissions)) {
                mGrant.onPermissionGranted(0);
                return;
            }
        }


        // 普通方式获取权限
        PermissionUtils.requestMultiPermissions(this, permissions, mGrant);
    }

    private boolean isNeedRequestPermission(String[] permissions) {
        boolean needRequestPermission = false;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermission = true;
                break;
            }
        }
        return needRequestPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.CODE_MULTI_PERMISSION) {
            PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mGrant);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtils.REQUEST_CODE_SETTING) {
            new Handler().postDelayed(this::requestPermission, 500);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
