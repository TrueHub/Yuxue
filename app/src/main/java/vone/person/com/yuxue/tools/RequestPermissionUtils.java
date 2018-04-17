package vone.person.com.yuxue.tools;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dell on 2017-3-30.
 *
 *通讯录
 group:android.permission-group.CONTACTS
 permission:android.permission.WRITE_CONTACTS
 permission:android.permission.GET_ACCOUNTS
 permission:android.permission.READ_CONTACTS

 电话
 group:android.permission-group.PHONE
 permission:android.permission.READ_CALL_LOG
 permission:android.permission.READ_PHONE_STATE
 permission:android.permission.CALL_PHONE
 permission:android.permission.WRITE_CALL_LOG
 permission:android.permission.USE_SIP
 permission:android.permission.PROCESS_OUTGOING_CALLS
 permission:com.android.voicemail.permission.ADD_VOICEMAIL

 日历
 group:android.permission-group.CALENDAR
 permission:android.permission.READ_CALENDAR
 permission:android.permission.WRITE_CALENDAR

 相机
 group:android.permission-group.CAMERA
 permission:android.permission.CAMERA

 传感器
 group:android.permission-group.SENSORS
 permission:android.permission.BODY_SENSORS

 定位
 group:android.permission-group.LOCATION
 permission:android.permission.ACCESS_FINE_LOCATION
 permission:android.permission.ACCESS_COARSE_LOCATION

 存储
 group:android.permission-group.STORAGE
 permission:android.permission.READ_EXTERNAL_STORAGE
 permission:android.permission.WRITE_EXTERNAL_STORAGE

 audio
 group:android.permission-group.MICROPHONE
 permission:android.permission.RECORD_AUDIO

 短信
 group:android.permission-group.SMS
 permission:android.permission.READ_SMS
 permission:android.permission.RECEIVE_WAP_PUSH
 permission:android.permission.RECEIVE_MMS
 permission:android.permission.RECEIVE_SMS
 permission:android.permission.SEND_SMS
 permission:android.permission.READ_CELL_BROADCASTS

 */
public class RequestPermissionUtils {

    static final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;

    /**
     * @param activity    因为要弹Dialog，所以传入Activity的实例
     * @param permissions 权限数组,如  String[] permissions = new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE};
     * @param str         向用户解释需要权限的必要性
     */
    public static void requestPermission(final Activity activity, final String[] permissions, String str) {
        /*@param indexs 权限列表,未满足的个数以及坐标*/
        List<Integer> indexs = isPermissionComplete(activity, permissions);

        if (indexs.size() == 0) return;

        List<String> unCompletePermissionList = new ArrayList<>();
        for (int j = 0; j < indexs.size(); j++) {
            unCompletePermissionList.add(permissions[indexs.get(j)]);
        }

        final String[] unCompletePermissions = unCompletePermissionList.toArray(new String[unCompletePermissionList.size()]);

        // Should we show an explanation?  嗯 和用户低头，解释，请求。

        if (shouldShow(activity, unCompletePermissions)) {

            Log.i("MSL", "requestPermission: 解释");
            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            showMessageOKCancel(activity, str,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, unCompletePermissions,
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });

        } else {
            Log.i("MSL", "requestPermission: 不解释");
            // No explanation needed, we can request the permission.不解释，直接申请

            ActivityCompat.requestPermissions(activity,
                    unCompletePermissions,
                    REQUEST_CODE_ASK_PERMISSIONS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }


    }

    /**
     * @param activity
     * @param permissions 必须满足的权限
     * @return 决定了未满足权限的个数以及permissions中的具体位置
     */
    private static List<Integer> isPermissionComplete(Activity activity, String[] permissions) {
        ArrayList<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED !=
                    ContextCompat.checkSelfPermission(activity, permissions[i])
                    ) {
                indexs.add(i);
            }
        }
        return indexs;
    }

    private static boolean shouldShow(Activity activity, String[] unCompletePermissions) {
        for (String unCompletePermission : unCompletePermissions) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    unCompletePermission)) return true;
        }
        return false;
    }

    private static void showMessageOKCancel(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("确认", okListener)
                .create()
                .show();
    }
}
