/**
 * 
 */
package app;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.wrapper.*;
/**
 * @author IMEDEIRO
 * 
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		// Get a hold on JADE runtime
		Runtime rt = Runtime.instance();

		// Exit the JVM when there are no more containers around
		rt.setCloseVM(true);

		// Launch a complete platform on the 8888 port
		// create a default Profile 
		Profile pMain = new ProfileImpl(null, 8888, null);

		System.out.println("Launching a whole in-process platform..."+pMain);
		AgentContainer mc = rt.createMainContainer(pMain);

		// set now the default Profile to start a container
//		ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
//		System.out.println("Launching the agent container ..."+pContainer);
//		AgentContainer cont = rt.createAgentContainer(pContainer);
//		System.out.println("Launching the agent container after ..."+pContainer);

		System.out.println("Launching the rma agent on the main container ...");
		AgentController rma;
		AgentController sniffer;
		AgentController introspector;
		
		try {
			rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]); 
			rma.start();
			
			//sniffer = mc.createNewAgent("sniffer","jade.tools.sniffer.Sniffer", new Object[0]);
			//sniffer.start();
			
			introspector = mc.createNewAgent("introspector", "jade.tools.introspector.Introspector", new Object[0]);
			introspector.start();
			
			AgentController acft_1 = 
				mc.createNewAgent("ACFT_1", agents.AircraftAgent.class.getName(), new Object[]{new Double(2.0)} );
			
			AgentController acft_2 = 
					mc.createNewAgent("ACFT_2", agents.AircraftAgent.class.getName(), new Object[]{new Double(4.0)} );
			
			AgentController acft_3 = 
					mc.createNewAgent("ACFT_3", agents.AircraftAgent.class.getName(), new Object[]{new Double(1.0)} );
			
			AgentController tas = mc.createNewAgent("TAS", agents.TasAgent.class.getName(), new Object[0]);
			
			acft_1.start();
			acft_2.start();
			acft_3.start();
			tas.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

}
