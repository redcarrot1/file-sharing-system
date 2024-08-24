package site.ithinkso.file_sharing_system.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FileUploadResponse {

    private List<String> success;
    private List<String> fail;


}
