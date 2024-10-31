package site.ithinkso.file_sharing_system.service.thumbnail;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class PPTXThumbnailHandler implements ThumbnailHandler {

    @Value("${file.thumbnail.dir}")
    private String thumbnailDir;

    @Value("${file.thumbnail.height}")
    private int thumbnailHeight;

    @Value("${file.thumbnail.width}")
    private int thumbnailWidth;

    private final String outputFormat = "jpg";

    @Override
    public boolean supported(String fileExtension) {
        return "pptx".equalsIgnoreCase(fileExtension);
    }

    @Override
    public String generateThumbnail(Path path) {
        String fullPathName = getOutputFullPathName(path);

        try (FileInputStream fis = new FileInputStream(path.toFile());
             XMLSlideShow ppt = new XMLSlideShow(fis)) {

            BufferedImage img = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = img.createGraphics();
            setRenderingOption(graphics);
            setBackgroundColor(graphics);
            setScale(graphics, ppt.getPageSize());
            drawFirstSlidesToGraphics(graphics, ppt);

            ImageIO.write(img, outputFormat, new File(fullPathName));

            return fullPathName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate thumbnail");
        }
    }

    private String getOutputFullPathName(Path path) {
        String filenameWithFormat = path.getFileName().toString();
        String filename = filenameWithFormat.split("\\.")[0];
        return thumbnailDir + filename + "." + outputFormat;
    }

    private void setRenderingOption(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    private void setBackgroundColor(Graphics2D graphics) {
        graphics.setPaint(Color.WHITE);
        graphics.fill(new Rectangle(0, 0, thumbnailWidth, thumbnailHeight));
    }

    private void setScale(Graphics2D graphics, Dimension pageSize) {
        double scaleX = (double) thumbnailWidth / pageSize.width;
        double scaleY = (double) thumbnailHeight / pageSize.height;
        graphics.scale(scaleX, scaleY);
    }

    private void drawFirstSlidesToGraphics(Graphics2D graphics, XMLSlideShow ppt) {
        XSLFSlide slide = ppt.getSlides().getFirst();
        slide.draw(graphics);
    }
}
