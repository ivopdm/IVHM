package behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import agents.TasAgent;

public class RequestSolution extends OneShotBehaviour {
    private static final long serialVersionUID = -4627972467250828190L;               
    private final Logger logger = Logger.getMyLogger(getClass().getName());   

    @Override
    public void action() {
        
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        cfp.addReceiver(new AID(TasAgent.AGENT_NAME, AID.ISLOCALNAME));
        
        myAgent.send(cfp);
        
        logger.info("Sent CFP message to Reasoners");        
                
    }    

}
