package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    EditText editText1;
    static ListView listView1;
    Button button5;
    public static ArrayList<String> al = new ArrayList<String>();
    public SQLiteDatabase database;
    public Cursor cursor;
    public String ACTIVITY_NAME = "Chat Activity";
    private String[] allItems = { ChatDatabaseHelper.KEY_ID,
            ChatDatabaseHelper.KEY_MESSAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        button5 = (Button) findViewById(R.id.button5);
        listView1 = (ListView) findViewById(R.id.chatView);
        editText1 = (EditText) findViewById(R.id.edit_text1);

        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        cursor = database.query(ChatDatabaseHelper.TABLE_CHAT,
                allItems, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: "+ cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: "+ cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE)));
            al.add(cursor.getString( cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();

        }
        cursor.close();

        final ChatAdapter messageAdapter = new ChatAdapter( this,al);
        listView1.setAdapter (messageAdapter);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                al.add( editText1.getText().toString());
                ContentValues insertValues = new ContentValues();
                insertValues.put(ChatDatabaseHelper.KEY_MESSAGE,editText1.getText().toString());
                database.insert(ChatDatabaseHelper.TABLE_CHAT, null, insertValues);
                messageAdapter.notifyDataSetChanged();
                editText1.setText("");
            }
        });


        AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MessageFragment fm = new MessageFragment();
                Bundle args = new Bundle();
                args.putInt("position", position);
                fm.setArguments(args);

                if (isTablet()|| getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.firstfragment, fm)
                            .addToBackStack(null)
                            .commit();
                }else{
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    startActivityForResult(intent, 10);
                }

            }
        };
        listView1.setOnItemClickListener(mListener);
    }

    private class ChatAdapter extends ArrayAdapter<String>
    {
          Context context;
          ArrayList<String> list;

          public ChatAdapter(Context context, ArrayList<String> list)
          {
               super(context, 0, list);
               this.context = context;
               this.list = list;
          }

          public int getCount()
          {
              return list.size();
          }

         public String getItem(int position)
         {
             return list.get(position);
         }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

        public long getItemId(int position)
        {
           cursor.moveToPosition(position);
            return Integer.parseInt(cursor.getString(0));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    public boolean isTablet() {
        try {
            // Compute screen size
            DisplayMetrics dm = getBaseContext().getResources().getDisplayMetrics();
            float screenWidth  = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            // Tablet devices should have a screen size greater than 6 inches
            return size >= 6;
        } catch(Throwable t) {

            return false;
        }
    }

}