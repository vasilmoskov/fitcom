package bg.softuni.fitcom.models.service;

public class ExerciseAddServiceModel {
    private String name;
    private String description;
    private String videoUrl;

    public String getName() {
        return name;
    }

    public ExerciseAddServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExerciseAddServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ExerciseAddServiceModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
