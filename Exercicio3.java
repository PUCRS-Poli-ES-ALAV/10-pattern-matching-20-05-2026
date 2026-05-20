import java.util.Random;

public class Exercicio3 {
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
        System.out.println("=== Exercicio 3: busca de padrao com KMP (LPS) ===");

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
        return new BenchmarkResultado("KMP LPS", label, pos, contador.comparacoes, contador.passos, duracao);
    }

    private static void executarTeste(String texto, String padrao, String label) {
        Contador contador = new Contador();
        int pos = buscarPadraoKMP(texto, padrao, contador);
        System.out.println(label + ": tamanho texto=" + texto.length() + ", tamanho padrao=" + padrao.length());
        System.out.println("  posicao=" + pos + ", " + contador);
    }

    private static int buscarPadraoKMP(String texto, String padrao, Contador contador) {
        int M = padrao.length();
        int N = texto.length();
        if (M == 0) {
            return 0;
        }

        int[] lps = new int[M];
        computeLPSArray(padrao, M, lps, contador);

        int i = 0;
        int j = 0;
        while (i < N) {
            contador.incrementarPasso();
            contador.incrementarComparacao();
            if (padrao.charAt(j) == texto.charAt(i)) {
                i++;
                j++;
            }

            if (j == M) {
                return i - j;
            } else if (i < N) {
                contador.incrementarComparacao();
                if (padrao.charAt(j) != texto.charAt(i)) {
                    if (j != 0) {
                        j = lps[j - 1];
                    } else {
                        i++;
                    }
                }
            }
        }

        return -1;
    }

    private static void computeLPSArray(String pat, int M, int[] lps, Contador contador) {
        int len = 0;
        lps[0] = 0;
        int i = 1;

        while (i < M) {
            contador.incrementarPasso();
            contador.incrementarComparacao();
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
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
