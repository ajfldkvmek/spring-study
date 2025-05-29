package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.file.FileStore;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;
    // application.properties 에 설정한 파일 경로를 가져옴( spring 의 어노테이션 import )
    @Value("${file.dir}")
    private String fileDir;

    public SpringUploadController(ItemRepository itemRepository, FileStore fileStore) {
        this.itemRepository = itemRepository;
        this.fileStore = fileStore;
    }

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(
            @RequestParam String itemName,
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        log.info("request: {}", request);
        log.info("itemName: {}", itemName);
        log.info("MultipartFile: {}", file);

        if (!file.isEmpty()) {
            //getOriginalFilename() : spring 제공 메소드
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath: {}", fullPath);
            file.transferTo(new File(fullPath));
        }
        return "upload-form";
    }



}
