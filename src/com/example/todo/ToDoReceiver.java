package com.example.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ToDoReceiver extends BroadcastReceiver {

	private static final String TAG = "TodoReceiver";
	Task newtask,edittask;

	@Override
	public void onReceive(Context context, Intent intent) {
	
		Globalvars globalvars=Globalvars.getInstance();
				  String message = intent.getStringExtra("message");
				  Log.d(TAG,"Received Broadcast" +message);
				
				  String msg = message.toString();
					String[] msgparts = msg.split(";");
					DateFormat dateFormat = new SimpleDateFormat(
							"yy/MM/dd hh:mm", Locale.getDefault());
					globalvars.setLastupdatetime(dateFormat
							.format(new Date()));
					String todo = msgparts[0];
					Integer newCount;
					if (todo.equalsIgnoreCase("NEW")) {
						newCount = Integer.valueOf(msgparts[1]);
						//increment count if i am not in sync
						if (Task.getCount() < newCount) {
							Task.setCount(Integer.valueOf(msgparts[1]));
						}
						newtask = new Task(Task.getCount(), msgparts[2],
								msgparts[3], msgparts[4], Boolean
								.valueOf(msgparts[5]));
								globalvars.getTasks().add(newtask);
								globalvars.updateTaskcnt();

								//updateTaskList();
								
								//onResume();
								Intent i = new Intent();
						        i.setClassName("com.example.todo", "com.example.todo.TaskUI");
						        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
						        context.startActivity(i);
								
							

					} else if (todo.equalsIgnoreCase("MODIFY")) {
						edittask = new Task(Integer.valueOf(msgparts[1]),
								msgparts[2], msgparts[3], msgparts[4],
								Boolean.valueOf(msgparts[5]));
								globalvars.editTask(edittask);
								globalvars.updateTaskcnt();
								
								Intent i = new Intent();
						        i.setClassName("com.example.todo", "com.example.todo.TaskUI");
						        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
						        context.startActivity(i);
												
					}
			

		
	}
}

