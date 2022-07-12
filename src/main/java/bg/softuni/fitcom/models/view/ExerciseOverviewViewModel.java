package bg.softuni.fitcom.models.view;

public class ExerciseOverviewViewModel {
    private long id;
    private String name;
    private String description;
    private String videoUrl;

    public ExerciseOverviewViewModel() {
    }

    public long getId() {
        return id;
    }

    public ExerciseOverviewViewModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExerciseOverviewViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExerciseOverviewViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ExerciseOverviewViewModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }
}
