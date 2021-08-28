import java.util.ArrayList;
import java.util.Date;

public class Transaction {

    private double amount; // Сумма транзакции
    private Date timestape; // Время и дата транзакции
    private String memo; // Запись этой транзакции
    private Account inAccount; // Аккаунт с кторого проводилась транзакция

    public Transaction(double amount, Account inAccount){

        this.amount = amount;
        this.inAccount = inAccount;
        this.timestape = new Date();
        this.memo = "";

    }
    // Переопределение метода транзакций
    public Transaction(double amount, String memo, Account inAccount){

        // Использование параметров в первом конструкторе
        this(amount, inAccount);
        this.memo = memo;
    }

    public double getAmount(){
        return this.amount;
    }

    /**
     * Получение строки обощающую все транзакции
     * @return итоговая строка
     */
    public String getSummaryLine(){
        if (this.amount >= 0){
            return String.format("%s : $%.02f : %s", this.timestape.toString(),
                    this.amount, this.memo);
        }
        else {
            return String.format("%s : $(%.02f) : %s", this.timestape.toString(),
                    this.amount, this.memo);
        }
    }

}
