package tabuleiro;

public class Tabuleiro {
    
    private int linhas;
    private int colunas;
    private Peca[][] pecas;

    public Tabuleiro(int linhas, int colunas) {
        if(linhas < 1 || colunas < 1){
            throw new TabuleiroException("Erro Criando o Tabuleiro: O tabuleiro precisa ter pelo menos 1 linha e 1 coluna");
        }
        this.linhas = linhas;
        this.colunas = colunas;
        this.pecas = new Peca[linhas][colunas];
    }
        
    public Peca pecaMatriz(int linha, int coluna){ 
        if(!posicaoExiste(linha, coluna)){
            throw new TabuleiroException("Posição fora do tabuleiro");
        }
        return pecas[linha][coluna];
    }
    
    public Peca pecaMatriz(Posicao pos){
        if(!posicaoExiste(pos)){
            throw new TabuleiroException("Posição fora do tabuleiro");
        }
        return pecas[pos.getLinha()][pos.getColuna()];
    }
    
    public void colocaPeca(Peca peca, Posicao pos){
        if(temPeca(pos)){
            throw new TabuleiroException("Posição ocupada");
        }
        pecas[pos.getLinha()][pos.getColuna()] = peca;
        peca.posicao = pos;
    }

    public Peca removePeca(Posicao pos){
        if (!posicaoExiste(pos)) {
            throw new TabuleiroException("Posição fora do tabuleiro");
	}
	if (pecaMatriz(pos) == null) {
            return null;
	}
	Peca aux = pecaMatriz(pos);
	aux.posicao = null;
	pecas[pos.getLinha()][pos.getColuna()] = null;
	return aux;
    }

    private boolean posicaoExiste(int linha, int coluna){
        return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
    }
    
    public boolean posicaoExiste(Posicao pos){
        return posicaoExiste(pos.getLinha(), pos.getColuna());
    }

    public boolean temPeca(Posicao pos){
        if(!posicaoExiste(pos)){
            throw new TabuleiroException("Posição fora do tabuleiro");
        }
        return pecaMatriz(pos) != null;
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }
 
}
