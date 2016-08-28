package KingsLand.server;

import java.util.HashMap;
import java.util.Map.Entry;

public class Helper
{
	public static HashMap<String, String> Resolvers = new HashMap<String, String>();
	
	public static void AddSolver(String from, String to, String prefix)
	{
		if(prefix == null) {prefix = "%";}
		Resolvers.put(prefix+from+prefix, to);
	}

	public String Solver(String text)
	{
		for(Entry<String, String> map : Resolvers.entrySet())
		{
			text = text.replace(map.getKey(), map.getValue());
		}
		return text;
	}
}
