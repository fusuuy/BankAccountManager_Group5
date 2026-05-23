public class CreditAccount extends Account {
    private double creditLimit;

    public CreditAccount(String holderName, double balance, double creditLimit) {
        super(holderName, balance);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= -creditLimit) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String getAccountType() {
        return "Credit";
    }
}