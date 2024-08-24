package site.ithinkso.file_sharing_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.ithinkso.file_sharing_system.controller.response.DataListResponse;
import site.ithinkso.file_sharing_system.domain.DirectoryEntity;
import site.ithinkso.file_sharing_system.service.FileDownloadService;
import site.ithinkso.file_sharing_system.service.FileExplorerService;
import site.ithinkso.file_sharing_system.service.FileUploadService;

import java.util.List;

@Controller
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService uploadService;
    private final FileExplorerService explorerService;
    private final FileDownloadService downloadService;

    @GetMapping
    public String findData(@RequestParam(value = "path", defaultValue = "/") String path, Model model) {
        DirectoryEntity directory = explorerService.findDirectory(path);

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

}