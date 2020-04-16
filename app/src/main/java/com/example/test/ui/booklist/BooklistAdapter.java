package com.example.test.ui.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.MyContacts;
import com.example.test.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class BooklistAdapter extends ArrayAdapter {
    private final int resourceId;
    public BooklistAdapter(@NonNull Context context, int resource, @NonNull List<MyContacts> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyContacts bookItem= (MyContacts) getItem(position);
        View view;//实例化一个对象
        view= LayoutInflater.from(getContext()).inflate(resourceId,null);
//        RoundedImageView appImage=view.findViewById(R.id.booklist_head);
        TextView book_name=view.findViewById(R.id.book_item_name);
        TextView call_number=view.findViewById(R.id.call_number);
        if (bookItem!=null){
//            if (appItem.getAppIcon()!=null){
//                appImage.setImageDrawable(appItem.getAppIcon());//为图片视图设置图片资源
//            }
            if (bookItem.getName()!=null){
                book_name.setText(bookItem.getName());
            }
            call_number.setText(bookItem.getPhone());


        }


        return view;
    }
}
