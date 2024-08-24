package site.ithinkso.file_sharing_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import site.ithinkso.file_sharing_system.controller.response.DataListResponse;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.domain.FileEntity;
import site.ithinkso.file_sharing_system.service.DirectoryService;
import site.ithinkso.file_sharing_system.service.FileDownloadService;
import site.ithinkso.file_sharing_system.service.FileUploadService;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService uploadService;
    private final DirectoryService directoryService;
    private final FileDownloadService downloadService;

    @GetMapping
    public String findData(@RequestParam(value = "path", defaultValue = "/") String path, Model model) {
        DirectoryEntity directory = directoryService.findDirectory(path);

        DataListResponse data = new DataListResponse(directory);
        model.addAttribute("data", data);

        return "file/list";
    }

    @PostMapping
    public String saveFile(@RequestParam(value = "path", defaultValue = "/") String path,
                           @RequestParam("files") List<MultipartFile> files) {
        uploadService.saveFiles(path, files);
        return "redirect:/files?path=" + path;
    }

    @GetMapping("download")
    public ResponseEntity<UrlResource> downloadFile(@RequestParam("id") String id) throws MalformedURLException {
        FileEntity fileEntity = downloadService.findById(id);
        UrlResource resource = new UrlResource("file:" + fileEntity.getStoreFullPath());

        String encodedUploadFileName = UriUtils.encode(fileEntity.getName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}