package util.book;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseConnection extends SQLiteOpenHelper {

	private final static int DATABASE_VERSION = 1;// 数据库版本号
	private final static String DATABASE_NAME = "book.db";// 数据库名
	private static Context context;

	public static void setContext(Context context) {
		DatabaseConnection.context = context;
	}

	public DatabaseConnection() {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase sqLiteDatabase) {

		String sql = "CREATE TABLE bookList("
				+ "name VARCHAR(30) primary key,"
				+ "author VARCHAR(30),"
				+ "date date)";
		sqLiteDatabase.execSQL(sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public SQLiteDatabase getConnection() {
		return getWritableDatabase();
	}

	public void close(SQLiteDatabase db) {
		db.close();
	}

}
