package org.aurona.lib.bitmap.output.share;
//package com.baiwang.lib.bitmap.output.share;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Bitmap.CompressFormat;
//import android.widget.Toast;
//
//import com.baiwang.lib.sysutillib.R;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.SendMessageToWX;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.openapi.WXImageObject;
//import com.tencent.mm.sdk.openapi.WXMediaMessage;
//import com.tencent.mm.sdk.openapi.WXTextObject;
//
//public class ShareToWeChat {
//	static final int THUMB_SIZE = 100;
//	static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
//	private IWXAPI api;
//	 
//	private Context context;
//	private boolean installed = false;
//	
//	public ShareToWeChat(Context context,String apiid){
//		this.context = context;
//		SetIWAPI(apiid);
//		installed = api.isWXAppInstalled();
//	}
//	
//	public void SetIWAPI(String apiid)
//	{
//		api = WXAPIFactory.createWXAPI(context, apiid);
//	}
//	
//	private static ShareToWeChat instance = null;
//
//	public static ShareToWeChat getInstance(Context context,String apiid) {
//	      if (instance == null) {                              //line 12    
//	          instance = new ShareToWeChat( context, apiid);          //line 13    
//	      }    
//	     return instance;    
//	}  
//	
//	public boolean isTimeLineSupport(){
//		int wxSdkVersion = api.getWXAppSupportAPI();
//		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
//			return true;
//		}
//		return false;
//	}
//	
//
//	
//	public static boolean shareImageToWeChat(Activity activity, Bitmap bmp,String apiid){
//		if(bmp == null || bmp.isRecycled()){
//			Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_image), Toast.LENGTH_LONG).show();
//			return false;
//		}
//
//		IWXAPI api = WXAPIFactory.createWXAPI(activity, apiid);
//		if(!api.isWXAppInstalled()){
//			Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_installed), Toast.LENGTH_LONG).show();
//			return false;
//		}
//		return getInstance(activity,apiid).sendWeChat(bmp,false);
//	}
//
//	public static boolean shareImageToWeChatMoments(Activity activity, Bitmap bmp,String apiid){
//		if(bmp == null || bmp.isRecycled()){
//			Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_image), Toast.LENGTH_LONG).show();
//			return false;
//		}
//
//		IWXAPI api = WXAPIFactory.createWXAPI(activity, apiid);
//		if(!api.isWXAppInstalled()){
//			Toast.makeText(activity, activity.getResources().getString(R.string.warning_no_installed), Toast.LENGTH_LONG).show();
//			return false;
//		}
//		return getInstance(activity,apiid).sendWeChat(bmp,true);
//	}
//	
//	
//	public boolean shareImageToWeChat(Bitmap bmp){
//		if(bmp == null || bmp.isRecycled()){
//			Toast.makeText(context, context.getResources().getString(R.string.warning_no_image), Toast.LENGTH_LONG).show();
//			return false;
//		}
//
//
//		if(!installed){
//			Toast.makeText(context, context.getResources().getString(R.string.warning_no_installed), Toast.LENGTH_LONG).show();
//			return false;
//		}
//		
//		return sendWeChat(bmp, false);
//	}
//	
//	public boolean shareImageToWeChatMoments(Bitmap bmp){
//		if(bmp == null || bmp.isRecycled()){
//			Toast.makeText(context, context.getResources().getString(R.string.warning_no_image), Toast.LENGTH_LONG).show();
//			return false;
//		}
//
//
//		if(!installed){
//			Toast.makeText(context, context.getResources().getString(R.string.warning_no_installed), Toast.LENGTH_LONG).show();
//			return false;
//		}
//		
//		return sendWeChat(bmp, true);
//	}
//	
//	public boolean sendWeChat(String filename,boolean isTimeLine){
//		File file = new File(filename);
//		if (!file.exists()) {
//			return false;
//		}
//		
//		WXImageObject imgObj = new WXImageObject();
//		imgObj.setImagePath(filename);
//		
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = imgObj;
//		
//		Bitmap bmp = BitmapFactory.decodeFile(filename);
//		float r = (float)bmp.getWidth() / (float)bmp.getHeight();
//		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, (int)((float)THUMB_SIZE * r), THUMB_SIZE, true);
//		msg.thumbData = bmpToByteArray(thumbBmp, true);
//		bmp.recycle();
//		
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = buildTransaction("img");
//		req.message = msg;
//		req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//		boolean result  = api.sendReq(req);
//		return result;
//	}
//	
//	public boolean sendWeChat(Bitmap bmp,boolean isTimeLine){
//
//		WXImageObject imgObj = new WXImageObject(bmp);		
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = imgObj;
//		
//		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//		//bmp.recycle();
//		msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = buildTransaction("img");
//		req.message = msg;
//		req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//		boolean result = api.sendReq(req);
//		return result;
//	}
//	
//	public boolean sendMoment(String text){
//		WXTextObject textObj = new WXTextObject();
//		textObj.text = text;
//
//		// 用WXTextObject对象初始化一个WXMediaMessage对象
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = textObj;
//		// 发送文本类型的消息时，title字段不起作用
//		// msg.title = "Will be ignored";
//		msg.description = text;
//
//		// 构造一个Req
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
//		req.message = msg;
//		req.scene =  SendMessageToWX.Req.WXSceneTimeline;
//		boolean result = api.sendReq(req);
//		return result;
//	}
//	
//	private String buildTransaction(final String type) {
//		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//	}
//	
//	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		bmp.compress(CompressFormat.PNG, 100, output);
//		if (needRecycle) {
//			bmp.recycle();
//		}
//		
//		byte[] result = output.toByteArray();
//		try {
//			output.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
//}
