package droplet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

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
    @FXML private Label completionLabel;
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
        File chosenFile = overlayFile = fileChooser.showOpenDialog(node.getScene().getWindow());
        
        if(chosenFile != null) {
            overlayNameLabel.setText(overlayFile.getName());
            overlayFile = chosenFile;
        } else {
            overlayNameLabel.setText("No Overlay Selected");
            overlayFile = null;
        }
    }
    
    @FXML
    private void handleProcessFilesClick(ActionEvent event) {
        if(fileList != null) {
            noneSelectedLabel.setVisible(false);

            if(overlayFile != null) {
                String outputFolderPath = System.getProperty("user.home") + "/Desktop/" + "processed_files/";
                File outputFolder = new File(outputFolderPath);
                outputFolder.mkdirs();
                
                fileList.stream()
                    .forEach((File file) -> {
                        DropletImage dropletImage = new DropletImage(file);
                        dropletImage.setOverlayImage(overlayFile);

                        try {
                            String fileName = UUID.randomUUID().toString().substring(0, 8) + ".png";
                            ImageIO.write(dropletImage.getTransformedImage(), "PNG", new File(outputFolder.getAbsolutePath() + "/" + fileName));
                        } catch (IOException ex) {
                            Logger.getLogger(DropletController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                completionLabel.setVisible(true);

                        
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