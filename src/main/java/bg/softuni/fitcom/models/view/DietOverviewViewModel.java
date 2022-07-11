package bg.softuni.fitcom.models.view;

public class DietOverviewViewModel {
    private long id;
    private String title;
    private String description;
    private String author;
    private String created;

    public String getTitle() {
        return title;
    }

    public DietOverviewViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DietOverviewViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DietOverviewViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public long getId() {
        return id;
    }

    public DietOverviewViewModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public DietOverviewViewModel setCreated(String created) {
        this.created = created;
        return this;
    }
}
