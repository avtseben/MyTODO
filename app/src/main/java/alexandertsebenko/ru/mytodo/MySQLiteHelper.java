package alexandertsebenko.ru.mytodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

/**
 * Created by sas on 09.05.2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TODO_TEXT = "todo_text";
    public static final String COLUMN_EXPIRE_DATE = "expire_date";
    public static final String COLUMN_DONE_DATE = "done_date";
    public static final String COLUMN_DONE = "done";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "my_todo.db";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODOS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TODO_TEXT + " text not null, "
            + COLUMN_EXPIRE_DATE + " integer, "
            + COLUMN_DONE_DATE + " integer, "
            + COLUMN_DONE + " numeric default 0"
            + ");";

    public MySQLiteHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }
}
