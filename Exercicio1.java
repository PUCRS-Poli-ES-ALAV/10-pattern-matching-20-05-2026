import java.util.Random;

public class Exercicio1 {
    private static final int TAMANHO_GRANDE = 500_000;
    private static final String ALFANUMERICO = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random GERADOR = new Random();

    private static class Contador {
        long comparacoes = 0;
        long passos = 0;

        void incrementarComparacao() {
            comparacoes++;
            passos++;
        }

        void incrementarPasso() {
            passos++;
        }

        @Override
        public String toString() {
            return "comparacoes=" + comparacoes + ", passos=" + passos;
        }
    }

    public static void executar() {
        System.out.println("=== Exercicio 1: busca de padrao com KMP ===");

        String s1 = "ABCDCBDCBDACBDABDCBADF";
        String s2 = "ADF";
        executarTeste(s1, s2, "Teste exemplo");

        executarTeste("ABCABCABCABC", "ABCABC", "Teste pequeno");
        executarTeste("AAAAAAAAAA", "AAAAB", "Padrao nao existe");

        String textoGrande = gerarAlfanumericoAleatorio(TAMANHO_GRANDE + 1);
        String padraoGrande = textoGrande.substring(1);
        executarTeste(textoGrande, padraoGrande, "Teste grande > 500000");

        System.out.println("Complexidade no pior caso: O(n + m), onde n = |texto| e m = |padrao|");
    }

    public static BenchmarkResultado executarBenchmark(String texto, String padrao, String label) {
        long inicio = System.currentTimeMillis();
        Contador contador = new Contador();
        int pos = buscarPadraoKMP(texto, padrao, contador);
        long duracao = System.currentTimeMillis() - inicio;
        return new BenchmarkResultado("KMP", label, pos, contador.comparacoes, contador.passos, duracao);
    }

    private static void executarTeste(String texto, String padrao, String label) {
        Contador contador = new Contador();
        int pos = buscarPadraoKMP(texto, padrao, contador);
        System.out.println(label + ": tamanho texto=" + texto.length() + ", tamanho padrao=" + padrao.length());
        System.out.println("  posicao=" + pos + ", " + contador);
    }

    private static int buscarPadraoKMP(String texto, String padrao, Contador contador) {
        contador.incrementarPasso();
        if (padrao.isEmpty()) {
            return 0;
        }

        int[] pi = calcularFuncaoPrefixo(padrao, contador);
        int j = 0;

        for (int i = 0; i < texto.length(); i++) {
            contador.incrementarPasso();
            contador.incrementarComparacao();
            while (j > 0 && texto.charAt(i) != padrao.charAt(j)) {
                contador.incrementarPasso();
                j = pi[j - 1];
            }

            contador.incrementarComparacao();
            if (texto.charAt(i) == padrao.charAt(j)) {
                j++;
                contador.incrementarPasso();
                if (j == padrao.length()) {
                    return i - padrao.length() + 1;
                }
            }
        }

        return -1;
    }

    private static int[] calcularFuncaoPrefixo(String padrao, Contador contador) {
        int m = padrao.length();
        int[] pi = new int[m];
        int j = 0;

        for (int i = 1; i < m; i++) {
            contador.incrementarPasso();
            contador.incrementarComparacao();
            while (j > 0 && padrao.charAt(i) != padrao.charAt(j)) {
                contador.incrementarPasso();
                j = pi[j - 1];
            }

            contador.incrementarComparacao();
            if (padrao.charAt(i) == padrao.charAt(j)) {
                j++;
                contador.incrementarPasso();
            }
            pi[i] = j;
            contador.incrementarPasso();
        }

        return pi;
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
