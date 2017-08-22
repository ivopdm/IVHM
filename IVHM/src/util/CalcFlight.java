package util;

import java.util.Date;

public class CalcFlight {
	// implementar regra do calculo
	public static boolean isMaiorTAT(Date p_inicio, Date p_fim) {
		return Data.calculaDiasDiferencaEntreDatas(p_fim, p_inicio) < 40;
	}
}
