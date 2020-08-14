package me.ljseokd.basicboard.modules.file.dto;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

import java.io.File;

@Data
@RequiredArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class AttacheFileDto {

    private File file;
    private InputStreamResource resource;
    private MediaType mediaType;
    private String fileName;
}
