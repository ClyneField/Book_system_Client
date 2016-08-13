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

import control.book.Controller;

public class BookshelfAdapter extends SimpleAdapter{

    int resourceId;
    Button deleteBook;
    Context context;
    TextView name,author,date;

    public BookshelfAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context,data,resource,from,to);
        resourceId = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        name = (TextView) view.findViewById(R.id.bookshelfTitle);
        author = (TextView) view.findViewById(R.id.bookshelfAuthor);
        date = (TextView) view.findViewById(R.id.bookshelfDate);

        deleteBook = (Button) view.findViewById(R.id.deleteFromBookshelf);
        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller controller = new Controller();
                if (controller.deleteFromDatabase(name.getText().toString(), author.getText().toString(), date.getText().toString()))
                    Log.d("BookshelfAdapter", "Success: delete from bookshelf");
            }
        });
        return view;
    }
}