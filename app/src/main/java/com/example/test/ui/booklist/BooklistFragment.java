package com.example.test.ui.booklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.GetBooklist;
import com.example.test.MyContacts;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;



public class BooklistFragment extends Fragment {

    private BooklistViewModel booklistViewModel;
    private BooklistAdapter booklistAdapter;
    private List<MyContacts> booklistItems=new ArrayList<MyContacts>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                         ViewGroup container, Bundle savedInstanceState) {
        booklistViewModel =
                ViewModelProviders.of(this).get(BooklistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_booklist, container, false);
        final TextView textView = root.findViewById(R.id.text_booklist);

        booklistViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //获取toolbar高度
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        int toolbarheight=toolbar.getHeight();


        GetBooklist getBooklist=new GetBooklist();
        booklistItems= getBooklist.getAllContacts(getContext());
        booklistAdapter=new BooklistAdapter(getContext(),R.layout.booklist_item,booklistItems);
        ListView booklistview=root.findViewById(R.id.booklistview);
        booklistview.setAdapter(booklistAdapter);

        return root;
    }


}