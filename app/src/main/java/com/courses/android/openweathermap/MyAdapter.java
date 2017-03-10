package com.courses.android.openweathermap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asus on 07.03.2016.
 */
public class MyAdapter extends BaseAdapter{

    public static final String TAG = MyAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Weather> mData;
    private LayoutInflater mLayoutInflater;
    //private ImageView imgView;

    public MyAdapter(Context _context, ArrayList<Weather> _data) {
        mContext = _context;
        mData = _data;
        mLayoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        Log.d(TAG, "getView: " + _position);
        View itemView = _convertView;
        ViewHolder viewHolder;
        if (itemView == null) {
            Log.d(TAG, "Create view: " + _position);
           // itemView = mLayoutInflater.inflate(R.layout.list_item, null);

            viewHolder = new ViewHolder();

            viewHolder.cityText = (TextView) itemView.findViewById(R.id.cityText);
            viewHolder.condDescr = (TextView) itemView.findViewById(R.id.condDescr);
            viewHolder.temp = (TextView) itemView.findViewById(R.id.temp);
            viewHolder.hum = (TextView) itemView.findViewById(R.id.hum);
            viewHolder.press = (TextView)itemView.findViewById(R.id.press);
            viewHolder.windSpeed = (TextView) itemView.findViewById(R.id.windSpeed);
            viewHolder.windDeg = (TextView) itemView.findViewById(R.id.windDeg);
            viewHolder.imgView = (ImageView) itemView.findViewById(R.id.condIcon);





          //  viewHolder.tvPhone = (TextView) itemView.findViewById(R.id.tvContactPhone);
        //    viewHolder.image = (ImageView) itemView.findViewById(R.id.pic);

            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        Weather weather = mData.get(_position);
        viewHolder.cityText.setText(weather.location.getCity() +"," + weather.location.getCountry());
        viewHolder.condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
        viewHolder.temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
        viewHolder.hum.setText("" + weather.currentCondition.getHumidity() + "%");
        viewHolder.press.setText("" + weather.currentCondition.getPressure() + " hPa");
        viewHolder.windSpeed.setText("" + weather.wind.getSpeed() + " mps");
        viewHolder.windDeg.setText("" + weather.wind.getDeg() + "�");
        Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
        viewHolder.imgView.setImageBitmap(img);
        return itemView;

    }
    public class ViewHolder {
        private TextView cityText;
        private TextView condDescr;
        private TextView temp;
        private TextView press;
        private TextView windSpeed;
        private TextView windDeg;

        private TextView hum;
        private ImageView imgView;
    }
}
