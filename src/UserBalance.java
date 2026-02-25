public class UserBalance {
    private String userId;
    private double owes;
    private double gets;
    public UserBalance(String from,  double owes, double gets) {
        this.userId = from;
        this.owes = owes;
        this.gets = gets;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getOwes() {
        return owes;
    }

    public void setOwes(double owes) {
        this.owes = owes;
    }

    public double getGets() {
        return gets;
    }

    public void setGets(double gets) {
        this.gets = gets;
    }
}
