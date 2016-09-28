package irfandp.task4;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 9/28/2016.
 */
public class Users {

    @SerializedName("users")
    public List<UserItem> users;
    public List<UserItem> getUsers() { return users; }
    public void setUsers(List<UserItem> users) { this.users = users; }
    public Users(List<UserItem> users) { this.users = users; }

    public class UserItem {

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
}
