package bg.softuni.fitcom.models.binding;

import javax.validation.constraints.NotBlank;

public class CommentAddBingingModel {
    @NotBlank(message = "You cannot post an empty comment!")
    private String textContent;

    public String getTextContent() {
        return textContent;
    }

    public CommentAddBingingModel setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }
}
