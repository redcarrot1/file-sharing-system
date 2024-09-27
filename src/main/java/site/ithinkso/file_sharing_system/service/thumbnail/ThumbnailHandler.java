package site.ithinkso.file_sharing_system.service.thumbnail;

import java.io.File;
import java.io.IOException;

public interface ThumbnailHandler {

    boolean supported(String fileExtension);

    String generateThumbnail(File file) throws IOException;

}
