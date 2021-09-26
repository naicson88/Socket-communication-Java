

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Util {
	
	public static String data() {
		
		Date time = new Date();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		String dt = formatter.format(time);
		
		return dt;
		
	}
}
