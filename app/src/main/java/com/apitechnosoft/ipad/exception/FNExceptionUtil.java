package com.apitechnosoft.ipad.exception;

import android.content.Context;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.utils.ASTLogUtil;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author AST Inc.
 */
public class FNExceptionUtil {

	public static String packageStr = "com.altametrics";

	public static void logException(Throwable ex) {
		logException(ex, ApplicationHelper.application().getActivity());
	}

	public static void logException(Throwable ex, Context context) {
		logException(ex, context, false);
	}

	public static void logException(Throwable ex, Context context, boolean isSendExpToServer) {
		logException(ex, context, isSendExpToServer, false);
	}

	public static void logException(Throwable ex, Context context, boolean isSendExpToServer, boolean sendExpToServerOnStart) {
		ex.printStackTrace();
		if (isSendExpToServer) {
			if (sendExpToServerOnStart) {
			//	ApplicationHelper.application().persistException(ex);
			} else {
			//	ApplicationHelper.application().sendExceptionToServer(ex);
			}
		}
		sendToLogger(ex, context);
	}

	public static void sendToLogger(Throwable ex, Context context) {

		StringBuffer stackTrace = exceptionTrace(ex);
		StringBuilder errorReport = new StringBuilder();
		//errorReport.append(ApplicationHelper.application().deviceInfo());
		errorReport.append("\n************ CAUSE OF ERROR ************\n\n");
		errorReport.append(stackTrace.toString());
		ASTLogUtil.sendToLogger(context, errorReport.toString());
		//writeLogFile(errorReport.toString());
	}

	/*public static void writeLogFile(String exceptionTxt) {

		File file = null;
		try {
			String fileName = "Log_" + new SimpleDateFormat("dd_MMM_yy").format(new Date()) + ".txt";
			//File logDir = new File(ApplicationHelper.application().appDir() + "/logs");
			if (!logDir.exists()) {
				logDir.mkdir();
			}
			file = new File(logDir.getAbsolutePath(), fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
			buf.append(exceptionTxt);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}*/

	public static String exceptionID(Throwable e) {
		return exceptionID(e, true);
	}

	public static String exceptionID(Throwable e, boolean addTopLine) {
		StringBuilder builder = new StringBuilder();
		StackTraceElement elements[] = e.getStackTrace();
		builder.append(elements[0].getClassName());
		builder.append("~");
		builder.append(elements[0].getMethodName());
		if (addTopLine) {
			add3ReleventLinesOfException(builder, elements);// adding top 3 lines
		}
		Throwable e1 = null;
		try {
			e1 = ExceptionUtils.getRootCause(e);
		} catch (Exception e2) {
		}
		if (e1 != null) {
			builder.append("~");
			add3ReleventLinesOfException(builder, e1.getStackTrace());// adding top 3 lines of root cause
		}
		return builder.toString();
	}

	private static void add3ReleventLinesOfException(StringBuilder builder, StackTraceElement rootElements[]) {
		int count = 0;
		String[] packages = packageStr.split("~");
		for (StackTraceElement stackTraceElement : rootElements) {
			if (isInValidLine(stackTraceElement, packages)) {
				continue;
			}
			builder.append("~");
			builder.append(stackTraceElement);
			count++;
			if (count == 3) {
				break;
			}
		}
	}

	private static boolean isInValidLine(StackTraceElement stackTraceElement, String[] packages) {
		boolean isValid = true;
		for (String string : packages) {
			if (stackTraceElement.toString().contains(string)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	public static StringBuffer exceptionTrace(Throwable e) {
		StringBuffer buffer = new StringBuffer();
		if (e != null) {
			try {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);
				printWriter.flush();
				printWriter.close();
				buffer.append(stringWriter.getBuffer());
			} catch (Exception ignore) {
			}
		}
		return buffer;
	}
}
