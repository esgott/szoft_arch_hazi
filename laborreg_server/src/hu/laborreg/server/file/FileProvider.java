package hu.laborreg.server.file;

import java.io.File;

public class FileProvider {

	public File requestFile(String path, String name) {
		return new File(path, name);
	}
	
	public File requestFile(File path, String name) {
		return new File(path, name);
	}

}
