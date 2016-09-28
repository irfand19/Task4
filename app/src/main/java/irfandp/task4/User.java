package irfandp.task4;

/**
 * Created by User on 9/28/2016.
 */
public class User {

    public User(String description, String amount) {
        this.description = description;
        this.amount = amount;
    }

    private int id;

    private String description;

    private String amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
