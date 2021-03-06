package vone.person.com.yuxue.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.animation.LinearInterpolator;

import java.util.List;

import vone.person.com.yuxue.BaseActivity;
import vone.person.com.yuxue.R;
import vone.person.com.yuxue.view.BrandTextView;

import static android.content.pm.PackageManager.GET_PROVIDERS;

public class SplanshActivity extends BaseActivity {

    private BrandTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splansh);
        initView();
        if (!isAddShortCut(this)) {
            addShortcut(this, getString(R.string.app_name));
        }
        //此activity进入
        getWindow().setEnterTransition(new Explode().setDuration(500));
        //此activity退出
        getWindow().setExitTransition(new Explode().setDuration(500));
        splansh();

    }

    private void splansh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(SplanshActivity.this, MainActivity.class));
                SplanshActivity.this.finish();
            }
        }).start();

        ObjectAnimator anm = ObjectAnimator.ofFloat(textView, "alpha", 1, 0.2f);
        anm.setInterpolator(new LinearInterpolator());
        anm.setStartDelay(1300);
        anm.setDuration(1000);
        anm.start();

    }

    public static void addShortcut(Activity cx, String name) {
        //    创建快捷方式的intent广播
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //   添加快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //  快捷图标是允许重复
        shortcut.putExtra("duplicate", false);
        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(cx, R.mipmap.snow_f);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // 下次启动要用的Intent信息
        Intent carryIntent = new Intent(Intent.ACTION_MAIN);
        carryIntent.putExtra("name", name);
        carryIntent.setClassName(cx.getPackageName(), cx.getClass().getName());
        carryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  添加携带的Intent
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, carryIntent);
        //  发送广播
        cx.sendBroadcast(shortcut);
    }

    //判断是否创建过了快捷方式
    public static boolean isAddShortCut(Context context) {
        boolean isInstallShortcut = false;
        ContentResolver cr = context.getContentResolver();
        String AUTHORITY = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

        Cursor c = cr.query(CONTENT_URI, new String[]{"title"}, "title=?", new String[]{context.getString(R.string.app_name)}, null);

        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }

        if (c != null) {
            c.close();
        }
        return isInstallShortcut;
    }

    public static String getAuthorityFromPermission(Context context, String permission) {
        if (TextUtils.isEmpty(permission)) {
            return null;
        }
        List<PackageInfo> packInfos = context.getPackageManager().getInstalledPackages(GET_PROVIDERS);
        if (packInfos == null) {
            return null;
        }
        for (PackageInfo info : packInfos) {
            ProviderInfo[] providers = info.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                        return provider.authority;
                    }
                }
            }
        }
        return null;
    }

    private void initView() {
        textView = (BrandTextView) findViewById(R.id.textView);
        String[] arr = getResources().getStringArray(R.array.qh);

        int index = (int) (Math.random() * arr.length);
        textView.setText(arr[index]);
    }
}
