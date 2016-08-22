package com.capture.accelo;

public class ActionListData 
{
	private String id;
	private String actionName;
	private String delay;
	private String freq;
	private boolean selected;

	public ActionListData(String idin, String actionnamein, String delayin, String freqin, boolean selectedin) 
	{
		this.id = idin;
		this.actionName = actionnamein;
		this.delay = delayin;
		this.freq = freqin;
		selected = selectedin;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionnamein) 
	{
		if((actionnamein.isEmpty()) || (actionnamein.length()==0))
		{
			this.actionName = "Unknown";
		}else{
			this.actionName = actionnamein;	
		}
	}

	public String getID() {
		return id;
	}

	public void setID(String idin) {
		this.id = idin;
	}
	
	public String getDelay() {
		return delay;
	}

	public void setDelay(String delayin) {
		this.delay = delayin;
	}
	
	public String getFreq() {
		return freq;
	}

	public void setFreq(String freqin) {
		this.freq = freqin;
	}
	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean isSelected) {
		this.selected = isSelected;
	}	
}