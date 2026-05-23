public class CheckingAccount extends Account {
    private double withdrawalFee;

    public CheckingAccount(String holderName, double balance, double withdrawalFee) {
        super(holderName, balance);
        this.withdrawalFee = withdrawalFee;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (amount + withdrawalFee) <= balance) {
            balance -= (amount + withdrawalFee);
            return true;
        }
        return false;
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    public double getWithdrawalFee() {
        return withdrawalFee;
    }
}