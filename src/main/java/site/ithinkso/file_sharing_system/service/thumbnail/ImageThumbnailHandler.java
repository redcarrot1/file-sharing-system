package site.ithinkso.file_sharing_system.service.thumbnail;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
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
    public String generateThumbnail(File file) throws IOException {
        String outputFormat = getOutputFormat(file);
        String filename = file.getName().split("\\.")[0];
        String fullPathName = thumbnailDir + filename + "." + outputFormat;

        Thumbnails.of(file)
                .size(thumbnailWidth, thumbnailHeight)
                .outputFormat(outputFormat)
                .toFile(new File(fullPathName));

        return fullPathName;
    }

    private String getOutputFormat(File file) {
        String fileExtension = StringUtils.getFilenameExtension(file.getName());
        if (fileExtension == null) {
            return null;
        }
        return outputFormats.getOrDefault(fileExtension.toLowerCase(), "jpg");
    }

}
