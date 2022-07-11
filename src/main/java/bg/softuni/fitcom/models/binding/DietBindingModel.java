package bg.softuni.fitcom.models.binding;

import bg.softuni.fitcom.models.enums.PurposeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DietBindingModel {
    private long id;

    @NotBlank(message = "Please give your diet a title.")
    private String title;

    @NotBlank(message = "Your diet should have a description.")
    private String description;

    @NotNull(message = "Please select the aim of your diet.")
    private PurposeEnum purpose;

    public long getId() {
        return id;
    }

    public DietBindingModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DietBindingModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DietBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public PurposeEnum getPurpose() {
        return purpose;
    }

    public DietBindingModel setPurpose(PurposeEnum purpose) {
        this.purpose = purpose;
        return this;
    }
}
