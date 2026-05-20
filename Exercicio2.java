import java.util.Random;

public class Exercicio2 {
    private static final int TAMANHO_GRANDE = 500_000;
    private static final int R = 256;
    private static final long Q = 2147483647L; // primo grande
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
        System.out.println("=== Exercicio 2: busca de padrao com Rabin-Karp ===");

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
        int pos = buscarPadraoRabinKarp(texto, padrao, contador);
        long duracao = System.currentTimeMillis() - inicio;
        return new BenchmarkResultado("Rabin-Karp", label, pos, contador.comparacoes, contador.passos, duracao);
    }

    private static void executarTeste(String texto, String padrao, String label) {
        Contador contador = new Contador();
        int pos = buscarPadraoRabinKarp(texto, padrao, contador);
        System.out.println(label + ": tamanho texto=" + texto.length() + ", tamanho padrao=" + padrao.length());
        System.out.println("  posicao=" + pos + ", " + contador);
    }

    private static int buscarPadraoRabinKarp(String texto, String padrao, Contador contador) {
        contador.incrementarPasso();
        int M = padrao.length();
        int N = texto.length();
        if (M == 0) {
            return 0;
        }

        long padraoHash = hash(padrao, M, contador);
        long textoHash = hash(texto, M, contador);
        long RM = calcularRM(M, contador);

        for (int i = 0; i <= N - M; i++) {
            contador.incrementarPasso();
            contador.incrementarComparacao();
            if (padraoHash == textoHash) {
                contador.incrementarPasso();
                if (texto.regionMatches(i, padrao, 0, M)) {
                    return i;
                }
            }

            if (i < N - M) {
                contador.incrementarPasso();
                textoHash = (textoHash + Q - RM * texto.charAt(i) % Q) % Q;
                textoHash = (textoHash * R + texto.charAt(i + M)) % Q;
            }
        }

        return -1;
    }

    private static long hash(String s, int M, Contador contador) {
        long h = 0;
        for (int j = 0; j < M; j++) {
            contador.incrementarPasso();
            h = (h * R + s.charAt(j)) % Q;
        }
        return h;
    }

    private static long calcularRM(int M, Contador contador) {
        long rm = 1;
        for (int i = 1; i <= M - 1; i++) {
            contador.incrementarPasso();
            rm = (rm * R) % Q;
        }
        return rm;
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
