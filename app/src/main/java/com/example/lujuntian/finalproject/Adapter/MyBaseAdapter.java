package com.example.lujuntian.finalproject.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lujuntian.finalproject.R;
import com.example.lujuntian.finalproject.getHttpData.ImageService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sky on 16/6/6.
 */

public class MyBaseAdapter extends BaseAdapter{
    private int[] colors = new int[] { 0xffffffff};
    private Context mContext;
    private List<HashMap<String, Object>> dataList;
    //private byte[] data1 = new byte[200];
    private Bitmap bitmap;
    private ViewHolder holder = null;
    public MyBaseAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return dataList.size();

    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public android.view.View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview, null);
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.tv1);
            holder.text = (TextView) convertView.findViewById(R.id.tv2);
            holder.sold = (TextView) convertView.findViewById(R.id.sold);
            // 将holder绑定到convertView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 向ViewHolder中填入的数据
        try {
            byte[] data1 = ImageService.getImage((String)getItem(position).get("ItemImage"));
            bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.image.setImageBitmap(bitmap);
        holder.title.setText((String) getItem(position).get("ItemTitle"));
        holder.text.setText((String) getItem(position).get("ItemText"));
        holder.sold.setText((String) getItem(position).get("ItemSold"));
        //holder.image.setImageResource((Integer) getItem(position).get("ItemImage"));

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }
    final class ViewHolder {
        ImageView image;
        TextView title;
        TextView text;
        TextView sold;
    }
}
