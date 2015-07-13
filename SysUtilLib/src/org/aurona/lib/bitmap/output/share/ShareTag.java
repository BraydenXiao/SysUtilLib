package org.aurona.lib.bitmap.output.share;

import org.aurona.lib.packages.AppPackages;

import org.aurona.lib.sysutillib.R;

import android.app.Activity;
import android.content.Context;

public class ShareTag {
public static String caesarApp;
	
	public static String instasquareTag ;
	public static String instashapeTag;
	public static String instaboxTag;
	public static String instapopartTag;
	public static String instabokenTag;
	public static String instagridTag;
	public static String instamirrorTag;
	public static String instacollageTag;
	public static String instatouchTag;
	public static String photomirrorTag;
	public static String instaframeTag;
	public static String instafaceTag;
	public static String instasplitTag;
	public static String blendpicTag;
	public static String instafaceoffTag;
	public static String lidowTag;
	public static String piceditorTag;
	public static String faceproTag;
	public static String squarepicTag;
	public static String xcollageTag;
	public static String fontoverTag;
	public static String squaremakerTag;
	public static String squareeditorTag;

	
	public static void initTag(Context context)
	{
		caesarApp = context.getResources().getString(R.string.tag_app_from) + "@caesarapp ";
		String tag_made_with = context.getResources().getString(R.string.tag_made_with);
		instasquareTag = "("+tag_made_with+"#instasquare )" ;
		instashapeTag = "("+tag_made_with+"#instashape )";
		instaboxTag = "("+tag_made_with+"#instabox )";
		instapopartTag = "("+tag_made_with+"#instapopart )";
		instabokenTag = "("+tag_made_with+"#instaboken )";
		instagridTag = "("+tag_made_with+"#instagrid )";
		instamirrorTag = "("+tag_made_with+"#mirrorpic )";
		instacollageTag = "("+tag_made_with+"#collagepro )";
		instatouchTag = "("+tag_made_with+"#instatouch )";
		photomirrorTag = "("+tag_made_with+"#photomirror )";
		instaframeTag = "("+tag_made_with+"#instaframe )";
		instafaceTag = "("+tag_made_with+"#instaface )";
		instasplitTag = "("+tag_made_with+"#instasplit )";
		blendpicTag = "("+tag_made_with+"#blendpic )";
		instafaceoffTag = "("+tag_made_with+"#instafaceoff )";
		lidowTag = "("+tag_made_with+"#lidow )";
		piceditorTag = "("+tag_made_with+"#piceditor )";
		faceproTag = "("+tag_made_with+"#facepro )";
		squarepicTag = "("+tag_made_with+"#squarepic )";
		xcollageTag = "("+tag_made_with+"#photocreator )";
		fontoverTag = "("+tag_made_with+"#fontover )";
		squaremakerTag = "("+tag_made_with+"#squaremaker )";
		squareeditorTag= "("+tag_made_with+"#instasquare )";

	}

	
	public static String getDefaultTag(Activity activity){
		String localPackage = activity.getPackageName();
		if(caesarApp == null)
		{
			initTag(activity);
		}
		AppPackages pack = new AppPackages();
		if(localPackage.equals(pack.getInstasquarePackage())){
			return instasquareTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstashapePackage())){
			return instashapeTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstaboxPackage())){
			return instaboxTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstapopartPackage())){
			return instapopartTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstabokenPackage())){
			return instabokenTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstagridPackage())){
			return instagridTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstamirrorPackage())){
			return instamirrorTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstacollagePackage())){
			return instacollageTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstatouchPackage())){
			return instatouchTag+caesarApp;
		}
		if(localPackage.equals(pack.getPhotomirrorPackage())){
			return photomirrorTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstaframePackage())){
			return instaframeTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstafacePackage())){
			return instafaceTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstasplitPackage())){
			return instasplitTag+caesarApp;
		}
		if(localPackage.equals(pack.getBlendpicPackage())){
			return blendpicTag+caesarApp;
		}
		if(localPackage.equals(pack.getInstafaceoffPackage())){
			return instafaceoffTag+caesarApp;
		}
		if(localPackage.equals(pack.getLidowPackage())){
			return lidowTag+caesarApp;
		}
		if(localPackage.equals(pack.getPiceditorPackage())){
			return piceditorTag+caesarApp;
		}
		if(localPackage.equals(pack.getFaceproPackage())){
			return faceproTag+caesarApp;
		}
		if(localPackage.equals(pack.getSquarepicPackage())){
			return squarepicTag+caesarApp;
		}
		
		if(localPackage.equals(pack.getFontoverPackage())){
			return fontoverTag+caesarApp;
		}

		if(localPackage.equals(pack.getSquaremakerPackage())){
			return squaremakerTag+caesarApp;
		}
		
		if(localPackage.equals(pack.getSquareEditorPackage())){
			return "#squareblur #instasquare";
		}
		return "";
	}
}
