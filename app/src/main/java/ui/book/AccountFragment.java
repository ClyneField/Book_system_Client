package ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;

public class AccountFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account, container, false);
        view.findViewById(R.id.sign_in).setOnClickListener(AccountFragment.this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(getContext(),MainActivity.class);
        startActivity(intent);
    }
}