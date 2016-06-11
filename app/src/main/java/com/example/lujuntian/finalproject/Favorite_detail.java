package com.example.lujuntian.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


/**
 * Created by sky on 16/6/7.
 */

public class Favorite_detail extends Activity {
    private TextView item_title,item_price,yunfei,sell,loc,shop,detial;
    private ImageView main_pic;
    private Button buy;
    private ImageButton fav_pic;
    private static String url = "http://api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&type=jsonp&dataType=jsonp&data=%7B%22itemNumId%22%3A%22哈哈哈%22";
    private static String url_buy = "https://h5.m.taobao.com/awp/core/detail.htm?ft=t&id={0}";

    private String tex = "17397334024";
    private String htmlContent;
    private static final String TAG = "Favorites";
    private String name,shop_name,detail_m;
    private String title,price,img_url,deliPrice,sellCount,from;
    private int flag_fav;           //判断是否已经存放在收藏夹中
    private Handler handler,handler1;
    private Thread thread,thread1;
    private final int SUCCESS = 1;
    private Bitmap bitmap;
    private String img_pic;
    private String shop_nameGet="shopName\":\".*?,";      //店铺名称
    private String detailGet = "基本信息\".*?rate"; //基本信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        item_title = (TextView)findViewById(R.id.item_title);
        item_price = (TextView)findViewById(R.id.item_price);
        yunfei = (TextView)findViewById(R.id.deliver);
        sell = (TextView)findViewById(R.id.sellamount);
        loc = (TextView)findViewById(R.id.place);
        main_pic = (ImageView)findViewById(R.id.img_detail);
        fav_pic = (ImageButton) findViewById(R.id.favorite);
        buy = (Button)findViewById(R.id.buy);
        detial = (TextView)findViewById(R.id.detail);
        shop = (TextView)findViewById(R.id.shop_name);

        Intent intent = this.getIntent();
        String chg = intent.getStringExtra("id");
        img_pic = intent.getStringExtra("img_url");

        tex = chg;

        //检测本项目是否已经存在数据库中(是否已经被收藏)
        Item_fav_Service service = new Item_fav_Dao(Favorite_detail.this);
        String[] seleStrings = {tex+','};
        Map<String, String> map = service.viewItem(seleStrings);
        //dialog(map.toString());
        if (map.toString().equals("{}")){
            flag_fav = 0;
        }else {
            flag_fav = 1;
        }

       thread1=new Thread(new Runnable() {
            public void run(){
                try {       //获取网页json数据
                    String htmlContent1 = GetHtmlBody.getHtml(url,tex,1);
                    String imgGet="images\":.*?,";

                    Message msg = new Message();
                    msg.obj = htmlContent1;
                    msg.what = SUCCESS;
                    handler1.sendMessage(msg);

                    img_url = TestRegex.match(imgGet,htmlContent1);
                    img_url = img_url.replace("images\":[\"","");
                    img_url = img_url.replace("\",","");

                    byte[] data1 = ImageService.getImage(img_url);
                    Message msg1 = new Message();
                    msg1.obj = data1;
                    msg1.what = SUCCESS;
                    handler.sendMessage(msg1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();

        handler1 = new Handler(){
            public void handleMessage(Message msg) {
                htmlContent = msg.obj.toString();
                String titleGet="\"title\":\".*?,";
                String sellGet = "sellCount\":\".*?,";
                String priceGet="\"priceText\":\".*?,";
                String fromGet = "from.*?,";
                title = TestRegex.match(titleGet,htmlContent);
                String fastGet="快递.*?,";
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
                shop_name = shop_name.replace("shopName\":\"","");
                shop_name = shop_name.replace("\",","");
                detail_m = detail_m.replace("基本信息\":[{\"","");
                detail_m = detail_m.replace("\":\"",":");
                detail_m = detail_m.replace("\"},{\"",", ");
                detail_m = detail_m.replace("\"}]}]},\"rate","");
                from = from.replace("from\":\"","");
                from = from.replace("\",","");

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
                bitmap = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                main_pic.setImageBitmap(bitmap);
            }

        };


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
            public void onClick(View v) {//添加删除收藏数据操作

                if (flag_fav == 0){
                    Item_fav_Service service = new Item_fav_Dao(Favorite_detail.this);
                     //新增加一条记录
                    Object[] params = {tex+',',img_pic+',',title+',',price+','+sellCount+','};
                    boolean flag = service.addItem(params);

                    if(flag){
                        dialog("收藏成功");
                        flag_fav = 1;
                    }
                }else {
                    Item_fav_Service service = new Item_fav_Dao(Favorite_detail.this);
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

    public void onBackPressed(){
        Intent intent = new Intent(Favorite_detail.this, Favorite_list.class);
        startActivity(intent);
        finish();
    }



    public String dialog(String dialog) {       //提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(Favorite_detail.this);
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
