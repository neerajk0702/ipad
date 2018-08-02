package com.apitechnosoft.ipad.exception;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.apitechnosoft.ipad.ApplicationHelper;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author AST Inc.
 */
public class FNUncaughtExceptionHandler implements UncaughtExceptionHandler {

	Context _context;

	public FNUncaughtExceptionHandler(Context context) {
		this._context = context;
	}

	private boolean isUIThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		if (this.isUIThread()) {
			this.invokeLogActivity(ex);
		} else {
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					FNUncaughtExceptionHandler.this.invokeLogActivity(ex);
				}
			});
		}
	}

	private void invokeLogActivity(Throwable ex) {
		Activity savedActivity = ApplicationHelper.application().getActivity();
	///	PendingIntent myActivity = PendingIntent.getActivity(_context, 0, new Intent(_context, FNSplashScreenFactory.activity().getClass()),PendingIntent.FLAG_ONE_SHOT);
		AlarmManager mgr = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
	//	mgr.set(AlarmManager.RTC, System.currentTimeMillis(), myActivity);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
