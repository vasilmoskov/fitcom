package bg.softuni.fitcom.models.service;

public class CloudinaryImageServiceModel {
    private String url;
    private String publicId;

    public String getUrl() {
        return url;
    }

    public CloudinaryImageServiceModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPublicId() {
        return publicId;
    }

    public CloudinaryImageServiceModel setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }
}
