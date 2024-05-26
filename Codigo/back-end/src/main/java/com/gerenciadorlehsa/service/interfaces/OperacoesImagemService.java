package com.gerenciadorlehsa.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OperacoesImagemService {

    byte[] getImage(String imageName) throws IOException;

    String saveImageToStorage(MultipartFile imageFile) throws IOException;

    String deleteImage(String imageName) throws IOException;


}
