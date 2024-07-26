package com.youth.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageUtil {


    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    // 폴더가 존재하지 않으면 생성
    public static void createDirectoryIfNotExist() {
        Path path = Paths.get(Const.UPLOAD_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("이미지 폴더 생성됨: {}", Const.UPLOAD_DIR);
            } catch (IOException e) {
                logger.error("이미지 폴더 생성 실패: {}", Const.UPLOAD_DIR, e);
            }
        }
    }

    // 파일 저장
    public static String saveFile(MultipartFile file) {
        createDirectoryIfNotExist();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            Path filePath = Paths.get(Const.UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());
            return fileName;
        } catch (IOException e) {
            logger.error("파일 저장 실패 {}", Const.UPLOAD_DIR, e);
            return null;
        }
    }
}