package droplet;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

/**
 *
 * @author glcheetham
 */

public class DropletImage {
    
    private BufferedImage baseImage;
    private BufferedImage overlayImage;
    
    public DropletImage(File baseFile) {
        try {
            this.baseImage = ImageIO.read(baseFile);
        } catch (IOException ex) {
            Logger.getLogger(DropletImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setOverlayImage(File overlayImage) {
        try {
            this.overlayImage = ImageIO.read(overlayImage);
        } catch (IOException ex) {
            Logger.getLogger(DropletImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getTransformedImage() {
        BufferedImage resizedImage = Scalr.resize(
            this.baseImage,
            Scalr.Method.BALANCED,
            Scalr.Mode.AUTOMATIC,
            640,
            480,
            Scalr.OP_ANTIALIAS
        );
        
        int w = Math.max(resizedImage.getWidth(), this.overlayImage.getWidth());
        int h = Math.max(resizedImage.getHeight(), this.overlayImage.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = combined.createGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.drawImage(this.overlayImage, 0, 0, null);

        return combined;
    }
    
}
