package bg.softuni.fitcom.models.view;

public class ExerciseDetailsViewModel {
    private String name;
    private String description;
    private String videoUrl;

    public String getName() {
        return name;
    }

    public ExerciseDetailsViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExerciseDetailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ExerciseDetailsViewModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
