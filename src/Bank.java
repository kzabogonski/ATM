import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name; // Название банка
    private ArrayList<User> users; // Список пользователей
    private  ArrayList<Account> accounts; // Какие аккаунты есть в банке

    /**
     * Создаем новый банк с пустым лситом пользователей и счетов(аккаунтов)
     * @param name это имя банка
     */
    public Bank(String name){

        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();

    }

    public String getNewUserUUID(){

        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        // Зацикливание до тех пор пока мы не получим уникальный индентифактор
        do {

            //Генерируем номер
            uuid = "";
            for (int c = 0; c < len; c++){
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            nonUnique = false;
            // Проверка что созданный ID уникальный
            for(User u : this.users){
                if (uuid.compareTo(u.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid;
    }
    public String getNewAccountUUID(){

        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        // Зацикливание до тех пор пока мы не получим уникальный индентифактор
        do {

            //Генерируем номер
            uuid = "";
            for (int c = 0; c < len; c++){
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            nonUnique = false;
            // Проверка что созданный ID уникальный
            for(Account a : this.accounts){
                if (uuid.compareTo(a.getUUID()) == 0){
                    nonUnique = true;
                    break;
                }
            }
        } while (nonUnique);

        return uuid;
    }


    public User addUser(String firstName, String lastName, String pin){
        // Создание нового объекта пользователя и добавление его в наш список пользователей
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        //Создание сберегатеьного счёта пользователя
        Account newAccount = new Account("Сберегательный", newUser, this);
        // Добовление в списки владельцев и банков
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

        return newUser;
    }
    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }

    public User userLogin(String userID, String pin){

        //Проверка существует ли пользователь
        for (User u : this.users){

            if(u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }

        }
        //Если мы не нашли пользователя или не верный ПИН-код
        return null;
    }
    // Возращение названия банка
    public String getName(){
        return this.name;
    }
}