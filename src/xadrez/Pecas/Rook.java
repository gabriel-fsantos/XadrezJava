package xadrez.Pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rook extends PecaXadrez{

    public Rook(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }
        
    @Override
    public String toString(){
        return "R";
    }

    @Override
    public boolean[][] possiveisMoves() {
        
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao p = new Posicao(0, 0);

        // acima
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() - 1);
        }
        if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() - 1);
        }
        if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setColuna(p.getColuna() + 1);
        }
        if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // atras
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        while (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
            p.setLinha(p.getLinha() + 1);
        }
        if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        return mat;
    }
}
