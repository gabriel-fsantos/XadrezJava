package xadrez.Pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Knight extends PecaXadrez{

    public Knight(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro, cor);
    }
    
    @Override
    public String toString(){
        return "K";
    }

    private boolean podeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez)getTabuleiro().pecaMatriz(posicao);
        return p == null || p.getCor() != getCor();
    }
	
    @Override
    public boolean[][] possiveisMoves() {
        
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

        Posicao p = new Posicao(0, 0);

        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 2);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() - 2, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() - 2, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 2);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 2);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() + 2, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() + 2, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }

        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 2);
        if (getTabuleiro().posicaoExiste(p) && podeMover(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
        }
        return mat;
    }

}
