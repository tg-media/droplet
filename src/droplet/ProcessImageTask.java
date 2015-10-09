/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droplet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javax.imageio.ImageIO;

/**
 *
 * @author glcheetham
 */
public class ProcessImageTask extends Task {
    private final File overlayFile;
    private final List<File> fileList;
    
    public ProcessImageTask(List fileList, File overlayFile) {
        this.fileList = fileList;
        this.overlayFile = overlayFile;
    }
    
    @Override public Void call() throws Exception {
        String outputFolderPath = System.getProperty("user.home") + "/Desktop/" + "processed_files/";
        File outputFolder = new File(outputFolderPath);
        outputFolder.mkdirs();
                
        for(int i = 0; i < this.fileList.size(); i++) {
            String fileName = UUID.randomUUID().toString().substring(0, 8) + ".png";
            processFileLater(this.fileList.get(i), this.overlayFile, new File(outputFolder.getAbsolutePath() + "/" + fileName));
            updateProgressLater(i + 1, this.fileList.size());
        }
            
        return null;
    }
    
    private void processFileLater(File file, File overlayFile, File outputFile) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                DropletImage dropletImage = new DropletImage(file);
                dropletImage.setOverlayImage(overlayFile);

                try {
                    ImageIO.write(dropletImage.getTransformedImage(), "PNG", outputFile);
                } catch (IOException ex) {
                    Logger.getLogger(DropletController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        });
    }
    
    void updateProgressLater(int completed, int total) {
        Platform.runLater(() -> {
            System.out.println(completed + "/" + total + " completed...");
            updateProgress(completed, total);
        });
    }
}
