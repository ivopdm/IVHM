package commons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jade.util.Logger;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import util.Data;

public class CarregarDadosExcel {

	private final Logger logger = Logger.getMyLogger(getClass().getName());

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
					// verifica se coluna 0 (A) e linha row nao e vazia
					if (!sheet.getCell(7, row).getContents().isEmpty()) {
						if (Integer.valueOf(sheet.getCell(7, row).getContents()) != compara) {
							Aircraft aviao = new Aircraft();
							// recupera informacao da coluna A linha row.
							aviao.setId(Long.valueOf(sheet.getCell(7, row).getContents().toString()));
							compara = Integer.valueOf(sheet.getCell(7, row).getContents());
							if (!sheet.getCell(6, row).getContents().isEmpty()) {
								// recupera informacao da coluna B linha
								// row.
								aviao.setNome(sheet.getCell(6, row).getContents().toString());
							}
							// Double valor = Math.random() + 1;
							// valor = Double.valueOf(String.format(Locale.US,
							// "%.2f", valor));
							Double valor = (0.05 * Math.random()) + 1;
							valor = Double.valueOf(String.format(Locale.US, "%.4f", valor));
							aviao.setFator(valor);
							aviao.setCurrLoc(sheet.getCell(2, row).getContents().toString());
							aviao.setRoute(new ArrayList<Flight>());
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
						flight.setM_dataEtd(
								Data.toDate(sheet.getCell(4, row).getContents().toString(), Data.DATA_HORA_PADRAO));
					}
					if (!sheet.getCell(5, row).getContents().isEmpty()) {
						flight.setM_dataEta(
								Data.toDate(sheet.getCell(5, row).getContents().toString(), Data.DATA_HORA_PADRAO));
					}
					Double valorFuel = Math.random() * (1000 - 5000 + 1) + 5000;
					valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
					flight.setM_fuelKG(valorFuel);

					Double flightValue = Math.random() * (5000 - 10000 + 1) + 5000;
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

	public List<Route> montarListaRoute(String nomeTalela) {
		List<Route> listaRotas = new java.util.ArrayList<Route>();
		String v_caminho = "./src/resources/data/" + nomeTalela;
		Workbook workbook = null;
		try {
			WorkbookSettings config = new WorkbookSettings();
			config.setEncoding("Cp1252");
			workbook = Workbook.getWorkbook(new File(v_caminho), config);
			Sheet sheet = workbook.getSheet(0);
			int linhas = sheet.getRows();
			
			Integer comparaRota = 0;
			Integer id = 1;
			Route route = new Route();
						
			for (int row = 0; row < linhas; row++) {
				if (row > 0) {
					if (!sheet.getCell(7, row).getContents().isEmpty()) {
						if (Integer.valueOf(sheet.getCell(7, row).getContents()) != comparaRota) {
							if (row != 1) {
								listaRotas.add(route);
							}
							route = new Route();
							route.setM_id(Long.valueOf(id));
							route.setM_lstFlights(new ArrayList<Flight>());
							id++;
						}
						comparaRota = Integer.valueOf(sheet.getCell(7, row).getContents());
						// monta o voo
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
							flight.setM_dataEtd(
									Data.toDate(sheet.getCell(4, row).getContents().toString(), Data.DATA_HORA_PADRAO));
						}
						if (!sheet.getCell(5, row).getContents().isEmpty()) {
							flight.setM_dataEta(
									Data.toDate(sheet.getCell(5, row).getContents().toString(), Data.DATA_HORA_PADRAO));
						}
						Double valorFuel = Math.random() * (1000 - 5000 + 1) + 5000;
						valorFuel = Double.valueOf(String.format(Locale.US, "%.0f", valorFuel));
						flight.setM_fuelKG(valorFuel);
						
						route.setM_SumValue(route.getM_SumValue() + valorFuel);

						Double flightValue = Math.random() * (5000 - 10000 + 1) + 5000;
						flightValue = Double.valueOf(String.format(Locale.US, "%.0f", flightValue));
						flight.setM_flightValue(flightValue);
						
						route.setM_SumFuelKG(route.getM_SumFuelKG() + flightValue);

						route.getM_lstFlights().add(flight);
						if (linhas-1 == row) {
							listaRotas.add(route);
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
			if (workbook != null)
				workbook.close();
		}
		return listaRotas;
	}

}
