package org.aurona.lib.type;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DataString {  
	
    private String mYear;  
    private String mMonth;  
    private String mDay;  
    private String mWay;
    private Calendar c;
    
    public DataString()
    {
    	c = Calendar.getInstance();  
        c.setTimeZone(TimeZone.getDefault());
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
    }

    public DataString(Calendar c)
    { 
    	this.c = c;
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
    }

    public DataString(Calendar c,TimeZone t)
    { 
    	c.setTimeZone(t);  
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
    }
    
      
    public String getCNStringData(){  
        final Calendar c = Calendar.getInstance();  
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份  
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份  
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK)); 
        String strEnWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){  
        	strEnWeek ="天";  
        }else if("2".equals(mWay)){  
        	strEnWeek ="一";  
        }else if("3".equals(mWay)){  
        	strEnWeek ="二";  
        }else if("4".equals(mWay)){  
        	strEnWeek ="三";  
        }else if("5".equals(mWay)){  
        	strEnWeek ="四";  
        }else if("6".equals(mWay)){  
        	strEnWeek ="五";  
        }else if("7".equals(mWay)){  
        	strEnWeek ="六";  
        }  
        return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+strEnWeek;  
    }
    
    public String getENWeekString()
    {
    	String strEnWeek = String.valueOf(c.get(Calendar.DAY_OF_WEEK)); 
        if("1".equals(mWay)){  
        	strEnWeek ="Sunday";  
        }else if("2".equals(mWay)){  
        	strEnWeek ="Monday";  
        }else if("3".equals(mWay)){  
        	strEnWeek ="Tuesday";  
        }else if("4".equals(mWay)){  
        	strEnWeek ="Wednesday";  
        }else if("5".equals(mWay)){  
        	strEnWeek ="Thursday";  
        }else if("6".equals(mWay)){  
        	strEnWeek ="Friday";  
        }else if("7".equals(mWay)){  
        	strEnWeek ="Saturday";  
        } 
    	return strEnWeek;
    }
    

    public static String getDateString(String format,Locale locale)
    {
    	SimpleDateFormat formatter = new SimpleDateFormat (format,locale);       
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
    	String str = formatter.format(curDate);
    	return str;
    }
      
}  
