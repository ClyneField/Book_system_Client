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

public class TouristAdapter extends SimpleAdapter{

    int resourceId;
    Context context;
    List<? extends Map<String, ?>> book_map;

    public TouristAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context,data,resource,from,to);
        this.resourceId = resource;
        this.context = context;
        this.book_map = data;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {

        Log.d("TouristAdapter", "getView: ");
        View view;
        ViewHolder viewHolder;

        final String name = book_map.get(position).get("name").toString();
        final String author = book_map.get(position).get("author").toString();
        final String date = book_map.get(position).get("date").toString();

        if ( convertView != null ) {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        else {
            view = LayoutInflater.from(context).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.bookTitle);
            viewHolder.author = (TextView) view.findViewById(R.id.bookAuthor);
            viewHolder.date = (TextView) view.findViewById(R.id.bookDate);
            viewHolder.addBook = (Button) view.findViewById(R.id.addToBookshelf);
            view.setTag(viewHolder);
        }

        viewHolder.addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    public void run() {
                        Controller controller = new Controller();
                        if (controller.addToDatabase( name, author, date )) {
                            Intent intent = new Intent("BookMessageAdapterSuccess");
                            context.sendBroadcast(intent);
                            Log.d("TouristAdapter", "Success: add to bookshelf:" + name);
                        }
                        else {
                            Intent intent = new Intent("BookMessageAdapterFailed");
                            context.sendBroadcast(intent);
                            Log.d("TouristAdapter", "Failed: add to bookshelf:" + name);
                        }
                    }
                }.start();
            }
        });

        viewHolder.name.setText(name);
        viewHolder.author.setText(author);
        viewHolder.date.setText(date);

        return view;
    }

    public class ViewHolder {
        Button addBook;
        TextView name,author,date;
    }
}
