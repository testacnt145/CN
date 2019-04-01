package com.chattynotes.util;

import com.chattynotes.application.App;
import com.chattynoteslite.R;
import org.joda.time.DateTime;

public class DateUtil {

	//Unix time (also known as POSIX time or Epoch time) is number of seconds(not milliseconds) that have elapsed since 00:00:00 Coordinated Universal Time (UTC),
	//Unix time is in seconds, Java time is milliseconds
	//WhatsApp stores timeStamp in milliseconds for Chats table, but uses Unix timeStamp for groupJID
	
	//We are also storing all timeStamp in milliseconds, but using Media-Name and groupJID as unixTimestamp
	
	
//______________________________________________________________________________________________________________ 
	public static long getDateInUnixTimestamp() {//i.e seconds
		return new DateTime().getMillis()/1000;
	}
	
	public static long getDateInMilliseconds() {
		return new DateTime().getMillis();
		//return new DateTime().minusDays(3).getMillis(); //yesterday
		//return new DateTime().plusDays(1).getMillis(); //tomorrow
	}
	
//	public static long getDateInMilliseconds(String Date_Time) { //for old time, calculate using Date in String
//		DateTime date = new DateTime();
//		date = DateTimeFormat.forPattern("MMM dd, yyyy, hh:mm:ss a").parseDateTime(Date_Time);
//		return date.getMillis();
//	}
	
//	public static String lastSeenDate() {//for new time
//		return new DateTime().toString("dd/MM/yy, hh:mm a");
//	}
//
//	public static String lastSeenDate(long seconds) {//calculate using seconds
//		//_seconds =  user is not last seen from 120 seconds
//		long currentSeconds = getDateInUnixTimestamp();
//		long actualTimestamp = currentSeconds - seconds;
//		DateTime last_seen_date = new DateTime(actualTimestamp * 1000);
//		return last_seen_date.toString("dd/MM/yy, hh:mm a");
//	}
	
	//______________ to display UI
//	public static String getDateInStringComplete() {
//		return new DateTime().toString("MMM dd, yyyy, hh:mm:ss a");
//	}
	
	public static String getDateInStringComplete(long _milliseconds) {
		return new DateTime(_milliseconds).toString("MMM dd, yyyy, hh:mm:ss a");
	}
	public static String getDateInStringForConversationList(long _milliseconds){
		return new DateTime(_milliseconds).toString("hh:mm a");
	}
	
	public static String getDateInStringForMsgTimestampDialog(long _milliseconds) {
		DateTime org = new DateTime(_milliseconds);

		//https://stackoverflow.com/a/6525490
		DateTime now = new DateTime();
		DateTime tomorrow = org.plusDays(1);
		DateTime dayAfterTomorrow = org.plusDays(2);
		DateTime startOfToday = org.withTimeAtStartOfDay();
		DateTime endOfToday = tomorrow.withTimeAtStartOfDay();
		DateTime endOfTomorrow = dayAfterTomorrow.withTimeAtStartOfDay();
		long orgMS = org.getMillis();
		long nowMS = now.getMillis();
		long startMS = startOfToday.getMillis();
		long endMS = endOfToday.getMillis();
		long endTMS = endOfTomorrow.getMillis();

		double minutes = (nowMS-orgMS)/1000/60;
		double hours = minutes/60;
		if(minutes>=0 && minutes<2)
			return "Just now, " + org.toString("hh:mm a");
		else if(hours>=0 && hours<1)
			return (int)minutes + " minutes ago, " + org.toString("hh:mm a");
		/*
		else if(hours >= 1 && hours < 24)
			return "Today, " + org.toString("hh:mm a");
		else if(hours>=24 && hours<48)
			return "Yesterday, " + org.toString("hh:mm a");
		*/
		else if((startMS<=nowMS) && (nowMS<endMS))
			return "Today, " + org.toString("hh:mm a");
		else if((nowMS>=endMS) && (nowMS<endTMS))
			return "Yesterday, " + org.toString("hh:mm a");
		else if(hours>=48 && hours<168)
			return org.toString("EEEE") + ", " + org.toString("hh:mm a");
		else
			return org.toString("MMM dd, yyyy, hh:mm a");
	}

	//https://stackoverflow.com/a/15465237/4754141
	//https://stackoverflow.com/a/38931440/4754141
	private static final String SPACE = " ";
	private static final String AND = SPACE + "and" + SPACE;
	public static String getDateInStringForMsgTimestampDialogCompletion(long _milliseconds) {
		int s = (int) (_milliseconds / (1000)) % 60 ;
		int m = (int) (_milliseconds / (1000*60)) % 60;
		int h = (int) (_milliseconds / (1000*60*60)) % 24;
		int d = (int) (_milliseconds / (1000*60*60*24)) % 7;
		int w = (int) (_milliseconds / (1000*60*60*24*7));
		if(w>0)
			return f(R.plurals.week,w) + SPACE + f(R.plurals.day,d) + AND  + f(R.plurals.hour,h);
		else if(d>0)
			return f(R.plurals.day,d) + SPACE + f(R.plurals.hour,h) + AND + f(R.plurals.minute,m);
		else if(h>0)
			return f(R.plurals.hour,h) + AND + f(R.plurals.minute,m);
		else if(m>0)
			return f(R.plurals.minute,m) + AND + f(R.plurals.second,s);
		else
			return f(R.plurals.second,s);
	}
	private static String f(int xml, int x) {
		return String.format(App.getQuantityString(xml, x), x);
	}

	public static String getDateInStringForChatList(long _milliseconds) {
//		//System.out.println("1  : " + new Date().getTime());
//		//System.out.println("2  : " + new DateTime().getMillis());
//		
//		DateTime date = DateTimeFormat.forPattern("MMM dd, yyyy, hh:mm:ss a").parseDateTime(Date_Time);
//		//Date date = new SimpleDateFormat("MMM dd, yyyy, KK:mm:ss.S a",Locale.ENGLISH).parse(Date_Time);

		DateTime org = new DateTime(_milliseconds);
		//https://stackoverflow.com/a/6525490
		DateTime now = new DateTime();
		DateTime tomorrow = org.plusDays(1);
		DateTime dayAfterTomorrow = org.plusDays(2);
		DateTime startOfToday = org.withTimeAtStartOfDay();
		DateTime endOfToday = tomorrow.withTimeAtStartOfDay();
		DateTime endOfTomorrow = dayAfterTomorrow.withTimeAtStartOfDay();
		long orgMS = org.getMillis();
		long nowMS = now.getMillis();
		long startMS = startOfToday.getMillis();
		long endMS = endOfToday.getMillis();
		long endTMS = endOfTomorrow.getMillis();
		double minutes = (nowMS-orgMS)/1000/60;
		double hours = minutes/60;
		if((startMS<=nowMS) && (nowMS<endMS))
			return org.toString("hh:mm a");
		else if((nowMS>=endMS) && (nowMS<endTMS))
			return "YESTERDAY";
		else if(hours>=48 && hours<168)
			return org.toString("EEEE");
		else
			return org.toString("MMM dd, yyyy");


		//_____________________________________________ CONVERTING anyDateInString TO CUSTOM FORMAT
		//DateTime date = DateTimeFormat.forPattern("MMM dd, yyyy, hh:mm:ss a").parseDateTime(anyDateInString);
		//return date.toString("hh:mm a");
		
		
		
		//_____________________________
		//http://stackoverflow.com/questions/15333320/how-to-convert-joda-time-to-date-of-java-util-date-and-vice-versa
		
		
		//---------------------------------- To convert java.util.Date to Joda DateTime:-
		//Date date = new Date();
		//DateTime dateTime = new DateTime(date);
		//With TimeZone, 
		//DateTime dateTimeNew = new DateTime(date.getTime(), timeZone);
		
		//----------------------------------- To convert Joda DateTime to java.util.Date
		//Date dateNew = dateTime.toDate();
		//With TimeZone, 
		//Date dateTimeZone = dateTime.toDateTimeAtStartOfDay(timeZone).toDate();
		
		
		/*
		 *  G   Era designator       Text               AD
			y   Year                 Year               1996; 96
			M   Month in year        Month              July; Jul; 07
			w   Week in year         Number             27
			W   Week in month        Number             2
			D   Day in year          Number             189
			d   Day in month         Number             10
			F   Day of week in month Number             2
			E   Day in week          Text               Tuesday; Tue
			u   Day number of week   Number             1
			a   Am/pm marker         Text               PM
			H   Hour in day (0-23)   Number             0
			k   Hour in day (1-24)   Number             24
			K   Hour in am/pm (0-11) Number             0
			h   Hour in am/pm (1-12) Number             12
			m   Minute in hour       Number             30
			s   Second in minute     Number             55
			S   Millisecond          Number             978
			z   Time zone            General time zone  Pacific Standard Time; PST; GMT-08:00
			Z   Time zone            RFC 822 time zone  -0800
			X   Time zone            ISO 8601 time zone -08; -0800; -08:00
		 * 
		 */
	}
		
}
