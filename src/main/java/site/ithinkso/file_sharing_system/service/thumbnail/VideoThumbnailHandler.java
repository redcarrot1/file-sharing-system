package site.ithinkso.file_sharing_system.service.thumbnail;

import net.coobird.thumbnailator.Thumbnails;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

@Component
public class VideoThumbnailHandler implements ThumbnailHandler {

    @Value("${file.thumbnail.dir}")
    private String thumbnailDir;

    @Value("${file.thumbnail.height}")
    private int thumbnailHeight;

    @Value("${file.thumbnail.width}")
    private int thumbnailWidth;

    private final Set<String> supportedExtensions = Set.of("mp4", "mkv", "mpeg", "wav", "mov");
    private final String outputFormat = "jpg";

    @Override
    public boolean supported(String fileExtension) {
        if (fileExtension == null) {
            return false;
        }
        return supportedExtensions.contains(fileExtension.toLowerCase());
    }

    @Override
    public String generateThumbnail(Path path) throws IOException {
        String filenameWithFormat = path.getFileName().toString();
        String filename = filenameWithFormat.split("\\.")[0];
        String fullPathName = thumbnailDir + filename + "." + outputFormat;

        try {
            FrameGrab frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(path.toFile()));

            frameGrab.seekToSecondPrecise(0);
            Picture picture = frameGrab.getNativeFrame();
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);

            Thumbnails.of(bufferedImage)
                    .outputFormat(outputFormat)
                    .size(thumbnailWidth, thumbnailHeight)
                    .toFile(fullPathName);

        } catch (JCodecException e) {
            throw new RuntimeException("Failed to generate thumbnail");
        }

        return fullPathName;
    }

}
