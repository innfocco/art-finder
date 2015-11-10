package marques.ctrl;

import java.io.File;
import java.io.FileFilter;

public class ImageFilter implements FileFilter {

	public boolean accept(File pathname) {
		if(pathname.getAbsolutePath().endsWith(".jpg")){
			return true;
		}
		return false;
	}

}
