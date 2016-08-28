package KingsLand.server.classmanager.classes;

import KingsLand.server.classmanager.FileManager;

public class Reward extends FileManager {

	public Reward(String FileName) {
		super(FileName);
	}

	@Override
	public void SetupFile() 
	{
		AddVariable("Allow", true);
		SetVariables();
	}
}
