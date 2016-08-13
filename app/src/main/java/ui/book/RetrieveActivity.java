package ui.book;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import model.book.BookList;
import model.book.Book;
import control.book.Controller;
import model.book.Response;

public class RetrieveActivity extends Activity{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.retrieve);
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
            String id = book.getDate();
            String author = book.getAuthor();

            TableRow tableRow = new TableRow(this);

            TextView r_name = new TextView(RetrieveActivity.this);
            TextView r_id =  new TextView(RetrieveActivity.this);
            TextView r_price =  new TextView(RetrieveActivity.this);

            r_name.setText(name);
            r_id.setText(id);
            r_price.setText(author);

            tableRow.addView(r_name);
            tableRow.addView(r_id);
            tableRow.addView(r_price);
            tableLayout.addView(tableRow);
        }
    }
}
