package site.ithinkso.file_sharing_system.service.thumbnail;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PdfThumbnailHandler implements ThumbnailHandler {

    @Value("${file.thumbnail.dir}")
    private String thumbnailDir;

    @Value("${file.thumbnail.height}")
    private int thumbnailHeight;

    @Value("${file.thumbnail.width}")
    private int thumbnailWidth;

    private final String outputFormat = "jpg";

    @Override
    public boolean supported(String fileExtension) {
        return "pdf".equalsIgnoreCase(fileExtension);
    }

    @Override
    public String generateThumbnail(Path path) throws IOException {
        PDFRenderer pdfRenderer = createPDFRenderer(Files.newInputStream(path));
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 100, ImageType.RGB);

        String filenameWithFormat = path.getFileName().toString();
        String filename = filenameWithFormat.split("\\.")[0];
        String fullPathName = thumbnailDir + filename + "." + outputFormat;

        Thumbnails.of(bufferedImage)
                .outputFormat(outputFormat)
                .size(thumbnailWidth, thumbnailHeight)
                .toFile(fullPathName);

        return fullPathName;
    }

    private PDFRenderer createPDFRenderer(InputStream inputStream) throws IOException {
        BufferedInputStream pdf = new BufferedInputStream(inputStream);
        PDDocument pdfDoc = PDDocument.load(pdf);
        return new PDFRenderer(pdfDoc);
    }

}
