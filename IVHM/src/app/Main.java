/**
 * 
 */
package app;

import java.util.List;

import commons.Aircraft;
import commons.CarregarDadosExcel;
import commons.Flight;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.Logger;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/**
 * @author IMEDEIRO
 * 
 *
 */
public class Main {

	private final static Logger logger = Logger.getMyLogger("Main.java");
	//private static String NOMETABELA = "FlightLegs.xls";
	private static String NOMETABELA = "teste.xls";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<Aircraft> listaDeAvioes = new java.util.ArrayList<Aircraft>();
		List<Flight> listaFlights = new java.util.ArrayList<Flight>();

		// Get a hold on JADE runtime
		Runtime rt = Runtime.instance();

		// Exit the JVM when there are no more containers around
		rt.setCloseVM(true);

		// Launch a complete platform on the 8888 port
		// create a default Profile
		Profile pMain = new ProfileImpl(null, 8888, null);

		System.out.println("Launching a whole in-process platform..." + pMain);
		AgentContainer mc = rt.createMainContainer(pMain);

		System.out.println("Launching the rma agent on the main container ...");
		// AgentController rma;
		// AgentController sniffer;
		AgentController introspector;
		CarregarDadosExcel carregarDadosExcel = new CarregarDadosExcel();

		try {

			listaDeAvioes = carregarDadosExcel.montarListaAvioes(NOMETABELA);			
			listaFlights = carregarDadosExcel.montarListaFlights(NOMETABELA);

			//TODO  para teste
			//			for (Aircraft aircraft : listaDeAvioes) {							
			//				//preimiero teste
			//				//aircraft.getRoute().add(listaFlights.get(0));
			//				//segundo teste
			//				//aircraft.getRoute().add(listaFlights.get(1));
			//				//terceiro
			//				aircraft.setRoute(listaFlights);
			//				System.out.println(listaFlights.get(0).getM_origem());
			//			}

			if (listaDeAvioes != null && !listaDeAvioes.isEmpty()) {
				for (int i = 0; i < listaDeAvioes.size(); i++) {
					AgentController acft = mc.createNewAgent(listaDeAvioes.get(i).getId().toString(), agents.AircraftAgent.class.getName(),
							new Object[] { listaDeAvioes.get(i) });
					acft.start();
				}
			} else {
				logger.warning("A lista de Aircraft est� vazia.");
			}

			if (listaDeAvioes != null && !listaDeAvioes.isEmpty() && listaFlights != null && !listaFlights.isEmpty()) {
				AgentController tas = mc.createNewAgent("TAS", agents.TasAgent.class.getName(),
						new Object[] { listaFlights, listaDeAvioes });
				tas.start();
			} else {
				logger.warning("A lista de Flights est� vazia.");
			}

			// FAZER A MESMA COISA PARA O VOOS AQUI

			// rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new
			// Object[0]);
			// rma.start();

			// sniffer =
			// mc.createNewAgent("sniffer","jade.tools.sniffer.Sniffer", new
			// Object[0]);
			// sniffer.start();

			introspector = mc.createNewAgent("introspector",
					"jade.tools.introspector.Introspector", new Object[0]);
			introspector.start();


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
