package Environment;

import java.math.BigDecimal;
import java.util.Map;

public class Bartos extends LiveEnvironment {
	private static String env = "Bartos";
	
	public Bartos() {
		super(env);
	}
	
	public BigDecimal getAR() {
		return getAccountsReceivable();
	}
	
	public BigDecimal getAP() {
		return getAccountsPayable();
	}
	
	public Map<String, BigDecimal> get_GLAccountBalances(String recentPeriod, String year) {
		return getGLAccountBalances(recentPeriod, year);
	}
	
	public Double getInvValue() {
		return getInventoryValue();
	}
	
	public Map<String, String> getPeriodandYear() {
		return getPeriodAndYear();
	}
	

}