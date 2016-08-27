package ui.book;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import control.book.Controller;

public class BookshelfAdapter extends SimpleAdapter{

    int resourceId;
    Context context;
    List<? extends Map<String, ?>> book_map;

    public BookshelfAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context,data,resource,from,to);
        this.resourceId = resource;
        this.context = context;
        this.book_map = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("BookshelfAdapter", "getView: ");
        View view;
        ViewHolder viewHolder;

        final String bookName = book_map.get(position).get("name").toString();
        final String bookAuthor = book_map.get(position).get("author").toString();
        final String bookDate = book_map.get(position).get("date").toString();

        Log.d("BookshelfAdapter", "getView: "+bookName);

        if ( convertView != null ) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        else {
            view = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.bookshelfTitle);
            viewHolder.author = (TextView) view.findViewById(R.id.bookshelfAuthor);
            viewHolder.date = (TextView) view.findViewById(R.id.bookshelfDate);
            viewHolder.deleteBook = (Button) view.findViewById(R.id.deleteFromBookshelf);
            view.setTag(viewHolder);
        }
        viewHolder.deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    public void run() {
                        Controller controller = new Controller();
                        if (controller.deleteFromDatabase(bookName, bookAuthor, bookDate)) {
                            Intent intent = new Intent("BookshelfAdapterSuccess");
                            intent.putExtra("name", bookName);
                            context.sendBroadcast(intent);
                            Log.d("BookshelfAdapter", "Success: delete from bookshelf:" + bookName);
                        } else
                            Log.d("BookshelfAdapter", "Failed: delete from bookshelf:" + bookName);
                    }
                }.start();
            }
        });

        viewHolder.name.setText(bookName);
        viewHolder.author.setText(bookAuthor);
        viewHolder.date.setText(bookDate);

        return view;
    }

    public class ViewHolder {
        Button deleteBook;
        TextView name,author,date;
    }
}