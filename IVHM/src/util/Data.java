package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
	// public static final String DATA_HORA_PADRAO = "dd/MM/yyyy hh:mm:ss";
	public static final String DATA_HORA_PADRAO = "MM/dd/yyyy hh:mm";

	public static Date toDate(String p_Data, String p_Padrao) {
		Date DataAux = new Date();
		SimpleDateFormat FormataDT = new SimpleDateFormat(p_Padrao);
		try {
			DataAux = FormataDT.parse(p_Data);
		} catch (java.text.ParseException E) {
			return null;
		}
		return DataAux;
	}

	/**
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 */
	public static int calculaDiasDiferencaEntreDatas(Date p_dataInicial, Date p_dataFinal) {
		long DAY = 24L * 60L * 60L * 1000L;
		int dif = (int) ((p_dataInicial.getTime() - p_dataFinal.getTime()) / DAY);
		return dif;
	}
}
