package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.CloudinaryImageServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    CloudinaryImageServiceModel upload(MultipartFile file) throws IOException;


}
