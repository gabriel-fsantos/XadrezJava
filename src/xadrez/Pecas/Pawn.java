package xadrez.Pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Pawn extends PecaXadrez{

    private PartidaXadrez partidaXadrez;

    public Pawn(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
        super(tabuleiro, cor);
        this.partidaXadrez = partidaXadrez;
    }
        
    @Override
    public String toString(){
        return "P";
    }

    @Override
    public boolean[][] possiveisMoves() {
        
        boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
        Posicao p = new Posicao(0, 0);

        if (getCor() == Cor.BRANCO) {
            p.setValores(posicao.getLinha() - 1, posicao.getColuna());
            
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temPeca(p2) && getContMove() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            
            if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }			
            p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            
            if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }	

            // #specialmove en passant white
            if (posicao.getLinha() == 3) {
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(left) && ePecaInimiga(left) && getTabuleiro().pecaMatriz(left) == partidaXadrez.getEnPassante()) {
                    mat[left.getLinha() - 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(right) && ePecaInimiga(right) && getTabuleiro().pecaMatriz(right) == partidaXadrez.getEnPassante()) {
                    mat[right.getLinha() - 1][right.getColuna()] = true;
                }
            }
        }
        else {
            p.setValores(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if (getTabuleiro().posicaoExiste(p) && !getTabuleiro().temPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temPeca(p2) && getContMove() == 0) {
                mat[p.getLinha()][p.getColuna()] = true;
            }
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }			
            p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (getTabuleiro().posicaoExiste(p) && ePecaInimiga(p)) {
                mat[p.getLinha()][p.getColuna()] = true;
            }

            // #specialmove en passant black
            if (posicao.getLinha() == 4) {
                Posicao left = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (getTabuleiro().posicaoExiste(left) && ePecaInimiga(left) && getTabuleiro().pecaMatriz(left) == partidaXadrez.getEnPassante()) {
                    mat[left.getLinha() + 1][left.getColuna()] = true;
                }
                Posicao right = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (getTabuleiro().posicaoExiste(right) && ePecaInimiga(right) && getTabuleiro().pecaMatriz(right) == partidaXadrez.getEnPassante()) {
                    mat[right.getLinha() + 1][right.getColuna()] = true;
                }
            }			
        }
        return mat;
    }
}
