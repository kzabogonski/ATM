import java.lang.annotation.Target;
import java.util.ArrayList;

public class Account {

    private String name; // Название аккаунта
    private double balance; // Состояния баланса
    private String uuid; // ID аккаунта
    private User holder; //Имя владельца аккаунта
    private ArrayList<Transaction> transaction; // Лист транзакции аккаунта

    public Account(String name, User holder, Bank theBank){

        this.name = name;
        this.holder = holder;

        // Получение нового идентификатора учетной запис
        this.uuid = theBank.getNewAccountUUID();

        // Создания списка транзакций
        this.transaction = new ArrayList<Transaction>();


    }

    public double getBalance(){
        double balance = 0;
        for (Transaction t : this.transaction){
            balance += t.getAmount();
        }
        return balance;
    }

    public String getUUID(){
        return this.uuid;
    }

    public String getSummaryLine(){
        // Получаем баланс на аккаунте
        double balance = this.getBalance();

        // Форматируем итоговую строку от того,
        // является ли баланс отрицательным

        if (balance >= 0) {
            return String.format("%s : $%.02f : ", this.uuid,
                    balance, this.name); // формат вывода, будет 2 знака после запятой
        }
        else {
            return String.format("%s : $(%.02f) : ", this.uuid,
                    balance, this.name);
        }
    }

    // Вывод списка транзакций
    public void printTransHistory(){
        System.out.printf("\nИстория транзакций для аккаунта %s\n", this.uuid);
        for (int t = this.transaction.size()-1; t >=0 ; t--){
            System.out.println(this.transaction.get(t).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction (double amount, String memo){
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transaction.add(newTrans);
    }
}
