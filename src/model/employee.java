package model;

public class employee {
   private String id;
   private String name;
   private String password;
   private String gender;
   private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public employee(String id, String name, String password, String gender, String status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.status = status;
    }

    public employee() {

    }

    @Override
    public String toString() {
        return "employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
