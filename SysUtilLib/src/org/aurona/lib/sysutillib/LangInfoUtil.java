package org.aurona.lib.sysutillib;

import java.util.Locale;

public class LangInfoUtil {
	public static  boolean isLangChinese(){
		boolean rtnVlue = false;
		Locale l = Locale.getDefault();
		String langcode = l.getLanguage();
		if(langcode.equalsIgnoreCase("zh")){
			rtnVlue = true;
		}
		return rtnVlue;
	}
	
	public static  boolean isLangSimpleChinese(){
		boolean rtnVlue = false;
		Locale l = Locale.getDefault();
		String langcode = l.getLanguage();
		String countryCode = l.getCountry();	
		if(langcode.equalsIgnoreCase("zh") && countryCode.equalsIgnoreCase("cn")){
			rtnVlue = true;
		}
		return rtnVlue;
	}
	
	public static  boolean isLangTraditionalChinese(){
		boolean rtnVlue = false;
		Locale l = Locale.getDefault();
		String langcode = l.getLanguage();
		String countryCode = l.getCountry();	
		if((langcode.equalsIgnoreCase("zh") && countryCode.equalsIgnoreCase("TW"))
				|| langcode.equalsIgnoreCase("zh") && countryCode.equalsIgnoreCase("HK")){
			rtnVlue = true;
		}
		return rtnVlue;
	}
}
