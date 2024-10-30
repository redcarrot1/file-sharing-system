package site.ithinkso.file_sharing_system.service.thumbnail;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

@Component
public class ImageThumbnailHandler implements ThumbnailHandler {

    @Value("${file.thumbnail.dir}")
    private String thumbnailDir;

    @Value("${file.thumbnail.height}")
    private int thumbnailHeight;

    @Value("${file.thumbnail.width}")
    private int thumbnailWidth;

    private final Set<String> supportedExtensions = Set.of("jpg", "jpeg", "png", "gif", "bmp", "tiff");
    private final Map<String, String> outputFormats = Map.of("png", "png");

    @Override
    public boolean supported(String fileExtension) {
        if (fileExtension == null) {
            return false;
        }
        return supportedExtensions.contains(fileExtension.toLowerCase());
    }

    @Override
    public String generateThumbnail(Path path) throws IOException {
        String fileNameWithFormat = path.getFileName().toString();

        String filename = fileNameWithFormat.split("\\.")[0];
        String outputFormat = getOutputFormat(fileNameWithFormat);

        String fullPathName = thumbnailDir + filename + "." + outputFormat;

        Thumbnails.of(Files.newInputStream(path))
                .size(thumbnailWidth, thumbnailHeight)
                .outputFormat(outputFormat)
                .toFile(fullPathName);

        return fullPathName;
    }

    private String getOutputFormat(String fileName) {
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        if (fileExtension == null) {
            return "jpg";
        }
        return outputFormats.getOrDefault(fileExtension.toLowerCase(), "jpg");
    }

}
