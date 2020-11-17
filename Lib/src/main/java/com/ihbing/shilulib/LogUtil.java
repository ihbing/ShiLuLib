package com.ihbing.shilulib;

import android.util.Log;

public class LogUtil
{
	static final String TAG="xdt";
	private static boolean sDebug;
	public static void init(boolean debug){
		sDebug=debug;
	}
	public static void debug(String msg){
		if(sDebug){
			Log.d(TAG+"Debug",msg);
		}
	}
	public static void error(String msg){
		if(sDebug){
			Log.e(TAG+"Error",msg);
		}
	}
	public static void other(String msg){
		if(sDebug){
			Log.e(TAG+"Other",msg);
		}
	}
	public static void printCurLineNumber(){
		StackTraceElement[] stackTraceElements=new Throwable().getStackTrace();
		debug("class->"+stackTraceElements[1].getClassName()+";step->"+stackTraceElements[1].getLineNumber());
	}
}
