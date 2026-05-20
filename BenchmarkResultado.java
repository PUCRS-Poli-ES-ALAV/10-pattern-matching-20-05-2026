public class BenchmarkResultado {
    public final String algoritmo;
    public final String teste;
    public final int posicao;
    public final long comparacoes;
    public final long passos;
    public final long duracaoMillis;

    public BenchmarkResultado(String algoritmo, String teste, int posicao, long comparacoes, long passos, long duracaoMillis) {
        this.algoritmo = algoritmo;
        this.teste = teste;
        this.posicao = posicao;
        this.comparacoes = comparacoes;
        this.passos = passos;
        this.duracaoMillis = duracaoMillis;
    }

    @Override
    public String toString() {
        return algoritmo + " => posicao=" + posicao + ", comparacoes=" + comparacoes + ", passos=" + passos + ", tempo=" + duracaoMillis + "ms";
    }
}
