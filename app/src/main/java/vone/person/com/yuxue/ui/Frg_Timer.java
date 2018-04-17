package vone.person.com.yuxue.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import vone.person.com.yuxue.BaseFragment;
import vone.person.com.yuxue.R;
import vone.person.com.yuxue.thread.Write2CSV;
import vone.person.com.yuxue.tools.DateUtils;
import vone.person.com.yuxue.view.MyLoadingView;

/**
 * Created by longyang on 2018/4/16.
 */

public class Frg_Timer extends BaseFragment implements View.OnClickListener {
    private MyLoadingView loadingView;
    private int progress = 0;
    private TextView tv_time;
    private ImageView btn_pause;
    private String startTime, endTime;
    private Handler handler;
    private int mlCount;
    private int totalSec;
    private int yushu;
    private int min;
    private int sec;
    private Timer timer;
    private TimerTask task;
    private Message msg;
    private Write2CSV write2CSV;
    private static final String TAG = "MSL Frg_Timer";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_content, container, false);
        initView(view);
        write2CSV = new Write2CSV();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
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
                        progress++;
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
        return view;
    }


    private void initView(View view) {
        loadingView = (MyLoadingView) view.findViewById(R.id.loadingView);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        btn_pause = (ImageView) view.findViewById(R.id.btn_pause);
        btn_pause.setTag(0);
        btn_pause.setOnClickListener(this);
    }

    private void setTime() {

        String diffTime = tv_time.getText().toString();

        Log.i(TAG, "setTime: start :" + startTime + " , endTime :" + endTime + " , diffTime :" + diffTime);

        String str = startTime + "," + //开始时间
                endTime + "," +//结束时间
                diffTime + "," +//持续时间
                "Yan Xue" + "," +//姓名
                "," +//备注
                "\n";
        write2CSV.writeData(str, "barde.csv");

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
                        long mlTimerUnit = 10;
                        timer.schedule(task, mlTimerUnit, mlTimerUnit); // set timer duration
                        btn_pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_24dp));
                    }
                    startTime = DateUtils.getCurrentDate();
                    break;
                } else {
                    endTime = DateUtils.getCurrentDate();

                    saveInfo();
                    btn_pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_24dp));
                    view.setTag(0);
                    progress = 0;
                    if (null != timer) {
                        task.cancel();
                        task = null;
                        timer.cancel(); // Cancel timer
                        timer.purge();
                        timer = null;
                        handler.removeMessages(msg.what);
                        mlCount = 0;
                        // 100 millisecond
                        tv_time.setText("00:00.00");
                        loadingView.setInit();
                    }
                }
        }
    }

    private void saveInfo() {
        String standTime = tv_time.getText().toString();
        boolean a = compareTime(standTime, "00:10.00");
        if (a) {
            Toast.makeText(this.getActivity(), "不会这么弱吧!!!", Toast.LENGTH_SHORT).show();
        } else {
            setTime();
        }
    }

    private boolean compareTime(String srcTime, String intentTime) {
        String srcMin = srcTime.substring(0, 2);
        String srcSec = srcTime.substring(3, 5);
        String intentMin = intentTime.substring(0, 2);
        String intentSec = intentTime.substring(3, 5);

        float src = Float.valueOf(srcMin + "." + srcSec);
        float intent = Float.valueOf(intentMin + "." + intentSec);

        Log.d(TAG, "compareTime: " + src + " , " + intent);
        return src < intent;
    }

}
