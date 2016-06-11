package com.example.lujuntian.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.lujuntian.finalproject.blur.BlurBehind;

/**
 * Created by sky on 16/5/28.
 */
public class Search extends Activity {
    private EditText searchView;
    private String search_content;
    private ImageButton imageButton,star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurred);
        BlurBehind.getInstance()    //在你需要添加模糊或者透明的背景中只需要设置这几行简单的代码就可以了
                .withAlpha(50)
                .withFilterColor(Color.parseColor("#ffffff"))
                .setBackground(this);
        searchView = (EditText) findViewById(R.id.search_k);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        star = (ImageButton)findViewById(R.id.starbutton);

        findViewById(R.id.background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(searchView.getText().toString().trim())) {
                    dialog("请输入内容!");

                }
                else {
                    Intent intent = new Intent(Search.this, Item_list.class);
                    try{
                        search_content = searchView.getText().toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    intent.putExtra("search_content",search_content);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, Favorite_list.class);
                startActivity(intent);
            }
        });

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if ("".equals(searchView.getText().toString().trim())) {
                        dialog("请输入内容!");

                    }
                    else {
                        Intent intent = new Intent(Search.this, Item_list.class);
                        try{
                            search_content = searchView.getText().toString();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        intent.putExtra("search_content",search_content);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }
                else{
                    return false;
                }
            }
        });


        /*searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ("".equals(searchView.getText().toString().trim())) {
                    dialog("请输入内容!");
                    return false;

                }
                else {
                    Intent intent = new Intent(Search.this, Client_List.class);
                    try{
                        search_content = searchView.getText().toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    intent.putExtra("search_content",search_content);
                    startActivity(intent);
                    return true;
                }

            }


        });*/

    }
    public String dialog(String dialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
        builder.setMessage(dialog);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return dialog;
    }
}
