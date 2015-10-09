package droplet;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 *
 * @author glcheetham
 */
public class DropletController implements Initializable {
    
    FileChooser fileChooser;
    List<File> fileList;
    File overlayFile;
    ObservableList<String> fileNameList;
    
    @FXML private BorderPane mainPane;
    @FXML private Label noneSelectedLabel;
    @FXML private Label overlayNameLabel;
    @FXML private Label progressLabel;
    @FXML private ProgressBar progressBar;
    @FXML private ListView<String> imagesListView;
        
    @FXML
    private void handleChooseFileClick(ActionEvent event) {
        Node node = (Node) event.getSource();
        fileList = fileChooser.showOpenMultipleDialog(node.getScene().getWindow());
        
        if( fileList != null ) {
            
            fileNameList = FXCollections.observableArrayList(
                    fileList.stream().map( f -> f.getName() ).collect(Collectors.toList())
            );
            imagesListView.setItems(fileNameList);
            
        }
    }
    
    @FXML
    private void handleChooseOverlayClick(ActionEvent event) {
        Node node = (Node) event.getSource();
        File chosenFile = fileChooser.showOpenDialog(node.getScene().getWindow());
        
        if(chosenFile != null) {
            overlayFile = chosenFile;
            overlayNameLabel.setText(overlayFile.getName());
        } else {
            overlayFile = null;
            overlayNameLabel.setText("No Overlay Selected");
        }
    }
    
    @FXML
    private void handleProcessFilesClick(ActionEvent event) {
        if(fileList != null) {
            noneSelectedLabel.setVisible(false);
            if(overlayFile != null) {
                Node node = (Node) event.getSource();
//                progressLabel.setVisible(true);
                ProcessImageTask task = new ProcessImageTask(fileList, overlayFile);
                progressBar.progressProperty().bind(task.progressProperty());
                
                final Thread thread = new Thread(task, "process-files");
                thread.setDaemon(true);
                thread.start();
            }
        } else {
            noneSelectedLabel.setVisible(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Files");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        
        noneSelectedLabel.setVisible(false);
    }    
    
}