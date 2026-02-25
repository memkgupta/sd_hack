public class BalanceEntry {
    private String from;
    private String to;
    private double amount;
    public BalanceEntry()
    {
    }
    public BalanceEntry(String from, String to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BalanceEntry(BalanceEntry entry) {
        this.from = entry.getFrom();
        this.to = entry.getTo();
        this.amount = entry.getAmount();
    }
}
