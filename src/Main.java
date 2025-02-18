
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //определение курсов валют и  инициализация баласа
    public static void main(String[] args) {

        ArrayList<Double> balance = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            balance.add(0.00);
        }
        boolean nachalo = true;

        Scanner vvod = new Scanner(System.in);

        while (true) {
            if (nachalo) {

                System.out.println("\n              Добро пожаловать в консоль банкомата!");
                nachalo = false;
            }

            System.out.println("Доступны следующие операции: 'Внести' 'Снять' 'Баланс' 'Обменять' 'Выход'");
            System.out.print("Напишите необходимую операцию:    ");
            String vybor = vvod.nextLine().trim();
            if (vybor.equalsIgnoreCase("Выход")) {
                System.out.println("Спасибо, что воспользовались нашим банкоматом!");
                System.out.println("Возвращайтесь поскорее;)");
                break;
            }

            switch (vybor.toLowerCase()) {

                case "внести":
                    System.out.println("---------------------");
                    Object[] depositResult = controlFormat(vvod);
                    if (depositResult != null) {
                        double value1 = (double) depositResult[0]; // Извлекаем parts
                        int valutaNumber = (int) depositResult[1];    // Извлекаем valutaNumber
                        deposit(value1, valutaNumber, balance); // Передаем данные в deposit
                    }
                    break;

                case "снять":
                    System.out.println("---------------------");
                    Object[] withdraftResult = controlFormat(vvod);
                    if (withdraftResult != null) {
                        double value1 = (double) withdraftResult[0];
                        int valutaNumber = (int) withdraftResult[1];
                        withdraft(value1, valutaNumber, balance);
                        System.out.println("Вам успешно выдано: " + value1 + " " + getValuty().get(valutaNumber));
                    }
                    break;
                case "баланс":
                    System.out.println("---------------------");
                    balances(balance);
                    System.out.println("---------------------");
                    break;
                case "обменять":
                    System.out.println("---------------------");
                    Object[] changeResult1 = controlFormat(vvod);
                    double money;

                    if (changeResult1 != null) {
                        double value1 = (double) changeResult1[0]; // Извлекаем parts
                        int valutaNumber1 = (int) changeResult1[1];    // Извлекаем valutaNumber
                        withdraft(value1, valutaNumber1, balance); // Передаем данные в change
                        money = getKurs().get(valutaNumber1)*value1;
                        int changeResult2 = changeValuta(vvod);
                        if (changeResult2>=0) {
                            double kurs2= getKurs().get(changeResult2);

                            deposit(money/kurs2,changeResult2, balance);
                        }
                    }

                    break;
                default:
                    System.out.println("--------------------------------------------");
                    System.out.println("Вы ввели неверную операцию, попробуйте снова");
                    System.out.println("--------------------------------------------");
            }

        }

        vvod.close();
    }

    //определение курсов валют и  инициализация баласа
    protected static ArrayList<String> getValuty() {
        ArrayList<String> valuty = new ArrayList<>();
        valuty.add("USD");
        valuty.add("CNY");
        valuty.add("KZT");
        valuty.add("TRY");
        valuty.add("RUB");
        return valuty;
    }

    protected static ArrayList<Double> getKurs() {
        ArrayList<Double> kursy = new ArrayList<>();
        kursy.add(91.45);
        kursy.add(11.91);
        kursy.add(0.19);
        kursy.add(2.69);
        kursy.add(1.00);
        return kursy;
    }

    public static void balances(ArrayList<Double> balance) {
        // проверка состояний
        System.out.println("Ваш баланс счета следующий:");
        for (int i = 0; i < getValuty().size(); i++) {
            double roundedBalance = Math.round(balance.get(i) * 100.0) / 100.0;
            if (i!=getValuty().size()-1){
                System.out.print(getValuty().get(i) + ":" + roundedBalance+", ");
            } else {
                System.out.print(getValuty().get(i) + ":" + roundedBalance+"\n");
            }
        }
    }

    public static Object[] controlFormat(Scanner vvod) {
        System.out.print("Операция доступна по следющим валютам: ");
        for (int i = 0; i < getValuty().size(); i++) {
            System.out.print(getValuty().get(i) + " ");
        }
        System.out.println("\nУкажите сумму и валюту (например: 320.53 RUB):  ");
        String input = vvod.nextLine().trim();
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            System.out.println("Ошибка ввода. Попробуйте снова.");
            System.out.println("-------------------------------");
            return null; // Возвращаем null в случае ошибки
        }
        int valutaNumber = -1;

        for (int i = 0; i < getValuty().size(); i++) {
            if (getValuty().get(i).equalsIgnoreCase(parts[1])) {
                valutaNumber = i;
                break;
            }
        }
        if (valutaNumber >= 0) {
            try {
                Double.parseDouble(parts[0]); // Проверяем, что сумма введена корректно
                Double value1 = Double.parseDouble(parts[0]);
                return new Object[]{value1, valutaNumber}; // Возвращаем массив с parts и valutaNumber
            } catch (NumberFormatException e) {
                System.err.println("Формат суммы указан не верно");
                System.out.println("----------------------------");
                return null;
            }
        } else {
            System.out.println("Валюта не найдена. Попробуйте снова.");
            System.out.println("------------------------------------");
            return null;
        }
    }

    public static void deposit(Double value1, int valutaNumber, ArrayList<Double> balance) {

        double value2 = balance.get(valutaNumber);

        value1 = value1 * 100;
        Integer integerNumber = value1.intValue();
        value1 = (double) integerNumber / 100;

        balance.set(valutaNumber, value2 + value1);
        System.out.println("Ваш баланс успешно пополнен на " + value1 + " " + getValuty().get(valutaNumber));
        System.out.println("---------------------");
    }

    public static void withdraft(double value1, int valutaNumber, ArrayList<Double> balance) {

        double value2 = balance.get(valutaNumber);
        if (value2 >= value1) {
            balance.set(valutaNumber, value2 - value1);
           // System.out.println("Вам успешно выдано: " + value1 + " " + getValuty().get(valutaNumber));
        } else {
            System.out.println("На вашем балансе недостаточно средств, операция отменена!");
        }
        System.out.println("---------------------");
    }

    public static int changeValuta(Scanner vvod) {
        System.out.print("Доступные валюты для обмена: ");
        for (int i = 0; i < getValuty().size(); i++) {
            System.out.print(getValuty().get(i) + " ");
        }
        System.out.println("\nУкажите валюту которую хотите получить (например: TRY):  ");
        String input = vvod.nextLine().trim();
        String[] parts = input.split(" ");
        if (parts.length != 1) {
            System.out.println("Ошибка  ввода. Попробуйте снова.");
            System.out.println("-------------------------------");
            return -1; // Возвращаем null в случае ошибки
        }
        int valutaNumber = -1;

        for (int i = 0; i < getValuty().size(); i++) {
            if (getValuty().get(i).equalsIgnoreCase(parts[0])) {
                valutaNumber = i;
                break;
            }
        }
        if (valutaNumber >= 0) {
            return valutaNumber;

        } else {
            System.out.println("Валюта не найдена. Попробуйте снова.");
            System.out.println("------------------------------------");
            return -1;
        }
    }

}
