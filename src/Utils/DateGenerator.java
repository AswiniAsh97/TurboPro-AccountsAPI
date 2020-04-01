package Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateGenerator {
	public String getCurrentDate() {
		SimpleDateFormat formatter;
		Calendar cal; 
		
		cal = Calendar.getInstance();
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date currentDate = cal.getTime();

		String current_Date = formatter.format(currentDate);
		return current_Date;
	}
}
