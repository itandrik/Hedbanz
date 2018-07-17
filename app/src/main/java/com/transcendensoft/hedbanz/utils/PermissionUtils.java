package com.transcendensoft.hedbanz.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Utility class that processes runtime permissions
 *
 * @author Andrii Chernysh
 *         Developed by <u>Transcendensoft</u>
 */

public class PermissionUtils {
    private static final String MNC = "M";

    //Request codes
    public static final int CAMERA_AND_STORAGE_REQUEST_CODE = 1;
    public static final int READ_STORAGE_REQUEST_CODE = 2;
    public static final int STORAGE_REQUEST_CODE = 3;

    // Calendar group.
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;

    // Camera group.
    public static final String CAMERA = Manifest.permission.CAMERA;

    // Storage group
    public static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    // Contacts group.
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;

    // Location group.
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    // Microphone group.
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    // Phone group.
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public static final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public static final String USE_SIP = Manifest.permission.USE_SIP;
    public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;

    // Sensors group.
    public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String USE_FINGERPRINT = Manifest.permission.USE_FINGERPRINT;

    // SMS group.
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String READ_SMS = Manifest.permission.READ_SMS;
    public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    public static final String READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";

    // Bookmarks group.
    public static final String READ_HISTORY_BOOKMARKS = "com.android.browser.permission.READ_HISTORY_BOOKMARKS";
    public static final String WRITE_HISTORY_BOOKMARKS = "com.android.browser.permission.WRITE_HISTORY_BOOKMARKS";

    /**
     * Create an array from a given permissions.
     *
     * @throws IllegalArgumentException
     */
    public static String[] asArray(@NonNull String... permissions) {
        if (permissions.length == 0) {
            throw new IllegalArgumentException("There is no given permission");
        }

        final String[] dest = new String[permissions.length];
        for (int i = 0, len = permissions.length; i < len; i++) {
            dest[i] = permissions[i];
        }
        return dest;
    }

    /**
     * Check that given permission have been granted.
     */
    public static boolean hasGranted(int grantResult) {
        return grantResult == PERMISSION_GRANTED;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link android.content.pm.PackageManager#PERMISSION_GRANTED}.
     */
    public static boolean hasGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (!hasGranted(result)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the Context has access to a given permission.
     * Always returns true on platforms below M.
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        if (isMNC()) {
            return permissionHasGranted(context, permission);
        }
        return true;
    }

    /**
     * Returns true if the Context has access to all given permissions.
     * Always returns true on platforms below M.
     */
    public static boolean hasSelfPermissions(Context context, String[] permissions) {
        if (!isMNC()) {
            return true;
        }

        for (String permission : permissions) {
            if (!permissionHasGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Requests permissions to be granted to this application.
     */
    public static void requestAllPermissions(@NonNull Activity activity, @NonNull String[] permissions, int requestCode) {
        if (isMNC()) {
            internalRequestPermissions(activity, permissions, requestCode);
        }
    }

    /**
     * Requests permissions to be granted to this application.
     */
    public static void requestAllPermissions(@NonNull Fragment fragment, @NonNull String[] permissions, int requestCode) {
        if (isMNC()) {
            internalRequestPermissions(fragment, permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void internalRequestPermissions(Fragment fragment, String[] permissions, int requestCode) {
        if (fragment == null) {
            throw new IllegalArgumentException("Given activity is null.");
        }
        fragment.requestPermissions(permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void internalRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException("Given activity is null.");
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean permissionHasGranted(Context context, String permission) {
        return hasGranted(ContextCompat.checkSelfPermission(context, permission));
    }

    private static boolean isMNC() {
        return Build.VERSION.SDK_INT >= 23;
    }
}
