package vone.person.com.yuxue.dbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by longyang on 2018/3/9.
 *
 */

public class SqliteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public SqliteHelper(Context context, String name ,int version) {
        super(context, name, null, version);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTabSql1 = "CREATE TABLE if not exists KEEP(_id integer primary key autoincrement," +
                " timeLong integer,timeStr text, x integer,y integer,z integer)";
        db.execSQL(createTabSql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
