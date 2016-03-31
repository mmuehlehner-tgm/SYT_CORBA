package cb;

import org.omg.PortableServer.POAHelper;
import java.util.Properties;
import org.omg.CORBA.Any;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.Object;

public class client extends CallBackPOA
{
	public client()
	{
	}

	public void call_back(String mesg)
	{
		System.out.println("Received message: "+mesg);
	}

	public static void main (String[] args) throws Exception
   	{
	  try
	  {
		ORB orb = ORB.init(args,null);
		org.omg.CORBA.Object o = orb.resolve_initial_references("NameService");
		NamingContextExt rootContext = NamingContextExtHelper.narrow(o);
		NameComponent[] name = new NameComponent[2];
		name[0] = new NameComponent("test","my_context");
		name[1] = new NameComponent("Echo", "Object");
		Server server = ServerHelper.narrow(rootContext.resolve(name));
		client c=new client();
		POA root_poa = POAHelper.narrow(orb.resolve_initial_references ("RootPOA"));
		root_poa.the_POAManager().activate();
		CallBack cb = CallBackHelper.narrow(root_poa.servant_to_reference(c));
		System.out.println("Calling method one_time on server...");
		server.one_time(cb,"Hello!");
		System.out.println("Calling methode _cxx_register on server...");
		server.register(cb,"Hey!",(short)1);
		while(true); //hält den Client am Leben (keine gute Lösung, nur als Test)
	  }
	  catch(Exception e)
	  {
		System.err.println("Es ist ein Fehler aufgetreten: " + e.getMessage());
		e.printStackTrace();
	  }
	}
}

