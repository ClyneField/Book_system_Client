package admin.book;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import model.book.BookList;
import model.book.Book;
import model.book.Response;
import control.book.Controller;
import ui.book.R;

public class RetrieveActivity extends Activity{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_retrieve);

        new Thread() {
            public void run() {
                Controller control = new Controller(handler);
                control.retrieveBook();
            }
        }.start();
    }

    private Handler handler=new Handler(){

        public void handleMessage(Message message){
            super.handleMessage(message);
            Bundle bundle=message.getData();
            String result=bundle.getString("result");
            // ----- 初始化Response ----- //
            Response response = Response.getResponse();
            // ----- 获得Response当中的图书列表 ----- //
            BookList booklist = response.getBookList();

            CreateTable(booklist);
            Toast.makeText( RetrieveActivity.this, result, Toast.LENGTH_SHORT ).show();
        }
    };

    private void CreateTable(BookList bookList){

        TableLayout tableLayout = (TableLayout)findViewById(R.id.retrieve);

        for( int i=0; i<bookList.size(); i++ ){
            // ----- 获得图书列表的一本图书信息 ----- //
            Book book = bookList.get(i);

            String name = book.getName();
            String author = book.getAuthor();
            String date = book.getDate();

            TableRow tableRow = new TableRow(this);

            TextView r_name = new TextView(RetrieveActivity.this);
            TextView r_author =  new TextView(RetrieveActivity.this);
            TextView r_date =  new TextView(RetrieveActivity.this);

            r_name.setText(name);
            r_author.setText(author);
            r_date.setText(date);

            tableRow.addView(r_name);
            tableRow.addView(r_author);
            tableRow.addView(r_date);

            tableLayout.addView(tableRow);
        }
    }
}
