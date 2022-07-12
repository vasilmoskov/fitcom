package bg.softuni.fitcom.models.view;

public class TrainingProgramsOverviewViewModel {
    private long id;
    private String title;
    private String description;
    private String pictureUrl;
    private String author;
    private String created;

    public TrainingProgramsOverviewViewModel() {
    }

    public long getId() {
        return id;
    }

    public TrainingProgramsOverviewViewModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TrainingProgramsOverviewViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public TrainingProgramsOverviewViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public TrainingProgramsOverviewViewModel setCreated(String created) {
        this.created = created;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TrainingProgramsOverviewViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public TrainingProgramsOverviewViewModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}
