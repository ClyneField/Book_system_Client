package ui.book;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;

public class BookshelfFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup listView = (ViewGroup) inflater.inflate(R.layout.bookshelf, container, false);
        return listView;
    }
}