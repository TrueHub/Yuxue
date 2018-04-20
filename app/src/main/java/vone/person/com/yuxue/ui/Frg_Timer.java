package vone.person.com.yuxue.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import vone.person.com.yuxue.BaseFragment;
import vone.person.com.yuxue.R;
import vone.person.com.yuxue.thread.Write2CSV;
import vone.person.com.yuxue.tools.Configs;
import vone.person.com.yuxue.tools.DateUtils;
import vone.person.com.yuxue.view.MyLoadingView;

/**
 * Created by longyang on 2018/4/16.
 *
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
    private int[] colors;
    private ImageView iv_anim;
    private ObjectAnimator o1, o2, o3, o4;
    private TextView tv_1;
    private TextView tv_noteTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_content, container, false);
        initView(view);
        write2CSV = new Write2CSV();
        colors = new int[]{
                getContext().getResources().getColor(R.color.colorGreen),
                getContext().getResources().getColor(R.color.colorBlue),
                getContext().getResources().getColor(R.color.colorYellow)
        };
        setStartAnim(true, true);
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

    private void setStartAnim(final boolean start, boolean isInit) {

        if (!isInit) {
            final int btnDur = 300;
            final float scaleV = .95f, alphaV = .3f;
            ObjectAnimator oBtnS_A = ObjectAnimator.ofFloat(btn_pause, "alpha", 1, alphaV);
            oBtnS_A.setDuration(btnDur);
            oBtnS_A.start();
            oBtnS_A.setInterpolator(new LinearInterpolator());

            ObjectAnimator oBtnS_X = ObjectAnimator.ofFloat(btn_pause, "scaleX", 1, scaleV);
            oBtnS_X.setDuration(btnDur);
            oBtnS_X.setInterpolator(new LinearInterpolator());
            oBtnS_X.start();

            ObjectAnimator oBtnS_Y = ObjectAnimator.ofFloat(btn_pause, "scaleY", 1, scaleV);
            oBtnS_Y.setInterpolator(new LinearInterpolator());
            oBtnS_Y.setDuration(btnDur);
            oBtnS_Y.start();

            oBtnS_X.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (start) {
                        btn_pause.setImageDrawable(getResources().getDrawable(R.mipmap.btn_img_start));
                    } else {
                        btn_pause.setImageDrawable(getResources().getDrawable(R.mipmap.btn_img_finish));
                    }

                    ObjectAnimator oBtnS_A = ObjectAnimator.ofFloat(btn_pause, "alpha", alphaV, 1);
                    oBtnS_A.setDuration(btnDur);
                    oBtnS_A.setInterpolator(new LinearInterpolator());
                    oBtnS_A.start();

                    ObjectAnimator oBtnS_X = ObjectAnimator.ofFloat(btn_pause, "scaleX", scaleV, 1);
                    oBtnS_X.setDuration(btnDur);
                    oBtnS_X.setInterpolator(new LinearInterpolator());
                    oBtnS_X.start();

                    ObjectAnimator oBtnS_Y = ObjectAnimator.ofFloat(btn_pause, "scaleY", scaleV, 1);
                    oBtnS_Y.setInterpolator(new LinearInterpolator());
                    oBtnS_Y.setDuration(btnDur);
                    oBtnS_Y.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        ObjectAnimator o5, o6, o7;
        if (start) {

            o1 = ObjectAnimator.ofFloat(iv_anim, "alpha", 1, 0);
            o1.setRepeatCount(ValueAnimator.INFINITE);
            o1.setDuration(1800);
            o1.setStartDelay(1800);
            o1.start();

            o2 = ObjectAnimator.ofFloat(iv_anim, "scaleX", 1, 1.5f);
            o3 = ObjectAnimator.ofFloat(iv_anim, "scaleY", 1, 1.5f);

            o2.setRepeatCount(ValueAnimator.INFINITE);
            o2.setDuration(1800);
            o2.setStartDelay(1200);
            o2.start();
            o3.setRepeatCount(ValueAnimator.INFINITE);
            o3.setStartDelay(1200);
            o3.setDuration(1800);
            o3.start();

            if (o4 != null && o4.isRunning() && o4.isStarted()) {
                o4.end();
            }
            o4 = ObjectAnimator.ofFloat(loadingView, "alpha", 1, 0);
            o4.setDuration(500);
            o4.start();

            o5 = ObjectAnimator.ofFloat(tv_time, "alpha", 1, 0);
            o5.setDuration(500);
            o5.start();

            o6 = ObjectAnimator.ofFloat(tv_1, "alpha", 1, 0);
            o6.setDuration(500);
            o6.start();

            o7 = ObjectAnimator.ofFloat(tv_noteTime, "alpha", 1, 0);
            o7.setDuration(500);
            o7.start();

            o1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    iv_anim.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            o4.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    loadingView.setVisibility(View.GONE);
                    tv_time.setVisibility(View.GONE);
                    tv_1.setVisibility(View.GONE);
                    tv_noteTime.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            Log.d(TAG, "setStartAnim: ");

        } else {

            if (o1 != null) {
                o1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        if (o1 != null && (o1.isRunning() || o1.isStarted())) {
                            o1.cancel();
                            o1 = null;

                            if (o2 != null) {
                                o2.cancel();
                                o2 = null;
                            }

                            if (o3 != null) {
                                o3.cancel();
                                o3 = null;
                            }

                            iv_anim.setVisibility(View.GONE);
                        }
                    }
                });
            }
            loadingView.setVisibility(View.VISIBLE);
            o4 = ObjectAnimator.ofFloat(loadingView, "alpha", 0, 1);
            o4.setDuration(600);
            o4.start();

            o5 = ObjectAnimator.ofFloat(tv_time, "alpha", 0, 1);
            o5.setDuration(800);
            o5.setStartDelay(500);
            o5.start();

            o6 = ObjectAnimator.ofFloat(tv_1, "alpha", 0, 1);
            o6.setDuration(500);
            o6.setStartDelay(1100);
            o6.start();

            o7 = ObjectAnimator.ofFloat(tv_noteTime, "alpha", 0, 1);
            o7.setDuration(800);
            o7.setStartDelay(800);
            o7.start();

            o4.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            o5.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    tv_time.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            o6.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if (!Configs.M_TIME.equals("00:00.0")) {
                        tv_1.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            o7.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if (!Configs.M_TIME.equals("00:00.0")) {
                        tv_noteTime.setVisibility(View.VISIBLE);
                        tv_noteTime.setText(Configs.M_TIME);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }


    private void initView(View view) {
        loadingView = (MyLoadingView) view.findViewById(R.id.loadingView);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        btn_pause = (ImageView) view.findViewById(R.id.btn_pause);
        btn_pause.setTag(0);
        btn_pause.setOnClickListener(this);

        loadingView.setCircleEnd(new MyLoadingView.CircleEnd() {
            @Override
            public void setCircleNumListener(int circleNum) {
                tv_time.setTextColor(colors[circleNum]);
            }
        });
        iv_anim = (ImageView) view.findViewById(R.id.img_anim);
        tv_1 = (TextView) view.findViewById(R.id.tv_1);
        tv_noteTime = (TextView) view.findViewById(R.id.tv_noteTime);
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
        write2CSV.writeData(str, Configs.FILE_NAME);
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
                    }
                    startTime = DateUtils.getCurrentDate();
                    tv_time.setTextColor(colors[0]);
                    setStartAnim(false, false);
                    break;
                } else {
                    setStartAnim(true, false);
                    endTime = DateUtils.getCurrentDate();

                    saveInfo();
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
                        tv_time.setTextColor(getResources().getColor(R.color.colorGray));
                        loadingView.setInit();
                    }
                }
        }
    }

    private void saveInfo() {
        String standTime = tv_time.getText().toString();
        boolean a = DateUtils.compareTime(standTime, "00:10.00");
        if (a) {
            AlertDialog dialog = new AlertDialog.Builder(this.getActivity(), R.style.MyDialogStyle)
                    .setMessage("宝贝这次有点太虚了吧...\n\n来叔亲一个补点糖~\n😘 😚")
                    .setPositiveButton("我要再撑一次!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(getActivity(), "😘 😚", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create();
            dialog.show();

            //设置dialog的宽度
            WindowManager windowManager = getActivity().getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth() * .8f); //设置宽度
            dialog.getWindow().setAttributes(lp);
        } else {
            setTime();
        }
    }
}
