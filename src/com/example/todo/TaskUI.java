package com.example.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

public class TaskUI extends Activity {

	private static final String TAG = "TaskUI";
	private static final String PREFS_NAME = "subscribed";
	String action;
	String collaborator;
	private ListView listView;
	private ArrayList<Task> tasks;
	private Task task,newtask,edittask;
	/** Called when the activity is first created. */
	private TaskAdapter taskAdapter;
	protected int requestcode,index;
	private Pubnub pubnub;
	TextView tasklistheader;
	private int taskcnt,donetaskcnt;
	private ArrayList<Task> taskslist;

	private static Globalvars globalvars;
	static String PUBLISH_KEY = "demo";
	static String SUBSCRIBE_KEY = "demo";	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskui);
		if (pubnub == null) {
			pubnub = new Pubnub(PUBLISH_KEY,SUBSCRIBE_KEY);
		}

		collaborator=getIntent().getStringExtra("collaborator");
		globalvars=Globalvars.getInstance();
		taskcnt=0;donetaskcnt=0;
		tasklistheader=(TextView) findViewById(R.id.tasklistheader);
		tasklistheader.setText("Tasks ("+donetaskcnt+"/"+taskcnt+")");
		listView = (ListView)findViewById(R.id.listView1);
		Log.d(TAG,"Calling oncreate" );
	
		//Call init only once when app installed.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(!prefs.getBoolean("1", false)) {
			
			init();
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("1", true);
			editor.commit();
		}
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG,"listview item clicked");
				Intent intent=new Intent(getApplicationContext(),ModifyTaskUI.class);
				intent.putExtra("collaborator", collaborator);
				intent.putExtra("action", "modify");
				index=position;
				task=(Task)listView.getItemAtPosition(position);
				intent.putExtra("task",task );
				Log.d(TAG,"Modifying task "+task.toString());
				requestcode=1;
				startActivityForResult(intent,requestcode);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add:
			Intent intent=new Intent(getApplicationContext(),ModifyTaskUI.class);
			intent.putExtra("collaborator", collaborator);
			intent.putExtra("action", "add");
			requestcode=2;
			startActivityForResult(intent,requestcode);
			
			break;	
		}
		return false;
	}

	// Call Back method  to get the Message form other Activity  
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);  
		if (data!=null) {
			
		// check if the request code is same as what is passed  here it is 2  
		if(requestCode==2)  
		{  
			newtask=data.getParcelableExtra("edittask"); 	      
			Callback callback = new Callback() {
				public void successCallback(String channel, Object response) {
					Log.d("PUBNUB","finished publishing" +response.toString());
				}
				public void errorCallback(String channel, PubnubError error) {
					Log.d("PUBNUB",error.toString());
				}
			};
			Log.d("PUBNUB","calling publish" + newtask.toString() );
			pubnub.publish("newtodochannel", "NEW"+";"+newtask.getId()+";"+newtask.getDescription()+";"+newtask.getOwner()+";"+newtask.getUpdatetime()+";"+newtask.getStatus(), callback);
		}
		if (requestCode==1) {
			if(resultCode==2){         	   
				edittask=data.getParcelableExtra("edittask"); 
				Log.d(TAG,"Modifying task "+edittask.toString()+"at index"+index);

				Callback callback = new Callback() {
					public void successCallback(String channel, Object response) {
						Log.d("PUBNUB","finished publishing MODIFY" +response.toString());


					}
					public void errorCallback(String channel, PubnubError error) {
						Log.d("PUBNUB",error.toString());
					}
				};
				Log.d("PUBNUB","calling publish for modify" + edittask.toString() );
				pubnub.publish("newtodochannel", "MODIFY"+";"+edittask.getId()+";"+edittask.getDescription()+";"+edittask.getOwner()+";"+edittask.getUpdatetime()+";"+edittask.getStatus(), callback);
			} 

		}
		}

	}  

	private void init() {

		//Map<String, String> map = getCredentials();
		Log.d(TAG,"init called");
		// Subscribe to a channel
		try {
			pubnub.subscribe("newtodochannel", new Callback() {

				private Task newtask;
				@Override
				public void connectCallback(String channel, Object message) {
					System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}

				@Override
				public void disconnectCallback(String channel, Object message) {
					System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}

				public void reconnectCallback(String channel, Object message) {
					System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}

				@Override
				public void successCallback(String channel, Object message) {
					System.out.println("SUBSCRIBE success: " + channel + " : "+ message.getClass() + " : " + message.toString());
					String msg=message.toString();
					String[] msgparts=msg.split(";");
					DateFormat dateFormat=new SimpleDateFormat("yy/MM/dd hh:mm",Locale.getDefault());
					globalvars.setLastupdatetime(dateFormat.format(new Date()));
					String todo=msgparts[0];
					Integer newCount;
					if (todo.equalsIgnoreCase("NEW")) {
						newCount=Integer.valueOf(msgparts[1]);
						//increment count if i am not in sync
						if (Task.getCount()<newCount) {
								Task.setCount(Integer.valueOf(msgparts[1]));
						} 
						newtask=new Task(Task.getCount(),msgparts[2],msgparts[3],msgparts[4],Boolean.valueOf(msgparts[5]));			           
						TaskUI.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								globalvars.getTasks().add(newtask);
								globalvars.incrTaskcnt();
						
								updateTaskList();
								Log.d(TAG,"Adapterdata"+taskAdapter.getTasks().toString());
								//onResume();
								Intent i = getIntent();
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							    startActivity(i);
							    finish();
							}
						});
					} else if (todo.equalsIgnoreCase("MODIFY")) {
						edittask=new Task(Integer.valueOf(msgparts[1]),msgparts[2],msgparts[3],msgparts[4],Boolean.valueOf(msgparts[5]));			           
						
						TaskUI.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								globalvars.editTask(edittask);
								Log.d(TAG,"Adapterdata"+taskAdapter.getTasks().toString());
								Log.d(TAG,"tasks afyter edit"+tasks);
								Intent i = getIntent();
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							    startActivity(i);
							    finish();
							}
						});
					}
				}
				@Override
				public void errorCallback(String channel, PubnubError error) {
					System.out.println("SUBSCRIBE : ERROR on channel " + channel + " : " + error.toString());
				}
			});
		} catch (PubnubException e) {
			System.out.println(e.toString());
		}
	}
	
	private void updateTaskList() {
		taskcnt=0;donetaskcnt=0;
		for(Task task:tasks) {
			taskcnt++;
			if (task.status) {
				donetaskcnt++;
			}
		}
		Log.d(TAG,"updated taskcnt for "+tasks+"done="+donetaskcnt+"/"+taskcnt);
		tasklistheader.setText("Tasks "+ donetaskcnt+"/"+taskcnt);
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("collaborator", collaborator);
		outState.putString("action", action);
		outState.putInt("index",index);
		outState.putParcelableArrayList("tasks", tasks);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		collaborator=savedInstanceState.getString("collaborator");
		action=savedInstanceState.getString("action");
		index=savedInstanceState.getInt("index");
		super.onRestoreInstanceState(savedInstanceState);
	}


	@Override
	protected void onResume() {
		  tasks=globalvars.getTasks();
		  Log.d(TAG,"Calling onresume" +tasks.toString());
		  taskAdapter = new TaskAdapter(TaskUI.this,R.id.list_item, tasks);
	      taskAdapter.notifyDataSetChanged();
		  listView.setAdapter(taskAdapter);
		  updateTaskList();
		super.onResume();
	}
}
