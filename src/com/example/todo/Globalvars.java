package com.example.todo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.util.Log;

import com.pubnub.api.Pubnub;

public class Globalvars  {
	
	private static final String TAG = "Globalvars";
	private String lastupdatetime;
	private Integer taskcnt=0;
	private static Globalvars instance;
	private static Globalvars globalvars;
	static Pubnub pubnub;
	static String PUBLISH_KEY = "demo";
    static String SUBSCRIBE_KEY = "demo";	
    
	private ArrayList<Task> tasks=new ArrayList<Task>();
	
	// Restrict the constructor from being instantiated
	private Globalvars(){}
	   
	public String getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
		Log.d(TAG,"setting lastupdatetime to "+lastupdatetime); 
	}
	
	public  ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public Integer getTaskcnt() {
		return this.taskcnt;
	}
	public  void incrTaskcnt() {
		this.taskcnt++;
		Log.d(TAG,"Taskcnt incr to "+this.taskcnt); 
	}
	
	public void editTask(Task edittask) {
		
		
		
		ListIterator<Task> iter = (ListIterator<Task>) tasks.listIterator();

		while (iter.hasNext()) {
		    Task t = iter.next();

		    if (t.getId()==edittask.getId()) {
				Log.d("Globalvars", "Editing task "+t.toString());
				int index=this.tasks.indexOf(t);
				Log.d("Globalvars", "tasks before Editing at index "+index+ ":"+tasks.toString());
				
				iter.remove();
				iter.add(edittask);
				Log.d("Globalvars", "tasks after Editing "+tasks.toString());
			}
		        
		}
		
	}
	 public static synchronized Globalvars getInstance(){
	     if(instance==null){
	    	Log.d(TAG,"Creating new instance"); 
	    	instance=new Globalvars();
	    	
	     }
			Log.d(TAG,"returning old instance");
	     return instance;
	 }
	 
}
