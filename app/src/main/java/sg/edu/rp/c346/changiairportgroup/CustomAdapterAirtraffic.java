package sg.edu.rp.c346.changiairportgroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 15056215 on 20/6/2017.
 */

public class CustomAdapterAirtraffic extends ArrayAdapter<Plane> {


    private Context context;
    private ArrayList<Plane> plans;
    private TextView tvLicensePlate;
    private TextView tvAirline;
    private TextView tvTiming;
    private TextView tvFlyToWhere;
    private  TextView tvFlightnum;
    private ImageView ivColour;

    public CustomAdapterAirtraffic(Context context, int resource, ArrayList<Plane> objects) {
        super(context, resource, objects);
        // Store the food that is passed to this adapter
        plans = objects;
        // Store Context object as we would need to use it later
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);

        tvLicensePlate = (TextView) rowView.findViewById(R.id.tvLicensePlate);
        tvAirline = (TextView) rowView.findViewById(R.id.tvAirline);
        tvTiming = (TextView) rowView.findViewById(R.id.tvTiming);
        tvFlyToWhere = (TextView) rowView.findViewById(R.id.tvFlyToWhere);
        tvFlightnum = (TextView) rowView.findViewById(R.id.tvFlightNum);



        ivColour = (ImageView) rowView.findViewById(R.id.ivColour);
        Plane currentPlane = plans.get(position);



        tvTiming.setText(currentPlane.getTiming());
        tvLicensePlate.setText(currentPlane.getLicensePlate());
        tvAirline.setText(currentPlane.getAirline());
        tvFlyToWhere.setText(currentPlane.getToWhere());
        tvFlightnum.setText(currentPlane.getFlightNum());

//        String img = currentHolidays.getimage();
//        int holiday = this.context.getResources().getIdentifier(img, "drawable", context.getPackageName());
//        ivimage.setImageResource(holiday);


        return rowView;
    }

}
