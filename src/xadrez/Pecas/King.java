package xadrez.Pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class King extends PecaXadrez{
 
    private PartidaXadrez partidaXadrez;

    public King(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
        super(tabuleiro, cor);
        this.partidaXadrez = partidaXadrez;
    }
    
    @Override
    public String toString(){
        return "K";
    }

    private boolean PodeMover(Posicao posicao) {
        PecaXadrez p = (PecaXadrez)getTabuleiro().pecaMatriz(posicao);
        return p == null || p.getCor() != getCor();
    }
	
    private boolean testeRook(Posicao posicao) {
        PecaXadrez p = (PecaXadrez)getTabuleiro().pecaMatriz(posicao);
        return p != null && p instanceof Rook && p.getCor() == getCor() && p.getContMove() == 0;
    }

    @Override
    public boolean[][] possiveisMoves() {
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao p = new Posicao(0, 0);

        // frente
        p.setValores(posicao.getLinha() - 1, posicao.getColuna());
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // atras
        p.setValores(posicao.getLinha() + 1, posicao.getColuna());
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // esquerda
        p.setValores(posicao.getLinha(), posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // direita
        p.setValores(posicao.getLinha(), posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // nw
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // ne
        p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // sw
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // se
        p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if (getTabuleiro().posicaoExiste(p) && PodeMover(p)) {
            mat[p.getLinha()][p.getColuna()] = true;
        }

        // #specialmove castling
        if (getContMove() == 0 && !partidaXadrez.getCheck()) {
            // #specialmove castling lado do rei rook
            Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if (testeRook(posT1)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if (getTabuleiro().pecaMatriz(p1) == null && getTabuleiro().pecaMatriz(p2) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }
            // #specialmove castling lado da rainha rook
            Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if (testeRook(posT2)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if (getTabuleiro().pecaMatriz(p1) == null && getTabuleiro().pecaMatriz(p2) == null && getTabuleiro().pecaMatriz(p3) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }
        }
        return mat;
    }
}
