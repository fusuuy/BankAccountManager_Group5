import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        Bank bank = new Bank();
        boolean running = true;

        while (running) {
            System.out.println("--- Bank Menu ---");
            System.out.println("1. Open a new account");
            System.out.println("2. List all accounts");
            System.out.println("3. Deposit by ID");
            System.out.println("4. Withdraw by ID");
            System.out.println("5. Show summary");
            System.out.println("6. Find account by ID");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = 0;

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter account type (savings/checking/credit): ");
                        String type = scanner.nextLine().trim().toLowerCase();

                        if (!type.equals("savings") && !type.equals("checking") && !type.equals("credit")) {
                            System.out.println("Unknown account type. Skipping...");
                            break;
                        }

                        System.out.print("Enter holder name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter initial balance: ");
                        double balance = scanner.nextDouble();

                        if (type.equals("savings")) {
                            System.out.print("Enter annual interest rate (e.g., 0.05): ");
                            double rate = scanner.nextDouble();
                            Account acc = new SavingsAccount(name, balance, rate);
                            bank.openAccount(acc);
                            System.out.println("Account opened. ID: " + acc.getId());
                        } else if (type.equals("checking")) {
                            System.out.print("Enter withdrawal fee: ");
                            double fee = scanner.nextDouble();
                            Account acc = new CheckingAccount(name, balance, fee);
                            bank.openAccount(acc);
                            System.out.println("Account opened. ID: " + acc.getId());
                        } else {
                            System.out.print("Enter credit limit: ");
                            double limit = scanner.nextDouble();
                            Account acc = new CreditAccount(name, balance, limit);
                            bank.openAccount(acc);
                            System.out.println("Account opened. ID: " + acc.getId());
                        }
                        break;
                    case 2:
                        bank.listAccounts();
                        break;
                    case 3:
                        System.out.print("Enter account ID: ");
                        int depId = scanner.nextInt();
                        System.out.print("Enter deposit amount: ");
                        double depAmt = scanner.nextDouble();
                        Account depAcc = bank.findById(depId);
                        if (depAcc != null) {
                            depAcc.deposit(depAmt);
                            System.out.printf("Deposit successful. New balance: %.2f TL\n", depAcc.getBalance());
                        } else {
                            System.out.println("No account found with that ID.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter account ID: ");
                        int withId = scanner.nextInt();
                        System.out.print("Enter withdrawal amount: ");
                        double withAmt = scanner.nextDouble();
                        Account withAcc = bank.findById(withId);

                        if (withAcc != null) {
                            boolean success = withAcc.withdraw(withAmt);
                            if (success) {
                                if (withAcc instanceof CreditAccount && withAcc.getBalance() < 0) {
                                    System.out.printf("Withdrawal successful. New balance: %.2f TL (Credit used)\n", withAcc.getBalance());
                                } else if (withAcc instanceof CheckingAccount) {
                                    System.out.printf("Withdrawal successful. New balance: %.2f TL (%.2f TL fee applied)\n",
                                            withAcc.getBalance(), ((CheckingAccount)withAcc).getWithdrawalFee());
                                } else {
                                    System.out.printf("Withdrawal successful. New balance: %.2f TL\n", withAcc.getBalance());
                                }
                            } else {
                                System.out.println("Withdrawal failed. Insufficient funds or invalid amount.");
                            }
                        } else {
                            System.out.println("No account found with that ID.");
                        }
                        break;
                    case 5:
                        bank.printSummary();
                        break;
                    case 6:
                        System.out.print("Enter account ID: ");
                        int findId = scanner.nextInt();
                        Account foundAcc = bank.findById(findId);
                        if (foundAcc != null) {
                            System.out.println(foundAcc.toString());
                        } else {
                            System.out.println("No account found with that ID.");
                        }
                        break;
                    case 7:
                        System.out.println("Exiting. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format. Transaction cancelled.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}