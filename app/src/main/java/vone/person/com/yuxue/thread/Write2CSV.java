package vone.person.com.yuxue.thread;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import vone.person.com.yuxue.tools.Configs;

/**
 * Created by longyang on 2018/4/10.
 */

public class Write2CSV {

    public void writeData(String content, String name) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//如果不存在SD卡,
            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
        }
        String fileDir = Configs.FILE_DIR;
        File dirFile = new File(fileDir);
        if (!dirFile.exists()) dirFile.mkdirs();

        File csvFile = new File(fileDir + "/" + name);

        if (!csvFile.exists()) {
            try {
                if (csvFile.createNewFile()) {
                    addToFileByFileWriter(csvFile.getAbsolutePath(), "startTime,endTime,diffTime,name,remark\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addToFileByFileWriter(csvFile.getAbsolutePath(), content);
    }

    // 以追加形式写文件:写文件器，构造函数中的第二个参数为true
    private void addToFileByFileWriter(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
