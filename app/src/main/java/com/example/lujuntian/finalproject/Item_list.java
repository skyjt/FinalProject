package com.example.lujuntian.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lujuntian.finalproject.Adapter.MyBaseAdapter;
import com.example.lujuntian.finalproject.blur.BlurBehind;
import com.example.lujuntian.finalproject.blur.OnBlurCompleteListener;
import com.example.lujuntian.finalproject.getHttpData.GetHtmlBody;
import com.example.lujuntian.finalproject.getHttpData.TestRegex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.CompoundButton.*;

/**
 * Created by sky on 16/5/30.
 *
 */

public class Item_list extends Activity {
    private int pageIndex = 0;   //保存当前页
    private Button btn_next,btn_previous;
    private ImageButton sch,goFav;
    private TextView page_number;
    private ListView listView;
    private String htmlContent;
    private static String url = "http://s.m.taobao.com/search?event_submit_do_new_search_auction=1&_input_charset=utf-8&searchfrom=1&action=home%3Aredirect_app_action&from=1&q=哈哈哈&sst=1&n=10&buying=buyitnow&m=api4h5&wlsort=10&page={2}";
    private String tex = "手机";
    private String id=null;
    private List<String> Item_title;
    private List<String> Item_price;
    private List<String> Item_pic_url;
    private List<String> Item_sold;
    private List<String> Item_id;
    private int page = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        page = pageReturn();



        Init();
        searchFunction();
        refreshPageNumber(pageIndex+1,page);
        previous_Page(page);
        nextPage(page);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Item_list.this, Item_detail.class);
                String putid = Item_id.get(position);
                String putimg = Item_pic_url.get(position);
                putid = putid.replace("item_id\":\"","");
                putid = putid.replace("\",","");
                putimg = putimg.replace("pic_path\":\"","");
                putimg = putimg.replace("\",","");
                intent.putExtra("id",putid);
                intent.putExtra("name",tex);
                intent.putExtra("img_url",putimg);
                startActivity(intent);

            }
        });
        goFav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Item_list.this, Favorite_list.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 注册组建初始化
     */
    public void Init(){
        btn_next = (Button)findViewById(R.id.next);
        btn_previous = (Button)findViewById(R.id.previous);
        sch = (ImageButton)findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.list);
        page_number = (TextView)findViewById(R.id.page_number);
        goFav = (ImageButton)findViewById(R.id.goFav);
        setAdp();

    }

    /**
     * 刷新页面底部page
     * @param page
     * @param sum
     */
    public void refreshPageNumber(int page,int sum){
        String a = Integer.toString(page);
        String b = Integer.toString(sum);
        page_number.setText(a+"/"+b);
    }
    /**
     * 初始化list数据
     */
    public void setAdp(){
        List<HashMap<String, Object>> data = getData(pageIndex);
        MyBaseAdapter adapter = new MyBaseAdapter(Item_list.this, data);
        listView.setAdapter(adapter);


    }

    /**
     * 获取总页数
     * @return
     */

    public int pageReturn(){
        int page = 0;
        String pageNumber;
        try{

            if (Build.VERSION.SDK_INT >= 11){
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
            }
            Intent intent = this.getIntent();
            tex = intent.getStringExtra("search_content");
            htmlContent = GetHtmlBody.getHtml(url,tex,1);
            String pagenum = "totalPage\":\".*?,";
            pageNumber = TestRegex.match(pagenum, htmlContent);
            String buf = pageNumber;
            buf = buf.replace("totalPage\":\"","");
            buf = buf.replace("\",","");
            page = Integer.valueOf(buf);

            return page;
        }catch (Exception e){
            //e.printStackTrace();
            return 0;
        }


    }

    /**
     * 获取需要现实的数据
     */
    public List<HashMap<String, Object>> getData(int page) {    //获取数据

        Intent intent = this.getIntent();
        tex = intent.getStringExtra("search_content");

        if (Build.VERSION.SDK_INT >= 11){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }

        try {
            htmlContent = GetHtmlBody.getHtml(url,tex,page+1);
            String pattern1="title\":\".*?,";
            String pattern2="price\":\".*?,";
            String pattern3="pic_path\":\".*?,";
            String pattern4="item_id\":\".*?,";
            String pattern5="sold\":\".*?,";
            Item_title= TestRegex.matchesToList(pattern1, htmlContent);
            Item_price= TestRegex.matchesToList(pattern2, htmlContent);
            Item_pic_url= TestRegex.matchesToList(pattern3, htmlContent);
            Item_id = TestRegex.matchesToList(pattern4,htmlContent);
            Item_sold = TestRegex.matchesToList(pattern5,htmlContent);

            List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < Item_id.size(); i++) {
                String out_title = Item_title.get(i);
                String out_price = Item_price.get(i);
                String out_pic = Item_pic_url.get(i);
                String out_sold = Item_sold.get(i);
                //id = Item_id.get(i);
                out_title = out_title.replace("title\":\"","");
                out_title = out_title.replace(",","");
                out_title = out_title.replace("\"","");
                out_price = out_price.replace("price\":\"","");
                out_price = out_price.replace(",","");
                out_price = out_price.replace("\"","");
                out_pic = out_pic.replace("pic_path\":\"","");
                out_pic = out_pic.replace("\",","");
                out_sold = out_sold.replace("sold\":\"","");
                out_sold = out_sold.replace("\",","");
                //System.out.println(out_pic);


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", out_pic);
                map.put("ItemTitle", out_title);
                map.put("ItemText", "价格:"+out_price);
                map.put("ItemSold", "销量:"+out_sold);
                data.add(map);

            }
            return data;

        }catch(Exception e){
            return null;
        }

    }

    /**
     * 搜索ImageView功能
     */
    public void searchFunction(){           //搜索按钮
        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlurBehind.getInstance().execute(Item_list.this, new OnBlurCompleteListener() {
                    @Override
                    public void onBlurComplete() {
                        Intent intent = new Intent(Item_list.this, Search.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);          //背景模糊特效~
                        finish();
                    }
                });
            }
        });
    }

    /**
     * 上一页功能
     */
    public void previous_Page(final int page){    //翻页
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex <= 0){
                    dialog("已经第一页了");
                }
                else{
                    refreshPageNumber(pageIndex,page);
                    pageIndex--;

                    setAdp();
                }

            }
        });
    }
    /**
     * 下一页按钮功能
     */
    public void nextPage(final int page){    //翻页
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex >= page-1){
                    dialog("已经最后一页了!");
                }
                else{
                    pageIndex++;
                    refreshPageNumber(pageIndex+1,page);
                    setAdp();
                }

            }
        });
    }


        /**
         * 最后一页提示框
         */
    public String dialog(String dialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Item_list.this);
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

