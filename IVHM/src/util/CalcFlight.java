package util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import commons.Flight;

public class CalcFlight {
	// implementar regra do calculo
	public static boolean isMaiorTAT(Date p_inicio, Date p_fim) {
		return Data.calculaDiasDiferencaEntreDatas(p_fim, p_inicio) > 40;
	}

	public static void ordenaPorData(List<Flight> listaFlight) {
		Collections.sort(listaFlight, new Comparator<Flight>() {
			@Override
			public int compare(Flight f1, Flight f2) {
				return f1.getM_dataEtd().compareTo(f2.getM_dataEtd());
			}
		});
	}
}
