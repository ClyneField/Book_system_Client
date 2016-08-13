package ui.book;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import control.book.Controller;
import model.book.Book;

public class BookMessageAdapter extends SimpleAdapter{

    int resourceId;
    Button addBook;
    Context context;
    TextView name,author,date;

    public BookMessageAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context,data,resource,from,to);
        resourceId = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        name = (TextView) view.findViewById(R.id.bookTitle);
        author = (TextView) view.findViewById(R.id.bookAuthor);
        date = (TextView) view.findViewById(R.id.bookDate);

        addBook = (Button) view.findViewById(R.id.addToBookshelf);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    public void run() {
                        Controller controller = new Controller();
                        if (controller.addToDatabase(name.getText().toString(), author.getText().toString(), date.getText().toString()))
                            Log.d("BookMessageAdapter", "Success: add to bookshelf");
                    }
                }.start();
            }
        });
        return view;
    }
}
