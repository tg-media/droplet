/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droplet;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 *
 * @author glcheetham
 */
public class DropletController implements Initializable {
    
    FileChooser fileChooser;
    List<File> fileList;
    
    @FXML
    private BorderPane mainPane;
        
    @FXML
    private void handleChooseFileClick(ActionEvent event) {
        Node node = (Node) event.getSource();
        fileList = fileChooser.showOpenMultipleDialog(node.getScene().getWindow());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Files");
    }    
    
}
