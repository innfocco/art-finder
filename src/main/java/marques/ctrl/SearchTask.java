package marques.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SearchTask extends Service<List<CardDTO>>{

	private static final String SUFIX = ".full.jpg";
	
	private List<File> imagesInDirectoryCache;
	private File imageDirectory;
	private List<CardDTO> list;
	
	public SearchTask(File imageDirectory, List<CardDTO> list){
		this.imageDirectory = imageDirectory;
		this.list = list;
	}
	
	private List<CardDTO> go() throws Exception {
		createIndex();
		heavySearch();
		return list;
	}
	
	
	private void heavySearch() {
		for (CardDTO card : list) {
			String fileName = card.getName() + SUFIX;
			for (File f : imagesInDirectoryCache) {
				if (f.getAbsolutePath().endsWith(fileName)) {
					card.setImageFile(f);
					System.out.println(card);
					break;
				}
			}
		}
	}

	private void createIndex() {
		imagesInDirectoryCache = new ArrayList<File>();
		for(File f : imageDirectory.listFiles()){
			if(f.isDirectory()){
				indexDir(f);
			} else if(f.getAbsolutePath().endsWith(SUFIX)){
				imagesInDirectoryCache.add(f);
			}
		}
	}
	
	private void indexDir(File dir) {
		for(File f : dir.listFiles()){
			if(f.isDirectory()){
				indexDir(f);
			} else if(f.getAbsolutePath().endsWith(SUFIX)){
				imagesInDirectoryCache.add(f);
			}
		}
	}

	@Override
	protected Task<List<CardDTO>> createTask() {
		return new Task<List<CardDTO>>() {
			@Override
			protected List<CardDTO> call() throws Exception {
				return go();
			}
			
		};
	}
	
}