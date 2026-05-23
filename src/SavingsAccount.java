public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String holderName, double balance, double interestRate) {
        super(holderName, balance);
        this.interestRate = interestRate;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void addInterest() {
        balance += balance * interestRate;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}