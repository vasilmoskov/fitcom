package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.models.service.CloudinaryImageServiceModel;
import bg.softuni.fitcom.services.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    private static final String TEMP_FILE = "temp-file";
    private static final String URL = "url";
    private static final String PUBLIC_ID = "public_id";

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryImageServiceModel upload(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile(TEMP_FILE, multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        try {
            Map<String, String> uploadResult = cloudinary.uploader().upload(tempFile, Map.of());

            String url = uploadResult.getOrDefault(URL, "https://previews.123rf.com/images/pavlostv/pavlostv1805/pavlostv180500401/101741080-hoppla-404-fehlerseite-nicht-gefunden-futuristisches-roboterkonzept-%E2%80%93-vektor.jpg");
            String publicId = uploadResult.getOrDefault(PUBLIC_ID, "");

            return new CloudinaryImageServiceModel()
                    .setUrl(url)
                    .setPublicId(publicId);

        } finally {
            tempFile.delete();
        }
    }
}
