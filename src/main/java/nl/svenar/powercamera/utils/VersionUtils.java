package nl.svenar.powercamera.utils;

public class VersionUtils {

	public static int getJavaVersion() {
		String version = System.getProperty("java.version");
		if (version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if (dot != -1) {
				version = version.substring(0, dot);
			}
		}
		return Integer.parseInt(version);
	}

	public static int calculateVersionFromString(String input) {
		int output = 0;
		input = input.split("R")[0].replaceAll("[a-zA-Z- ]", "");
		String[] input_split = input.split("\\.");

		String calcString = "1000000";
		for (int i = 0; i < input_split.length; i++) {
			if (input_split[i].length() != 0) {
				int num = Integer.parseInt(input_split[i]) * Integer.parseInt(calcString);
				if (calcString.charAt(calcString.length() - 1) == '0') {
					calcString = calcString.substring(0, calcString.length() - 1);
				}
				output += num;
			}
		}

		return output;
	}
}
