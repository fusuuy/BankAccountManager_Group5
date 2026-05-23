public abstract class Account {
    private static int nextId = 1;

    protected int id;
    protected String holderName;
    protected double balance;

    public Account(String holderName, double balance) {
        this.id = nextId++;
        this.holderName = holderName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public abstract boolean withdraw(double amount);
    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("ID: %d | %s (%s) - Balance: %.2f TL", id, holderName, getAccountType(), balance);
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }
}