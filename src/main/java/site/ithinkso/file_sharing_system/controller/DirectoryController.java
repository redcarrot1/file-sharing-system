package site.ithinkso.file_sharing_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.ithinkso.file_sharing_system.service.DirectoryService;

@Controller
@RequestMapping("directories")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping
    public String createDirectory(@RequestParam(value = "path", defaultValue = "/") String path,
                                  @RequestParam(value = "name") String name) {
        directoryService.createDirectory(path, name);
        return "redirect:/files?path=" + path;
    }

}