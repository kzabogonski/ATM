import java.util.Scanner;
import java.util.SortedMap;

public class ATM {

    public static void main(String[] args) {

        // Инициализируем сканер
        Scanner sc = new Scanner(System.in);

        //Создаем банк
        Bank theBank = new Bank("Банк бедных студентов");

        //Добавим пользователя, у которого будет сберегательный счёт
        User aUser = theBank.addUser("Кирилл", "Забогонский", "8520");

        // Добавим учетную запись для нашего пользователя
        Account newAccount = new Account("Счёт", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true){

            // Остаёмся в авторизации пока не авторизуемся
            curUser = ATM.mainMenuPromt (theBank, sc);

            //Остаемся в меню, пока пользователь не выйдет
            ATM.printUserMenu(curUser, sc);
        }
    }

    /**
     *
     * @param theBank Имя банка пользователя
     * @param sc сканер
     * @return возращение того что пользователь вошел
     */
    public static User mainMenuPromt(Bank theBank, Scanner sc){
        String userID;
        String pin;
        User authUser;
        // Запрашиваем у пользователя комбинацию ID пользователя и пин--кода до тех пор, пока не будет вернно
        do{
            System.out.printf("\n\nДобропожаловать в банк %s\n\n", theBank.getName());
            System.out.print("Введите ваш ID: ");
            userID = sc.nextLine();
            System.out.print("Введите пин-код: ");
            pin = sc.nextLine();

            // Пробуем получить объкт пользователя, соответсвующему ID и пин-коду
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null){
                System.out.println("Невернно введеныые данные ID или пин-кода. " +
                        "Повторите попытку снова.");
            }
        } while(authUser == null); // Продолжаем, пока неавторизуется пользователь

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc){

        // Печчатаем сводку учетных записей пользователя
        theUser.printAccountsSummary();
        int choice;

        //Пользовательское меню
        do {

            System.out.printf("Добропожаловать %s, что вы хотите сделать?\n", theUser.getFirstName());
            System.out.println("   1) Показать историю транзакций по счёту");
            System.out.println("   2) Снятие денег ");
            System.out.println("   3) Пополнение ");
            System.out.println("   4) Перевод ");
            System.out.println("   5) Выход  ");
            System.out.println();
            System.out.print("Выберите действие : ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Невернный ввод. Выберите вариант 1-5");
            }
        } while (choice < 1 || choice > 5);

        //
        switch (choice){
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5 :
                sc.nextLine();
                break;
        }
        // Повторно отображаем меню, если пользователь хочет выйти
        if (choice != 5){
            ATM.printUserMenu(theUser, sc);
        }
    }

    public static void showTransHistory(User theUser, Scanner sc){

        int theActt;

        //Получить учетную запись, историю транзакций которую нужно посмотреть
        do {
            System.out.printf("Введите номер аккаунта (1-%d), " +
                    "транзакцию которого вы хотите увидеть : ",
                     theUser.numAccounts());
            theActt = sc.nextInt() - 1;
            if (theActt < 0 || theActt >= theUser.numAccounts()){
                System.out.println("Неверный аккаунт. Попробуйте снова.");
            }
        }while(theActt < 0 || theActt >= theUser.numAccounts());

        // Печатаем список транзакций
        theUser.printAccountTransaction(theActt);
    }

    public static void transferFunds(User theUser, Scanner sc){

        int fromAcct;
        int toAcct;
        double amount;
        double acctBalance;

        // С каого аккаунта переводим
        do{
            System.out.printf("Введите номер (1-%d) аккаунта\n" +
                    "с какого первести :", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Неверный аккаунт. Попробуйте снова.");
            }
        }while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBalance = theUser.getAccountBalance(fromAcct);

        do{
            System.out.printf("Введите номер (1-%d) аккаунта\n" +
                    "на который перевести :", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Неверный аккаунт. Попробуйте снова.");
            }
        }while(toAcct < 0 || fromAcct >= theUser.numAccounts());

        // Сколько будем переводить
        do {
            System.out.printf("Введите сумму для перевода (max $%.02f) :$", acctBalance);
            amount = sc.nextDouble();
            if (amount < 0 ) {
                System.out.println("Сумма должна быть положительной");
            } else if (amount > acctBalance) {
                System.out.printf("Сумма перевода не может быть больше $%.02f",acctBalance );
            }
        }while (amount < 0 || amount > acctBalance);
        // Делаем транзакцию

        theUser.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Перевод c аккаунт %s", theUser.getAccUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Перевод на аккаунт %s", theUser.getAccUUID(fromAcct)));
    }

    public static void withdrawFunds(User theUser, Scanner sc){
        int toAcct;
        double amount;
        double acctBalance;
        String memo;

        // С каого аккаунта переводим
        do{
            System.out.printf("Введите номер (1-%d) аккаунта\n" +
                    "с какого выводить :", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Неверный аккаунт. Попробуйте снова.");
            }
        }while(toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBalance = theUser.getAccountBalance(toAcct);
        do{
            System.out.printf("Введите колличесвто для снятия (макс $%.02f) : $", acctBalance);
            amount =sc.nextDouble();
            if (amount < 0){
                System.out.println("Сумма должна быть больше 0.");
            } else if (amount > acctBalance) {
                System.out.printf("Сумма не должна первышать сумму баланса $%.02f.", acctBalance);
            }
        }while(amount < 0 || amount > acctBalance);

        // Очиащем прошлвый ввод
        sc.nextLine();

        // Получаем дату
        System.out.println("Примечание : ");
        memo = sc.nextLine();

        //Делаем вывод денег
        theUser.addAcctTransaction(toAcct, -1*amount, memo);

    }

    public static void depositFunds(User theUser, Scanner sc){
        int toAcct;
        double amount = 0;
        String memo;

        // С каого аккаунта переводим
        do{
            System.out.printf("Введите номер (1-%d) аккаунта\n" +
                    "на который желаете пополнить :", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct > theUser.numAccounts()){
                System.out.println("Неверный аккаунт. Попробуйте снова.");
            }
        }while(toAcct < 0 || toAcct > theUser.numAccounts());

        do{
            System.out.print("Введите сумму пополнения : $");
            amount = sc.nextDouble();
            if (amount < 0){
                System.out.println("Сумма должна быть больше 0.");
            }
        }while(amount < 0);

        // Получаем дату
        System.out.println("Примечание : ");
        memo = sc.nextLine();

        //Делаем вывод денег
        theUser.addAcctTransaction(toAcct, amount, memo);
    }



}
