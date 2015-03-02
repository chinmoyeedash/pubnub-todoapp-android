package com.example.todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AppUI extends Activity {
	String collaborator,time;
	Globalvars globalvars;
	TextView updatetimetext;
	Button tasks;
	Integer cnt,taskcnt,donetaskcnt;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appui);
		collaborator=getIntent().getStringExtra("collaborator");
		updatetimetext=(TextView) findViewById(R.id.updatetimetext);
		
		tasks=(Button) findViewById(R.id.taskbutton);
		tasks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AppUI.this,TaskUI.class);
				intent.putExtra("collaborator", collaborator);
				intent.putExtra("taskcnt",taskcnt );
				intent.putExtra("donetaskcnt", donetaskcnt);
				startActivity(intent);

			}
		});
	}

	@Override
	protected void onResume() {
		
		globalvars=Globalvars.getInstance();
		time=globalvars.getLastupdatetime();
		if (time!=null)  updatetimetext.setText(time);
		cnt=globalvars.getTaskcnt();
		ArrayList<Task> alltasks=globalvars.getTasks();
		taskcnt=0;donetaskcnt=0;
		for(Task task:  alltasks) {
			taskcnt++;
			if (task.status) {
				donetaskcnt++;
			}
		}
		tasks.setText("Tasks "+ donetaskcnt+"/"+taskcnt);
		super.onResume();
	}

}
