package commons;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jade.util.Logger;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class CarregarDadosExcel {

	private final Logger logger = Logger.getMyLogger(getClass().getName());
	// public static final String DATA_HORA_PADRAO = "dd/MM/yyyy hh:mm:ss";
	public static final String DATA_HORA_PADRAO = "MM/dd/yyyy hh:mm";

	public List<Aircraft> montarListaAvioes(String nomeTalela) {
		List<Aircraft> listaAvioes = new java.util.ArrayList<Aircraft>();
		String v_caminho = "./src/resources/data/" + nomeTalela;
		// objeto relativo ao arquivo excel
		Workbook workbook = null;
		try {
			// Carrega planilha
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");// configura acentua��o
			// recupera arquivo desejado
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			// recupera pagina/planilha/aba do arquivo
			Sheet sheet = workbook.getSheet(0);
			// recupera numero de linhas
			int linhas = sheet.getRows();
			// percorre todas as linhas da planilha
			Integer compara = 0;
			// Random gerador = new Random();
			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					// verifica se coluna 0 (A) e linha row n�o � vazia
					if (!sheet.getCell(7, row).getContents().isEmpty()) {
						if (Integer.valueOf(sheet.getCell(7, row).getContents()) != compara) {
							Aircraft aviao = new Aircraft();
							// recupera informa��o da coluna A linha row.
							aviao.setId(Long.valueOf(sheet.getCell(7, row).getContents().toString()));
							compara = Integer.valueOf(sheet.getCell(7, row).getContents());
							if (!sheet.getCell(6, row).getContents().isEmpty()) {
								// recupera informa��o da coluna B linha
								// row.
								aviao.setNome(sheet.getCell(6, row).getContents().toString());
							}
							// Double valor = Math.random() + 1;
							// valor = Double.valueOf(String.format(Locale.US,
							// "%.2f", valor));
							Double valor = 0.05 * Math.random() + 1;
							valor = Double.valueOf(String.format(Locale.US, "%.4f", valor));
							aviao.setFator(valor);
							aviao.setCurrLoc(sheet.getCell(2, row).getContents().toString());
							listaAvioes.add(aviao);

						}
					}
				}

			}

		} catch (IOException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (BiffException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (NumberFormatException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			// fechar
			if (workbook != null)
				workbook.close();
		}
		return listaAvioes;
	}

	public List<Flight> montarListaFlights(String nomeTalela) {
		List<Flight> listaFlights = new java.util.ArrayList<Flight>();
		String v_caminho = "./src/resources/data/" + nomeTalela;
		// objeto relativo ao arquivo excel
		Workbook workbook = null;
		try {
			// Carrega planilha
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");// configura acentua��o
			// recupera arquivo desejado
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			// recupera pagina/planilha/aba do arquivo
			Sheet sheet = workbook.getSheet(0);
			// recupera numero de linhas
			int linhas = sheet.getRows();
			// percorre todas as linhas da planilha
			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					Flight flight = new Flight();
					if (!sheet.getCell(1, row).getContents().isEmpty()) {
						flight.setM_FlightID(sheet.getCell(1, row).getContents().toString());
					}
					if (!sheet.getCell(2, row).getContents().isEmpty()) {
						flight.setM_origem(sheet.getCell(2, row).getContents().toString());
					}
					if (!sheet.getCell(3, row).getContents().isEmpty()) {
						flight.setM_destino(sheet.getCell(3, row).getContents().toString());
					}
					if (!sheet.getCell(4, row).getContents().isEmpty()) {
						flight.setM_dataEtd(toDate(sheet.getCell(4, row).getContents().toString(), DATA_HORA_PADRAO));
					}
					if (!sheet.getCell(5, row).getContents().isEmpty()) {
						flight.setM_dataEta(toDate(sheet.getCell(5, row).getContents().toString(), DATA_HORA_PADRAO));
					}
					Double valorFuel = Math.random() * (10000.00 - 5000.00) + 5000.00;
					valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
					flight.setM_fuelKG(valorFuel);

					Double flightValue = Math.random() * (50000.00 - 10000.00) + 5000.00;
					flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
					flight.setM_flightValue(flightValue);

					listaFlights.add(flight);
				}
			}

		} catch (IOException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (BiffException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (NumberFormatException e) {
			logger.info("Erro: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Erro: " + e.getMessage());
		} finally {
			// fechar
			if (workbook != null)
				workbook.close();
		}
		return listaFlights;
	}

	public static Date toDate(String Data, String Padrao) {
		Date DataAux = new Date();
		SimpleDateFormat FormataDT = new SimpleDateFormat(Padrao);
		try {
			DataAux = FormataDT.parse(Data);
		} catch (java.text.ParseException E) {
			return null;
		}
		return DataAux;
	}
}
