package com.benzimmer123.mobcoins.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class LicenseValidation {
	private String licenseKey;
	private Plugin plugin;
	private String validationServer;
	private LogType logType = LogType.NORMAL;
	private String securityKey = "YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";
	private boolean debug = false;

	public LicenseValidation(String licenseKey, String validationServer, Plugin plugin) {
		this.licenseKey = licenseKey;
		this.plugin = plugin;
		this.validationServer = validationServer;
	}

	public LicenseValidation setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
		return this;
	}

	public LicenseValidation setConsoleLog(LogType logType) {
		this.logType = logType;
		return this;
	}

	public LicenseValidation debug() {
		this.debug = true;
		return this;
	}

	public boolean register() {
		log(0, "[]==========[License-System]==========[]");
		log(0, "Connecting to License-Server...");
		ValidationType vt = isValid();
		if (vt == ValidationType.VALID) {
			log(1, "License valid!");
			log(0, "[]==========[License-System]==========[]");
			return true;
		}
		log(1, "License is NOT valid!");
		log(1, "Failed as a result of " + vt.toString());
		log(1, "Disabling plugin!");
		log(0, "[]==========[License-System]==========[]");

		Bukkit.getScheduler().cancelTasks(this.plugin);
		Bukkit.getPluginManager().disablePlugin(this.plugin);
		return false;
	}

	public boolean isValidSimple() {
		return (isValid() == ValidationType.VALID);
	}

	private String requestServer(String v1, String v2) throws IOException {
		URL url = new URL(this.validationServer + "?v1=" + v1 + "&v2=" + v2 + "&pl=" + this.plugin.getName());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		if (this.debug) {
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

			StringBuilder response = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			return response.toString();
		}
	}

	public ValidationType isValid() {
		String rand = toBinary(UUID.randomUUID().toString());
		String sKey = toBinary(this.securityKey);
		String key = toBinary(this.licenseKey);

		try {
			String response = requestServer(xor(rand, sKey), xor(rand, key));

			if (response.startsWith("<")) {
				log(1, "The License-Server returned an invalid response!");
				log(1, "In most cases this is caused by:");
				log(1, "1) Your Web-Host injects JS into the page (often caused by free hosts)");
				log(1, "2) Your ValidationServer-URL is wrong");
				log(1, "SERVER-RESPONSE: " + ((response.length() < 150 || this.debug) ? response : (response.substring(0, 150) + "...")));
				return ValidationType.PAGE_ERROR;
			}

			try {
				return ValidationType.valueOf(response);
			} catch (IllegalArgumentException exc) {
				String respRand = xor(xor(response, key), sKey);
				if (rand.substring(0, respRand.length()).equals(respRand)) {
					return ValidationType.VALID;
				}
				return ValidationType.WRONG_RESPONSE;
			}
		} catch (IOException e) {
			if (this.debug)
				e.printStackTrace();
			return ValidationType.PAGE_ERROR;
		}
	}

	private static String xor(String s1, String s2) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < Math.min(s1.length(), s2.length()); i++)
			result.append(Byte.parseByte("" + s1.charAt(i)) ^ Byte.parseByte(s2.charAt(i) + ""));
		return result.toString();
	}

	public enum LogType {
		NORMAL,
		LOW,
		NONE;
	}

	public enum ValidationType {
		WRONG_RESPONSE,
		PAGE_ERROR,
		URL_ERROR,
		KEY_OUTDATED,
		KEY_NOT_FOUND,
		NOT_VALID_IP,
		INVALID_PLUGIN,
		VALID;
	}

	private String toBinary(String s) {
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append(((val & 0x80) == 0) ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}

	private void log(int type, String message) {
		if (this.logType == LogType.NONE || (this.logType == LogType.LOW && type == 0))
			return;
		System.out.println(message);
	}
}
