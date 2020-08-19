package me.ljseokd.basicboard.modules.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attacheFile")
public class AttacheFileController {

    private final AttacheFileService attacheFileService;

    @PostMapping("/{fileId}/remove")
    public ResponseEntity deleteAttacheFile(@PathVariable Long fileId){
        attacheFileService.removeFile(fileId);
        return ResponseEntity.ok().build();
    }

}

