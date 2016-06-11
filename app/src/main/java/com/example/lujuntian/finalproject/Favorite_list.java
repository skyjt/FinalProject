package com.example.lujuntian.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lujuntian.finalproject.Adapter.MyBaseAdapter;
import com.example.lujuntian.finalproject.DataBaseOperation.Item_fav_Dao;
import com.example.lujuntian.finalproject.DataBaseOperation.Item_fav_Service;
import com.example.lujuntian.finalproject.getHttpData.TestRegex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sky on 16/6/8.
 */

public class Favorite_list extends Activity {
    private ListView listView;
    private TextView page_number;
    private Button btn_next,btn_previous,deleteall;
    private static String url = "http://api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&type=jsonp&dataType=jsonp&data=%7B%22itemNumId%22%3A%22哈哈哈%22";
    private String htmlContent;
    private int noo,page1,page_now = 1;
    private int sum =10;
    private String tex ,f_id,img_url;
    private List<String> Item_id,Item_pic,Item_title,Item_price,Item_sold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fav);
        init();

        //dialog(ans);
        //适配器载入
        adp();


        //list项目点击事件
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Favorite_list.this, Favorite_detail.class);
                String puton = Item_id.get(position);
                String putpic = Item_pic.get(position);
                putpic = putpic.replace("item_pic=","");
                putpic = putpic.replace(",","");
                puton = puton.replace("item_id=","");
                puton = puton.replace(",","");
                intent.putExtra("id",puton);
                intent.putExtra("img_url",putpic);
                //intent.putExtra("name",tex);
                startActivity(intent);

            }
        });
        previous_Page();
        nextPage();
        clear();
    }
    public void init(){
        btn_next = (Button)findViewById(R.id.next);
        btn_previous = (Button)findViewById(R.id.previous);
        deleteall = (Button)findViewById(R.id.deleteAll);

        listView = (ListView) findViewById(R.id.list);
        page_number = (TextView)findViewById(R.id.page_number);

        Item_fav_Service service = new Item_fav_Dao(Favorite_list.this);
        List<Map<String, String>> list = service.listItemMaps(null);
        Log.i("1", "---查询所有记录--->> " + list.toString());

        String ans = list.toString();
        String pattern1 = "item_id.*?,";
        String pattern2 = "item_pic.*?,";
        String pattern3 = "item_title.*?,";
        String pattern4 = "item_price.*?,";
        String pattern5 = "item_sold.*?,";
        Item_id = TestRegex.matchesToList(pattern1, ans);
        Item_pic = TestRegex.matchesToList(pattern2, ans);
        Item_title = TestRegex.matchesToList(pattern3, ans);
        Item_price = TestRegex.matchesToList(pattern4, ans);
        Item_sold = TestRegex.matchesToList(pattern5, ans);
        noo = Item_id.size();
        page1 = noo/sum+1;
        refreshPageNumber(page_now,page1);
    }

    public void adp(){
        List<HashMap<String, Object>> data = getData(0);
        MyBaseAdapter adapter = new MyBaseAdapter(Favorite_list.this, data);
        listView.setAdapter(adapter);
    }

    public void refreshPageNumber(int page,int sum){
        String a = Integer.toString(page);
        String b = Integer.toString(sum);
        page_number.setText(a+"/"+b);
    }

    /**
     * 清除收藏夹
     */
    public void clear(){
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2("警告:此操作不可恢复");

            }
        });
    }

    /**
     * 上一页功能
     */
    public void previous_Page(){    //翻页
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page_now <= 1){
                    dialog("已经第一页了");
                }
                else{
                    refreshPageNumber(page_now,page1);
                    page_now--;
                    adp();
                }

            }
        });
    }
    /**
     * 下一页按钮功能
     */
    public void nextPage(){    //翻页
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page_now >= page1){
                    dialog("已经最后一页了!");
                }
                else{
                    page_now++;
                    refreshPageNumber(page_now,page1);
                    adp();
                }

            }
        });
    }



    public List<HashMap<String, Object>> getData(int page) {
        try {
            if (Build.VERSION.SDK_INT >= 11){
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
            }
            List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
            //获取数据库所有记录
            if(sum > noo || noo-(sum*(page_now-1))<sum) {
                for (int i = (page_now-1)*10 ; i < noo; i++) {
                    //String id = Item_id.get(i);
                    String title = Item_title.get(i);
                    String price = Item_price.get(i);
                    String pic = Item_pic.get(i);
                    //String sold = Item_sold.get(i);
                    //id = id.replace("item_id=", "");
                    //id = id.replace(",", "");
                    title = title.replace("item_title=", "");
                    title = title.replace(",", "");
                    price = price.replace("item_price=", "");
                    price = price.replace(",", "");
                    pic = pic.replace("item_pic=", "");
                    pic = pic.replace(",", "");
                    //sold = sold.replace("item_sold", "");
                    //sold = sold.replace(",", "");

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemImage", pic);
                    map.put("ItemTitle", title);
                    map.put("ItemText", price + "元");
                    map.put("ItemSold", "");
                    data.add(map);

                }
            }else {
                for (int i = (page_now-1)*10; i < sum*page_now; i++) {
                    //String id = Item_id.get(i);
                    String title = Item_title.get(i);
                    String price = Item_price.get(i);
                    String pic = Item_pic.get(i);
                    //String sold = Item_sold.get(i);
                    //id = id.replace("item_id=", "");
                    //id = id.replace(",", "");
                    title = title.replace("item_title=", "");
                    title = title.replace(",", "");
                    price = price.replace("item_price=", "");
                    price = price.replace(",", "");
                    pic = pic.replace("item_pic=", "");
                    pic = pic.replace(",", "");
                    //sold = sold.replace("item_sold", "");
                    //sold = sold.replace(",", "");

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemImage", pic);
                    map.put("ItemTitle", title);
                    map.put("ItemText", price + "元");
                    map.put("ItemSold", "");
                    data.add(map);
                }
            }
            return data;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //public void onBackPressed(){
    //    Intent intent = new Intent(Fav_list.this, Search.class);
    //    //intent.putExtra("search_content",name);
    ///    startActivity(intent);
     //   finish();
     //   return;
    //}


    public String dialog2(String dialog) {       //提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(Favorite_list.this);
        builder.setMessage(dialog);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Item_fav_Service service = new Item_fav_Dao(Favorite_list.this);
                boolean flag = service.deleteAll();
                if(flag){
                    dialog("删除成功");
                    init();
                    adp();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return dialog;
    }



    public String dialog(String dialog) {       //提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(Favorite_list.this);
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
