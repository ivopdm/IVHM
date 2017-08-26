package util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import commons.Flight;

public class CalcFlight {
	
	/**
	 * @param ETD
	 * @param ETA
	 * @return
	 */
	public static boolean isMaiorTAT(Date ETD, Date ETA) {
		return Data.calculaDiasDiferencaEntreDatas(ETD, ETA) > 40;
	}

	/**
	 * @param listaFlight
	 */
	public static void ordenaPorData(List<Flight> listaFlight) {
		Collections.sort(listaFlight, new Comparator<Flight>() {
			@Override
			public int compare(Flight f1, Flight f2) {
				return f1.getM_dataEtd().compareTo(f2.getM_dataEtd());
			}
		});
	}
}
