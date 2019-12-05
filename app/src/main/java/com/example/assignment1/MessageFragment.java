package com.example.assignment1;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import static com.example.assignment1.ChatWindow.al;


public class MessageFragment extends Fragment {


    int position = 0;
    TextView message;
    TextView id;
    Button delete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            // Get back arguments
            if(getArguments() != null) {
                position = getArguments().getInt("position", 0);
            }
        }
    }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layoutforfragment, parent, false);
        }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set values for view here
        message = (TextView) view.findViewById(R.id.textView8);
        id = (TextView) view.findViewById(R.id.textView9);
        delete = (Button) view.findViewById(R.id.button7);
        message.setText(al.get(position));
        id.setText(position);
    }

}


