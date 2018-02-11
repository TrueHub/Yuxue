package vone.person.com.yuxue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import vone.person.com.yuxue.view.MyLoadingView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private MyLoadingView loadingView;
    private int progress = 0;
    private TextView tv_time;
    private ImageView btn_pause;
    private long startTime;
    private Handler handler;
    private int mlCount;
    private int totalSec;
    private int yushu;
    private int min;
    private int sec;
    private Timer timer;
    private TimerTask task;
    private Message msg;
    private long mlTimerUnit = 10;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case 1:
                        mlCount++;
                        totalSec = 0;
                        // 100 millisecond
                        totalSec = (int) (mlCount / 100);
                        yushu = (int) (mlCount % 100);
                        // Set time display
                        min = (totalSec / 60);
                        sec = (totalSec % 60);
                        String m = String.valueOf(yushu);
                        if (yushu < 10) {
                            m = "0" + yushu;
                        }
                        progress ++;
                        loadingView.setmProgress(progress);
                        try {
                            // 100 millisecond
                            tv_time.setText(String.format("%1$02d:%2$02d.%3$s", min, sec, m));
                        } catch (Exception e) {
                            tv_time.setText("" + min + ":" + sec + "." + m);
                            e.printStackTrace();
                            Log.e("MyTimer onCreate", "Format string error.");
                        }
                        break;

                    default:
                        break;
                }

                super.handleMessage(msg);
            }

        };


    }

    private void initView() {
        loadingView = (MyLoadingView) findViewById(R.id.loadingView);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_pause = (ImageView) findViewById(R.id.btn_pause);
        btn_pause.setTag(0);
        btn_pause.setOnClickListener(this);
    }

    private void setTime() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pause:
                if ((int) view.getTag() == 0) {
                    view.setTag(1);
                    if (null == timer) {
                        if (null == task) {
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    if (null == msg) {
                                        msg = new Message();
                                    } else {
                                        msg = Message.obtain();
                                    }
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                }

                            };
                        }
                        timer = new Timer(true);
                        timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer duration

                    }
                    break;
                } else {
                    view.setTag(0);
                    if (null != timer) {
                        task.cancel();
                        task = null;
                        timer.cancel(); // Cancel timer
                        timer.purge();
                        timer = null;
                        handler.removeMessages(msg.what);
                    }
                    mlCount = 0;
                    // 100 millisecond
                    tv_time.setText("00:00.00");
                    loadingView.setInit();
                }
        }
    }
}
