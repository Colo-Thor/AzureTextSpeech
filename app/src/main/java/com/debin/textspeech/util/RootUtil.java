package com.debin.textspeech.util;

import java.io.File;

/**
 * Root检查
 */
public class RootUtil {
    public static boolean checkRoot() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
                "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
                "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean requestRootPermission() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("", true);
        return result.result == 0;
    }
}
