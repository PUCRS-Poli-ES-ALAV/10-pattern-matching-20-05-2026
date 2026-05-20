import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final String ALFANUMERICO = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random GERADOR = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha o exercicio:");
            System.out.println("1 - Exercicio 1 (KMP)");
            System.out.println("2 - Exercicio 2 (Rabin-Karp)");
            System.out.println("3 - Exercicio 3 (KMP LPS)");
            System.out.println("4 - Comparar os tres exercicios");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            String opcao = scanner.nextLine().trim();
            System.out.println();

            switch (opcao) {
                case "1":
                    Exercicio1.executar();
                    break;
                case "2":
                    Exercicio2.executar();
                    break;
                case "3":
                    Exercicio3.executar();
                    break;
                case "4":
                    executarComparacao();
                    break;
                case "0":
                    System.out.println("Saindo.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcao invalida. Digite 1, 2, 3, 4 ou 0.");
                    break;
            }

            System.out.println();
        }
    }

    private static void executarComparacao() {
        System.out.println("=== Comparacao entre os tres algoritmos ===");

        String[][] casos = {
            {"ABCDCBDCBDACBDABDCBADF", "ADF", "Teste exemplo"},
            {"ABCABCABCABC", "ABCABC", "Teste pequeno"},
            {"AAAAAAAAAA", "AAAAB", "Padrao nao existe"}
        };

        for (String[] caso : casos) {
            String texto = caso[0];
            String padrao = caso[1];
            String label = caso[2];
            System.out.println("--- " + label + " ---");
            printBenchmark(Exercicio1.executarBenchmark(texto, padrao, label));
            printBenchmark(Exercicio2.executarBenchmark(texto, padrao, label));
            printBenchmark(Exercicio3.executarBenchmark(texto, padrao, label));
            System.out.println();
        }

        String textoGrande = gerarAlfanumericoAleatorio(500_001);
        String padraoGrande = textoGrande.substring(1);
        String label = "Teste grande > 500000";
        System.out.println("--- " + label + " ---");
        printBenchmark(Exercicio1.executarBenchmark(textoGrande, padraoGrande, label));
        printBenchmark(Exercicio2.executarBenchmark(textoGrande, padraoGrande, label));
        printBenchmark(Exercicio3.executarBenchmark(textoGrande, padraoGrande, label));
        System.out.println();
    }

    private static void printBenchmark(BenchmarkResultado resultado) {
        System.out.println(resultado);
    }

    private static String gerarAlfanumericoAleatorio(int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            int index = GERADOR.nextInt(ALFANUMERICO.length());
            sb.append(ALFANUMERICO.charAt(index));
        }
        return sb.toString();
    }
}