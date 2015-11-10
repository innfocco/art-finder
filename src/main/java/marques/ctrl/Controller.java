package marques.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

public class Controller {
	
	@FXML
	private ImageView imageCard;
	
	@FXML
	private ScrollPane scrollpaneList;
	
	@FXML
	private ScrollPane scrollpaneText;
	
	@FXML
	private ListView<CardDTO> list;
	
	@FXML
	private TextArea textarea;
	
	@FXML
	private Label lbSelectedFile;
	
	@FXML
	private ProgressBar progress;
	
	
	private File imageDirectory;
	
	private static final DirectoryChooser FILE_CHOOSER = new DirectoryChooser();
	
	@FXML
	private void initialize() {
		initFileChooser();
	}
	
	private void initFileChooser() {
		FILE_CHOOSER.setTitle("Selecione a pasta de imagens");
	}
		
	@FXML protected void doSetFolder(ActionEvent event) {
		File selected = FILE_CHOOSER.showDialog(lbSelectedFile.getScene().getWindow());
		if(selected != null){
			imageDirectory = selected;
			lbSelectedFile.setText("Usando: " + imageDirectory.getAbsolutePath());
		}
	}
	
	@FXML protected void doEdit(ActionEvent event) {
		list.getItems().clear();
		scrollpaneText.setVisible(true);
		scrollpaneList.setVisible(false);
	}
	
	@FXML protected void doSearch(ActionEvent event) {
		final SearchTask searchTask = new SearchTask(imageDirectory, fillList()); 
		progress.visibleProperty().bind(searchTask.runningProperty());
		progress.progressProperty().unbind();
        progress.progressProperty().bind(searchTask.progressProperty());
        
        searchTask.setOnSucceeded( new EventHandler<WorkerStateEvent>(){
			public void handle(WorkerStateEvent event) {
				list.getItems().addAll(searchTask.getValue());
				scrollpaneText.setVisible(false);
				scrollpaneList.setVisible(true);
			}
        } );
        searchTask.restart();
	}

	@FXML protected void showCardImg(MouseEvent event) {
		imageCard.setImage(list.getSelectionModel().getSelectedItem().getImage());
	}
	
	private List<CardDTO> fillList() {
		List<CardDTO> cardlist = new ArrayList<CardDTO>();
		if(textarea.getText().length() > 10){
			String[] cards = textarea.getText().split("\n");
			for(String cardSt : cards){
				int amount = Integer.parseInt(cardSt.substring(0, cardSt.indexOf("x")));
				String nm = cardSt.substring(cardSt.indexOf(" ")+1);
				CardDTO card = new CardDTO(amount, nm);
				cardlist.add(card);
			}
			sort(cardlist);
		}
		return cardlist;
	}

	private void sort(List<CardDTO> cardlist) {
		Collections.sort(cardlist, new Comparator<CardDTO>() {
			public int compare(CardDTO o1, CardDTO o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});		
	}

	@FXML protected void doSaveDeck(ActionEvent event) {
		File selected = FILE_CHOOSER.showDialog(lbSelectedFile.getScene().getWindow());
		if(selected != null){
			String root = selected.getAbsolutePath() + "" + File.separatorChar;
			for(CardDTO card : list.getItems()){
				try {
					saveFile(card.getImageFile(), new File(root+card.getName()+".jpg"));
					success();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void success() {
		Alert suc = new Alert(AlertType.INFORMATION);
		suc.setTitle("Sucesso!");
		suc.setHeaderText("Deck Criado!");
		suc.setContentText("Deck criado com sucesso!!");
		suc.showAndWait();
	}

	private void saveFile(File in, File out) throws IOException {
		FileInputStream inStream = new FileInputStream(in);
		FileOutputStream outStream = new FileOutputStream(out);
	    byte[] buffer = new byte[1024];
	    int length;
	    while ((length = inStream.read(buffer)) > 0){
	    	outStream.write(buffer, 0, length);
	    }
	    inStream.close();
	    outStream.close();
	}

	@FXML protected void about(ActionEvent event) {
		
	}
	
	

}
