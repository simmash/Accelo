package com.capture.accelo;

import java.util.ArrayList;
import java.util.List;
import com.capture.accelo.ActionListArrayAdapter;
import com.capture.accelo.ActionListData;
import com.capture.accelo.AcceloPersistant;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AccelMainActivity extends ListActivity
{
	private static AcceloPersistant mPersistance = null;
	private List<ActionListData> list = null;
	ProgressDialog progressDialog = null;
	ActionListArrayAdapter adapter = null;
	int selected = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		mPersistance = new AcceloPersistant(getApplicationContext());	
		mPersistance.getStringValue(mPersistance.userIdentKey);		
		
		list = getAllAction(getApplicationContext());

		adapter = new ActionListArrayAdapter(this, list);
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
		    @Override
		    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int row, long arg3) 
		    {
		    	ActionListData itemtoReset = (ActionListData) getListAdapter().getItem(row);
		    	Toast.makeText(getApplicationContext(), "Implement long click :"+itemtoReset.getActionName(), Toast.LENGTH_LONG).show();
		    	return true;
		    }
		});
	}

	private ActionListData get(String id, String actionname, String delay, String freq, boolean selected) 
	{
		return new ActionListData(id, actionname, delay, freq, selected);
	}
	
	private List<ActionListData> getAllAction(Context context)
	{
		List<ActionListData> listnew = new ArrayList<ActionListData>();
		listnew.add(get("1", "Dinner", "25", "200", false));
		listnew.add(get("2", "Walking", "10", "200", false));
		listnew.add(get("3", "Running", "15", "200", false));
		listnew.add(get("4", "Sleeping", "30", "200", false));
		listnew.add(get("5", "Dinking", "10", "200", false));
		listnew.add(get("6", "Driving", "10", "200", false));
		return listnew;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		ActionListData itemtoReset = (ActionListData) getListAdapter().getItem(position);
		
		Intent myIntent = new Intent(AccelMainActivity.this, AcceloTimer.class);
		myIntent.putExtra(AcceloDefines.ID_ACTION_NAME, itemtoReset.getActionName()); 
		myIntent.putExtra(AcceloDefines.ID_DELAY, itemtoReset.getDelay()); 
		myIntent.putExtra(AcceloDefines.ID_FREQ, itemtoReset.getFreq()); 
		AccelMainActivity.this.startActivity(myIntent);
    }
	
	/*
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
	{
		ActionListData itemtoReset = (ActionListData) getListAdapter().getItem(position);
		Toast.makeText(this, "Implement long click :" + itemtoReset.getActionName(), Toast.LENGTH_LONG).show();
		return false;
	}
	*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accel_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
