package br.dev.rx.conversormoeda;

import br.dev.rx.conversormoeda.models.ExchangeRateAPI;

import java.util.Currency;
import java.util.Scanner;

public class ConversorMoeda {
    private static final ExchangeRateAPI exchangeRateAPI = new ExchangeRateAPI();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (true) {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        convert("BRL", "USD");
                        break;
                    case 2:
                        convert("USD", "BRL");
                        break;
                    case 3:
                        convert("BRL", "EUR");
                        break;
                    case 4:
                        convert("EUR", "BRL");
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void showMenu() {
        System.out.print("""
               1. Converter Real para Dolar
               2. Converter Dolar para Real
               3. Converter Real para Euro
               4. Converter Euro para Real
               0. Sair
               \s
               Digite a opção desejada:\s""");
    }

    private static void convert(String from, String to) {
        Currency baseCurrency = Currency.getInstance(from);
        Currency rateCurrency = Currency.getInstance(to);

        System.out.printf(
                "Digite o valor em %s (%s) que deseja converter para %s (%s): ",
                baseCurrency.getDisplayName(),
                baseCurrency.getSymbol(),
                rateCurrency.getDisplayName(),
                rateCurrency.getSymbol()
        );

        double amount = scanner.nextDouble();
        scanner.nextLine();

        double convertedAmount = exchangeRateAPI.convert(amount, from, to);

        System.out.printf(
                "--------------\n%s %.2f equivalem a %s %.2f\n--------------",
                baseCurrency.getSymbol(),
                amount,
                rateCurrency.getSymbol(),
                convertedAmount
        );

        System.out.println("\nPressione \"ENTER\" para continuar...");
        scanner.nextLine();
    }
}
