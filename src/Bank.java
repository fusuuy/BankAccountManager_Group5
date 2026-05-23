import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }

    public void openAccount(Account a) {
        accounts.add(a);
    }

    public void listAccounts() {
        for (Account a : accounts) {
            System.out.println(a.toString());
        }
    }

    public Account findById(int id) {
        for (Account a : accounts) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        return total;
    }

    public void printSummary() {
        System.out.printf("Total Accounts: %d | Total Balance: %.2f TL\n", accounts.size(), getTotalBalance());
    }
}