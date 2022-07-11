package bg.softuni.fitcom.models.view;

public class CommentViewModel {
    private long id;
    private String author;
    private String textContent;
    private String created;

    public String getAuthor() {
        return author;
    }

    public CommentViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTextContent() {
        return textContent;
    }

    public CommentViewModel setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public CommentViewModel setCreated(String created) {
        this.created = created;
        return this;
    }

    public long getId() {
        return id;
    }

    public CommentViewModel setId(long id) {
        this.id = id;
        return this;
    }
}
