package hu.laborreg.server.file;

import java.io.File;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;

public class FileProvider {

	public File requestFile(String path, String name) {
		return new File(path, name);
	}
	
	public File requestFile(File path, String name) {
		return new File(path, name);
	}
	
	public FileEntity createFileEntity(File file) {
		return new FileEntity(file, ContentType.create("text/html"));
	}

}
