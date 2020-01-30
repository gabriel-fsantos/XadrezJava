package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca{
    
    private Cor cor;
    private int contMove;
    

    public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
        super(tabuleiro);
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public int getContMove() {
        return contMove;
    }
       
    public PosicaoXadrez getPosicaoXadrez(){
        return PosicaoXadrez.toPosicaoXadrez(posicao);
    }
    
    protected boolean ePecaInimiga(Posicao pos){
        PecaXadrez p = (PecaXadrez)getTabuleiro().pecaMatriz(pos);
	return p != null && p.getCor() != cor;
    }
    
    protected void aumentaMove(){
        contMove++;
    }
    
    protected void diminuiMove(){
        contMove--;
    }   
}
