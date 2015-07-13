package org.aurona.lib.activity;

import org.aurona.lib.sysutillib.R;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;




public class FragmentActivityTemplate extends android.support.v4.app.FragmentActivity{
	protected ProcessDialogFragment dlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}
	
	protected void showProcessDialog() {
		try{
			if(dlg != null){
				FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
				transaction.remove(dlg);
				transaction.addToBackStack(null);
				transaction.commit();
				dlg = null;
			}
			
			if(this.dlg == null){
				dlg = new ProcessDialogFragment();  
		        Bundle bundle = new Bundle();  
		        bundle.putString("text", this.getResources().getString(R.string.dlg_processing));  
		        dlg.setArguments(bundle);  
			}
			
			dlg.show(this.getSupportFragmentManager(), "process");
		}catch(Exception e){
			//FlurryAgent.logEvent("ShowDialogNullException");
		}
	}
		
	protected void dismissProcessDialog() {
		try{
			if(dlg != null){
				this.dlg.dismissAllowingStateLoss();
				FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
				transaction.remove(dlg);
				transaction.addToBackStack(null);
				transaction.commit();
				dlg = null;
			}
			//dlg = null;
		}catch(Exception ex){
			//FlurryAgent.logEvent("DismissDialogNull");
		}
	}
	
	
	
	public class BtnBackOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
	        FragmentActivityTemplate.this.finish();   		
		}    	
    }	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {		
			   this.finish();
			}
		return false;
	}
}
