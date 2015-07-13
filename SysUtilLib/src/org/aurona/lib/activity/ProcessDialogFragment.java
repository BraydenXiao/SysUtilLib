package org.aurona.lib.activity;



import android.app.Dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class ProcessDialogFragment extends DialogFragment {
	
	public static ProcessDialogFragment getInstance(String processText){
		ProcessDialogFragment myDialogFragment = new ProcessDialogFragment();  
        Bundle bundle = new Bundle();  
        bundle.putString("text", processText);  
        myDialogFragment.setArguments(bundle);  
        return myDialogFragment;  
	}
	
	
	 @Override  
	 public Dialog onCreateDialog(Bundle savedInstanceState) {  
		 
		 String args = getArguments().getString("text");  
		 ProgressDialog dialog = new ProgressDialog(this.getActivity());
		 dialog.setMessage(args);
		 return dialog;
	 }


	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);
	}


	@Override
	public int show(FragmentTransaction transaction, String tag) {
		return super.show(transaction, tag);
	}
	 
	 
}
