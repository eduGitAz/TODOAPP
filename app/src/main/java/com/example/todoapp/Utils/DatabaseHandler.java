package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "TODO_DATABASE";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + "TEXT, " + STATUS + " INTEGER)";

    private SQLiteDatabase database;

    private DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS" + TODO_TABLE);
        onCreate(database);
    }

    public void openDatabase() {
        database = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task.getTask());
        contentValues.put(STATUS, 0);
        database.insert(TODO_TABLE, null, contentValues);
    }

    public List<ToDoModel> getAllTask(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        database.beginTransaction();
            try{
                    cursor = database.query(TODO_TABLE, null, null, null, null, null, null, null);
                    if(cursor != null){
                        if(cursor.moveToFirst()){
                            do{
                                ToDoModel task = new ToDoModel();
                                task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                                task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                                task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                                taskList.add(task);
                            } while (cursor.moveToNext());
                        }
                    }
               }
            finally {
                database.endTransaction();
                cursor.close();
            }
            return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        database.update(TODO_TABLE, contentValues, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, int task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task);
        database.update(TODO_TABLE, contentValues, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        database.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

}
