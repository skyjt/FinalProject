package com.example.lujuntian.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lujuntian.finalproject.DataBaseOperation.Item_fav_Dao;
import com.example.lujuntian.finalproject.DataBaseOperation.Item_fav_Service;
import com.example.lujuntian.finalproject.getHttpData.GetHtmlBody;
import com.example.lujuntian.finalproject.getHttpData.ImageService;
import com.example.lujuntian.finalproject.getHttpData.TestRegex;

import java.util.Map;

import static android.graphics.BitmapFactory.decodeByteArray;


/**
 * Created by sky on 16/6/7.
 */

public class Item_detail extends Activity {
    private TextView item_title,item_price,yunfei,sell,loc,shop,detial;
    private ImageView main_pic;
    private Button buy;
    private ImageButton fav_pic;
    private static String url = "http://api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&type=jsonp&dataType=jsonp&data=%7B%22itemNumId%22%3A%22哈哈哈%22";
    private static String url_buy = "https://h5.m.taobao.com/awp/core/detail.htm?ft=t&id={0}";
    private String tex = "17397334024";

    private String htmlContent,html;
    private static final String TAG = "Favorites";
    private String name;
    private String title,price,img_url,deliPrice,sellCount,from,shop_name,detail_m;
    private int flag_fav;           //判断是否已经存放在收藏夹中
    private String titleGet="\"title\":\".*?,";     //标题
    private String sellGet = "sellCount\":\".*?,";  //销量
    private String priceGet="\"priceText\":\".*?,"; //价格
    private String fromGet = "from.*?,";            //发货地
    private String opriceGet = "originalPrice\":\".*?,";
    private String imgGet="images\":.*?,";          //图片
    private String fastGet="快递.*?,";              // 快递
    private String shop_nameGet="shopName\":\".*?,";      //店铺名称
    private String detailGet = "基本信息\".*?rate"; //基本信息
    private Handler handler,handler1;
    private final int SUCCESS = 1;
    private Bitmap bitmap;
    private String chg,get_img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        item_title = (TextView)findViewById(R.id.item_title);
        item_price = (TextView)findViewById(R.id.item_price);
        detial = (TextView)findViewById(R.id.detail);
        shop = (TextView)findViewById(R.id.shop_name);
        yunfei = (TextView)findViewById(R.id.deliver);
        sell = (TextView)findViewById(R.id.sellamount);
        loc = (TextView)findViewById(R.id.place);
        main_pic = (ImageView)findViewById(R.id.img_detail);
        fav_pic = (ImageButton) findViewById(R.id.favorite);
        buy = (Button)findViewById(R.id.buy);
        Intent intent = this.getIntent();
        name = intent.getStringExtra("name");
        chg = intent.getStringExtra("id");
        get_img_url = intent.getStringExtra("img_url");
        tex = chg;

        //检测本项目是否已经存在数据库中(是否已经被收藏)
        Item_fav_Service service = new Item_fav_Dao(Item_detail.this);
        String[] seleStrings = {tex+','};
        Map<String, String> map = service.viewItem(seleStrings);
        //dialog(map.toString());
        if (map.toString().equals("{}")){
            flag_fav = 0;
        }else {
            flag_fav = 1;
        }


        //强制主线程执行
        if (Build.VERSION.SDK_INT >= 11){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads  ().detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }


        try {       //获取网页json数据
            htmlContent = GetHtmlBody.getHtml(url,tex,1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //提取获取的数据中有用信息



        //线程获取图片防止卡顿
        new Thread(){
            public void run(){
                try {
                    htmlContent = GetHtmlBody.getHtml(url,tex,1);
                    Message msg1 = new Message();
                    msg1.obj = htmlContent;
                    handler1.sendMessage(msg1);

                    img_url = TestRegex.match(imgGet,htmlContent);
                    img_url = img_url.replace("images\":[\"","");
                    img_url = img_url.replace("\",","");

                    byte[] data1 = ImageService.getImage(img_url);
                    Message msg = new Message();
                    msg.obj = data1;
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        handler1 = new Handler(){
            public void handleMessage(Message msg){
                htmlContent = msg.obj.toString();
                title = TestRegex.match(titleGet,htmlContent);
                shop_name = TestRegex.match(shop_nameGet,htmlContent);
                detail_m = TestRegex.match(detailGet,htmlContent);

                htmlContent = htmlContent.replace("\\","");
                deliPrice = TestRegex.match(fastGet,htmlContent);
                price = TestRegex.match(priceGet,htmlContent);
                sellCount = TestRegex.match(sellGet,htmlContent);
                from = TestRegex.match(fromGet,htmlContent);
                title = title.replace("\"title\":\"","");
                title = title.replace("\",","");
                title = title.replace("\"},","");
                price = price.replace("\"priceText\":\"","");
                price = price.replace("\",","");
                deliPrice = deliPrice.replace("\",","");
                sellCount = sellCount.replace("sellCount\":\"","");
                sellCount = sellCount.replace("\",","");
                sellCount = sellCount.replace("\"},","");

                from = from.replace("from\":\"","");
                from = from.replace("\",","");


                shop_name = shop_name.replace("shopName\":\"","");
                shop_name = shop_name.replace("\",","");
                detail_m = detail_m.replace("基本信息\":[{\"","");
                detail_m = detail_m.replace("\":\"",":");
                detail_m = detail_m.replace("\"},{\"",", ");
                detail_m = detail_m.replace("\"}]}]},\"rate","");

                yunfei.setText(deliPrice);
                item_title.setText(title);
                item_price.setText(price+"元");
                sell.setText("销量:"+sellCount);
                loc.setText("地点:"+from);
                shop.setText("店铺:"+shop_name);
                detial.setText("基本信息:"+detail_m);
            }
        };

        handler = new Handler(){
            public void handleMessage(Message msg) {

                byte[] data2 = (byte[]) msg.obj;
                if(data2 == null){
                    dialog("无图片数据");
                }
                else{
                    bitmap = decodeByteArray(data2, 0, data2.length);
                    main_pic.setImageBitmap(bitmap);
                }
            }

        };


        //购买按钮跳转
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ul = url_buy;
                ul = ul.replace("{0}",tex);
                Uri uri = Uri.parse(ul);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //收藏按钮点击事件
        fav_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //添加删除收藏数据操作

                if (flag_fav == 0){              //如果数据库中没有这条数据,便执行添加
                    Item_fav_Service service = new Item_fav_Dao(Item_detail.this);
                     //新增加一条记录
                    Object[] params = {tex+',',get_img_url+',',title+',',price+','+sellCount+','};
                    boolean flag = service.addItem(params);
                    if(flag){                       //若返回true
                        dialog("收藏成功");
                        flag_fav = 1;
                    }
                }else {
                    Item_fav_Service service = new Item_fav_Dao(Item_detail.this);
                    Object[] params = {tex+','};
                    boolean flag = service.deleteItem(params);
                    //删除一条记录
                    if(flag){
                        dialog("取消成功");
                        flag_fav = 0;
                    }
                }

            }
        });

    }


    public String dialog(String dialog) {       //提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(Item_detail.this);
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
