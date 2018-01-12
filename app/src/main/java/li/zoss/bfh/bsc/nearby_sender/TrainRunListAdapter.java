package li.zoss.bfh.bsc.nearby_sender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrainRunListAdapter extends ArrayAdapter<Station> implements View.OnClickListener{

    private ArrayList<Station> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtStation;
        ImageView imgRequest;
        ImageView info;
    }

    public TrainRunListAdapter(ArrayList<Station> data, Context context) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Station curStation=(Station)object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Station dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.txtStation = convertView.findViewById(R.id.name);
            viewHolder.info = convertView.findViewById(R.id.imgInfo);
            viewHolder.imgRequest = convertView.findViewById(R.id.imgRequest);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        lastPosition = position;

        viewHolder.txtStation.setText(dataModel.getStationName());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}