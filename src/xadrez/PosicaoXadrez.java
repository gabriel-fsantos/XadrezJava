package xadrez;

import tabuleiro.Posicao;

public class PosicaoXadrez {
    
    private int linha;
    private char coluna;

    public PosicaoXadrez(char coluna, int linha) {
        if(coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8){
            throw new XadrezException("Erro, posições disponiveis 1-8 e a-h");
        }
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public char getColuna() {
        return coluna;
    }
    
    protected Posicao toPosicao(){
        return new Posicao(8 - linha, coluna - 'a');
    }
    
    protected static PosicaoXadrez toPosicaoXadrez(Posicao pos){
        return new PosicaoXadrez((char) ('a' + pos.getColuna()), 8 - pos.getLinha());
    }
    
    @Override
    public String toString(){
        return "" + coluna + linha;
    }
}
