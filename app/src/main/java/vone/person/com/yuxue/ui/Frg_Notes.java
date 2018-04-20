package vone.person.com.yuxue.ui;

import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.HorizontalBarChartView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import io.reactivex.functions.Consumer;
import vone.person.com.yuxue.BaseFragment;
import vone.person.com.yuxue.R;
import vone.person.com.yuxue.rxbeans.Land;
import vone.person.com.yuxue.tools.Configs;
import vone.person.com.yuxue.tools.DateUtils;
import vone.person.com.yuxue.tools.RxBus;

public class Frg_Notes extends BaseFragment {
    private static final String TAG = "MSL Frg_Notes";
    private HorizontalBarChartView mHorizontalBarChart;
    private String lastLongTime;
    private TextView tv_maxTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg__notes, container, false);
        initView(view);
        readNote();

        setRxRcv();

        return view;
    }

    private void setRxRcv() {
        RxBus.getDefault().subscribe(Land.class, new Consumer<Land>() {
            @Override
            public void accept(Land land) throws Exception {
                if (land == Land.PauseTime) {
//                    readNote();
                }
            }
        });
    }

    private void readNote() {

        new ReadCSVThread(Configs.FILE_DIR, Configs.FILE_NAME).start();
    }

    class ReadCSVThread extends Thread {
        String fileName;
        String folder;

        private ReadCSVThread(String folder, String fileName) {
            this.folder = folder;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            super.run();
            File inFile = new File(folder + File.separator + fileName);
            final StringBuilder cSb = new StringBuilder();
            String inString;
            try {
                BufferedReader reader =
                        new BufferedReader(new FileReader(inFile));
                while ((inString = reader.readLine()) != null) {
                    cSb.append(inString).append("\n");
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(cSb)) {
                Looper.prepare();
                syncData(cSb.toString());
                Looper.loop();
            }
        }
    }

    private void syncData(String str) {
        String[] notes = str.split("\n");
        String[] lables = new String[notes.length - 1];

        float[] time = new float[notes.length - 1];

        BarSet barSet = new BarSet();

        for (int i = 0, len = notes.length - 1; i < len; i++) {
            String[] a = notes[i + 1].split(",");

            String dlTime = a[2];
            if (!DateUtils.compareTime(dlTime, lastLongTime)) lastLongTime = dlTime;
        }
        Configs.M_TIME = lastLongTime;

        if (TextUtils.isEmpty(lastLongTime)) {
            tv_maxTime.setVisibility(View.GONE);
        } else {
            tv_maxTime.setVisibility(View.VISIBLE);
            tv_maxTime.setText("当前记录：" + lastLongTime);
        }

        for (int len = notes.length - 1, i = len > 10 ? len - 10 : 0; i < len; i++) {
            String[] a = notes[i + 1].split(",");
            lables[i] = a[0];
            time[i] = Float.parseFloat(a[2].replace(".", "").replace(":", "."));
            Bar bar = new Bar(a[0], time[i]);
            bar.setColor(getResources().getColor(R.color.colorGreen));

//            bar.animateColor(getResources().getColor(R.color.colorWhite), getResources().getColor(R.color.colorBlue));
            barSet.addBar(bar);
        }
//        barSet.setColor(getResources().getColor(R.color.colorRed));
        mHorizontalBarChart.setLabelsColor(getResources().getColor(R.color.colorWhite));
        mHorizontalBarChart.setAxisColor(getResources().getColor(R.color.colorWhite));
        mHorizontalBarChart.setBarSpacing(20);
        mHorizontalBarChart.addData(barSet);
        mHorizontalBarChart.setContentDescription("分钟");
        mHorizontalBarChart.show();


    }

    private void initView(View view) {
        mHorizontalBarChart = (HorizontalBarChartView) view.findViewById(R.id.mHorizontalBarChart);
        tv_maxTime = (TextView) view.findViewById(R.id.tv_maxTime);
    }

}
