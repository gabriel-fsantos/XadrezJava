package tabuleiro;

public abstract class Peca {
    
    protected Posicao posicao;
    private Tabuleiro tabuleiro;

    public Peca(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
        posicao = null;
    }
    
    public abstract boolean[][] possiveisMoves();   
    
    public boolean possivelMove(Posicao pos){
        return possiveisMoves()[pos.getLinha()][pos.getColuna()];
    }
    
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
          
    public boolean temMovePossivel(){
        boolean[][] mat = possiveisMoves();
	for (int i=0; i<mat.length; i++) {
            for (int j=0; j<mat.length; j++) {
		if (mat[i][j]) {
                    return true;
		}
            }
	}
	return false;
    }
}
