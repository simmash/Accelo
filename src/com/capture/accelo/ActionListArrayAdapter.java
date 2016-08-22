package com.capture.accelo;

import java.util.List;
import com.capture.accelo.R;
import com.capture.accelo.ActionListData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionListArrayAdapter extends ArrayAdapter<ActionListData>
{
	private final Context context;
	private final List<ActionListData> values;
	public ViewHolder tag;
	
	public ActionListArrayAdapter(Context context, List<ActionListData> values) 
	{
		super(context, R.layout.activity_accel_main, values);
		this.context = context;
		this.values = values;
	}
	@Override
	public int getViewTypeCount() {
		return values.size();
	}

	@Override
	public int getItemViewType(int position) {
		return position % values.size();
	}

	public static class ViewHolder {
		public TextView actionname;
		public TextView delay;
		public TextView freq;
		public ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View rowView = null;

		if (convertView != null) {
			rowView = convertView;
		} else {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.activity_accel_main, parent, false);

			TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
			TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
			TextView textView3 = (TextView) rowView.findViewById(R.id.thirdLine);
			//ImageView imageView = (ImageView) rowView.findViewById(R.id.buttonsq);
			ViewHolder holder = new ViewHolder();
			holder.actionname = textView1;
			holder.delay = textView2;
			holder.freq = textView3;
			//holder.img = imageView;
			rowView.setTag(holder);

		}
		tag = (ViewHolder) rowView.getTag();
				
		tag.actionname.setText(values.get(position).getActionName());
		tag.delay.setText(values.get(position).getDelay());
		tag.freq.setText(values.get(position).getFreq());
		rowView.setBackgroundColor(Color.BLACK);
		return rowView;
	}
	
}
