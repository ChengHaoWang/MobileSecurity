package com.example.test.ui.call_log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.CallLogInfo;
import com.example.test.ContactsMsgUtils;
import com.example.test.R;


import java.util.ArrayList;
import java.util.List;

public class CallLogFragment extends Fragment {
    private CallLogViewModel calllogViewModel;
    private CallLogAdapter calllogAdapter;
    private List<CallLogInfo> calllogItems=new ArrayList<CallLogInfo>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calllogViewModel =
                ViewModelProviders.of(this).get(CallLogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calllog, container, false);
        final TextView textView = root.findViewById(R.id.text_calllog);

        calllogViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //获取toolbar高度
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        int toolbarheight=toolbar.getHeight();
        LinearLayout background=root.findViewById(R.id.notification_background);


        ContactsMsgUtils getCalllog=new ContactsMsgUtils();
        calllogItems= getCalllog.getCallLog(getContext());
        calllogAdapter=new CallLogAdapter(getContext(),R.layout.calllog_item,calllogItems);
        ListView callloglistview=root.findViewById(R.id.calllogview);
        callloglistview.setAdapter(calllogAdapter);

        return root;
    }
}
