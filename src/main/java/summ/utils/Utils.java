package summ.utils;

import java.util.Date;

public class Utils {
	
	public static String generateStringFormattedData() {
		return "_"+(new Date().toString()).replace("-", "_").replace(" ", "_").replace(":", "_");
	}
	
}
