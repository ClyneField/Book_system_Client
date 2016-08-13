package model.book;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import util.book.DatabaseConnection;

public class BookList extends ArrayList<Book> {

    private static final long serialVersionUID = 1L;
    private static BookList booklist = null;

    public BookList(){

    }

    /**
     * 类名：BookList
     * 方法名：getBookList
     * 作用：操作数据库，获得数据库中的图书列表
     * @return
     */
    public  static BookList getBookList() {

        if ( booklist == null ) {

            booklist = new BookList();

            DatabaseConnection connection = new DatabaseConnection();
            SQLiteDatabase db = connection.getConnection();

            Cursor cur = db.query("bookList", null, null, null, null, null, null);
            while (cur.moveToNext()) {
                int nameNum = cur.getColumnIndex("name");
                int authorNum = cur.getColumnIndex("author");
                int dateNum = cur.getColumnIndex("date");
                String name = cur.getString(nameNum);
                String author = cur.getString(authorNum);
                String date = cur.getString(dateNum);
                Book book = new Book(name, author, date);
                booklist.add(book);
                cur.moveToNext();
            }
            cur.close();
            connection.close(db);
        }
        return booklist;
    }

    /**
     * 类名：BookList
     * 方法名：insert
     * 作用：将图书信息插入数据库
     * @param book
     * @return
     */
    public boolean insert(Book book) {

        if ( checkName(book.getName()) ) {
            return false;
        } else {
            booklist.add(book);
            String name = book.getName();
            String author = book.getAuthor();
            String date = book.getDate();
            DatabaseConnection connection = new DatabaseConnection();
            SQLiteDatabase db = connection.getConnection();
            String sql = "INSERT INTO bookList(name,author,date)VALUES(\""+name+"\",\""+author+"\",'"+date+"')";
            db.execSQL(sql);
            db.close();
            return true;
        }
    }

    public  boolean delete(Book book) {

        if ( checkName(book.getName())) {
            booklist.delete(book);
            DatabaseConnection connection = new DatabaseConnection();
            SQLiteDatabase db = connection.getConnection();
            String sql = "DELETE FROM bookList WHERE name=\"" + book.getName() + "\"";
            db.execSQL(sql);
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public  boolean checkName(String name) {

        for (int i = 0; i < booklist.size(); ++i) {
            Book book = booklist.get(i);
            if (book.getName().equals(name))
                return true;
        }
        return false;
    }
}