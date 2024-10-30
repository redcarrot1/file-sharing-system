package site.ithinkso.file_sharing_system.service.thumbnail;

import java.io.IOException;
import java.nio.file.Path;

public interface ThumbnailHandler {

    boolean supported(String fileExtension);

    String generateThumbnail(Path path) throws IOException;

}
