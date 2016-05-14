package alexandertsebenko.ru.mytodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
    TodosDataSource - класс который формирует объект TodoInstance из данных БД
    и наоборот из объекта формирует insert в БД
 */
public class TodosDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
                                    MySQLiteHelper.COLUMN_TODO_TEXT,
                                    MySQLiteHelper.COLUMN_EXPIRE_DATE,
                                    MySQLiteHelper.COLUMN_DONE_DATE,
                                    MySQLiteHelper.COLUMN_DONE,
    };
    public TodosDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    public TodoInstance createTodoInstance(String todoText, long expireDate, long doneDate, boolean done) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TODO_TEXT, todoText);
        values.put(MySQLiteHelper.COLUMN_EXPIRE_DATE, expireDate);
        values.put(MySQLiteHelper.COLUMN_DONE_DATE, doneDate);
        values.put(MySQLiteHelper.COLUMN_DONE, done);
        long insertId = db.insert(MySQLiteHelper.TABLE_TODOS, null,values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_TODOS, allColumns,
                                MySQLiteHelper.COLUMN_ID + " = " + insertId,
                                null, null, null, null);
        cursor.moveToFirst();
        TodoInstance newTodoText = cursorToTodoInstance(cursor);
        cursor.close();
        Log.d("mylog insert todo",values.toString() + " in table " + MySQLiteHelper.TABLE_TODOS);
        return newTodoText;
    }

    public void deleteTodoInstance(TodoInstance todoText) {
        long id = todoText.getID();
        System.out.println("TodoInstance deleted with id: " + id);
        db.delete(MySQLiteHelper.TABLE_TODOS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }
    public void deleteTodoInstanceByText(String todoText) {
        System.out.println("TodoInstance deleted with text: " + todoText);
        db.delete(MySQLiteHelper.TABLE_TODOS, MySQLiteHelper.COLUMN_TODO_TEXT
                + " = " + todoText, null);
    }
    public void deleteTodoInstanceByID(long _id) {
        System.out.println("TodoInstance deleted with id: " + _id);
        db.delete(MySQLiteHelper.TABLE_TODOS, MySQLiteHelper.COLUMN_ID
                + " = " + _id, null);
    }
    public void updateSetDoneTodoInstanceByText(String todoText, long currentUnixTime) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(MySQLiteHelper.COLUMN_DONE, true);
        updateValues.put(MySQLiteHelper.COLUMN_DONE_DATE, currentUnixTime);
        db.update(MySQLiteHelper.TABLE_TODOS, updateValues,
                  MySQLiteHelper.COLUMN_TODO_TEXT + " = '" + todoText + "'",
                  null);
    }
    public void updateSetTextTodoInstanceByText(String todoText, String newTodoText) {
        ContentValues updateValues = new ContentValues();
        updateValues.put(MySQLiteHelper.COLUMN_TODO_TEXT, newTodoText);
        db.update(MySQLiteHelper.TABLE_TODOS, updateValues,
                MySQLiteHelper.COLUMN_TODO_TEXT + " = '" + todoText + "'",
                null);
    }
    public ArrayList<TodoInstance> getAllTodoInstances() {
        ArrayList<TodoInstance> todoTexts = new ArrayList<TodoInstance>();

        Cursor cursor = db.query(MySQLiteHelper.TABLE_TODOS,
                allColumns, null, null, null, null, "_id" + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TodoInstance todoText = cursorToTodoInstance(cursor);
            todoTexts.add(todoText);
            cursor.moveToNext();
        }
        cursor.close();
        return todoTexts;
    }

    private TodoInstance cursorToTodoInstance(Cursor cursor) {
        TodoInstance todoText = new TodoInstance();
        todoText.setID(cursor.getLong(0));
        todoText.setTodoText(cursor.getString(1));
        todoText.setExpireDate(cursor.getLong(2));
        todoText.setDoneDate(cursor.getLong(3));
        if(1 == cursor.getInt(4))
            todoText.setDone(true);
        else
            todoText.setDone(false);
        return todoText;
    }
}
