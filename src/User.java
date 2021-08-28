import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.util.Map;

public class User {

    private String firstName; // Имя
    private String lastName; // Фамилия
    private String uuid; // Номер ID пользователя
    private byte pinHash[]; // Пин-код
    private ArrayList<Account> accounts; // Список учетных записей пользователя???


    public User(String firstName, String lastName, String pin, Bank theBank) { // Конструктор пользователя

        this.firstName = firstName; // Имя пользователя
        this.lastName = lastName; // Фамилия пользователя

        // Сохранение MD5-хэш, а не исходное значение, по соображениям безопасности
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Ошибка, найдено исключение");
            e.printStackTrace();
            System.exit(1);
        }

        // Получение уникального, универсального идентификатора для пользователя
        this.uuid = theBank.getNewUserUUID();

        // Создание пустого списка счетов (учетных записей)
        this.accounts = new ArrayList<Account>();

        //Вывод сообщения о регистрации
        System.out.printf("Новый пользователь %s %s с ID %s был создан.\n", lastName, firstName, this.uuid);
    }

    /**
     * Добовление аккаунта для пользователя
     * @param anAcct добавление аккаунта
     */
    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }

    public String getUUID(){
        return this.uuid;
    }

    public boolean validatePin(String qPin){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(qPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Ошибка, найдено исключение");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public void printAccountsSummary(){

        System.out.printf("\n\n%s значение баланса \n", this.firstName);
        for(int a = 0; a < this.accounts.size(); a++){
            System.out.printf("%d) %s\n", a+1,
                    this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public String getFirstName(){
        return this.firstName;
    }

    public int numAccounts(){
        return this.accounts.size();
    }

    public void printAccountTransaction(int acctIndx){

        this.accounts.get(acctIndx).printTransHistory();

    }

    public double getAccountBalance(int accIx){
        return this.accounts.get(accIx).getBalance();
    }

    public String getAccUUID(int acctIdx){
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAcctTransaction(int acctIdx, double amount,String memo){
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }

}
