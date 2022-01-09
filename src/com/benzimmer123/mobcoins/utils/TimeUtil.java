package com.benzimmer123.mobcoins.utils;

public class TimeUtil {

	public static String getTimeAsString(long time) {
		if (time == -1)
			return "";

		long difference = time - System.currentTimeMillis();
		int total = (int) difference / 1000;
		String cooldownTime = getTime(total);
		return cooldownTime;
	}

	private static String getTime(int seconds) {
		if (seconds < 60) {
			return seconds + "s";
		}

		int minutes = seconds / 60;
		int s = 60 * minutes;
		int secondsLeft = seconds - s;

		if (minutes < 60) {
			if (secondsLeft > 0) {
				return String.valueOf(minutes + "m " + secondsLeft + "s");
			}
			return String.valueOf(minutes + "m");
		}

		if (minutes < 1440) {
			int hours = minutes / 60;
			String time = hours + "h";
			int inMins = 60 * hours;
			int leftOver = minutes - inMins;
			if (leftOver >= 1) {
				time = time + " " + leftOver + "m";
			}
			if (secondsLeft > 0) {
				time = time + " " + secondsLeft + "s";
			}
			return time;
		}

		int days = minutes / 1440;
		String time = days + "d";
		int inMins = 1440 * days;
		int leftOver = minutes - inMins;

		if (leftOver >= 1) {
			if (leftOver < 60) {
				time = time + " " + leftOver + "m";
			} else {
				int hours = leftOver / 60;
				time = time + " " + hours + "h";
				int hoursInMins = 60 * hours;
				int minsLeft = leftOver - hoursInMins;
				time = time + " " + minsLeft + "m";
			}
		}

		if (secondsLeft > 0) {
			time = time + " " + secondsLeft + "s";
		}

		return time;
	}
}
