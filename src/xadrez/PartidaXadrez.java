package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import tabuleiro.*;
import xadrez.Pecas.*;

public class PartidaXadrez {
   
    private Tabuleiro tabuleiro;
    private int turno;
    private Cor jogadorAtual;
    private boolean check;
    private boolean checkMate;
    private PecaXadrez enPassante;
    private PecaXadrez promovido;
    
    private ArrayList<Peca> pecasTabuleiro = new ArrayList();
    private ArrayList<Peca> pecasCapturadas = new ArrayList();

    public PartidaXadrez() {
        tabuleiro = new Tabuleiro(8,8);
        turno = 1;
        jogadorAtual = Cor.BRANCO;
        iniciarSetup();
    }
    
    public PecaXadrez[][] getPecas(){
        PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
        for(int i=0; i<tabuleiro.getLinhas(); i++) {
            for(int j=0; j<tabuleiro.getColunas(); j++) {
                mat[i][j] = (PecaXadrez) tabuleiro.pecaMatriz(i, j);
            }
        }
	return mat;
    }
    
    public boolean[][] possiveisMoves(PosicaoXadrez fontePos) {
        Posicao pos = fontePos.toPosicao();
        validaFontePosicao(pos);
        return tabuleiro.pecaMatriz(pos).possiveisMoves();
    }
    
    public PecaXadrez executeMovimentoPeca(PosicaoXadrez posicaoFonte, PosicaoXadrez posicaoAlvo){
        Posicao fonte = posicaoFonte.toPosicao();
        Posicao alvo = posicaoAlvo.toPosicao();
        validaFontePosicao(fonte);
        validaAlvoPosicao(fonte, alvo);
        Peca capturada = movimenta(fonte, alvo);
        
        if(testeCheck(jogadorAtual)){
            desfazMovimento(fonte, alvo, capturada);
            throw new XadrezException("Você não pode se colocar em Xeque!!");
        }
        
        PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.pecaMatriz(alvo);
        
        // #specialmove promoção
        promovido = null;
        if(pecaMovida instanceof Pawn){
            if((pecaMovida.getCor() == Cor.BRANCO && alvo.getLinha() == 0) ||(pecaMovida.getCor() == Cor.PRETO && alvo.getLinha() == 7)){
                promovido = (PecaXadrez) tabuleiro.pecaMatriz(alvo);
                promovido = colocaPecaPromovida("Q");
            }
        }
        
        check = (testeCheck(oponente(jogadorAtual))) ? true : false;
        if(testeCheckMate(oponente(jogadorAtual))){
            checkMate = true;
        }
        else{
            proximoTurno();
        }
        
        // #specialmove en passant
        if(pecaMovida instanceof Pawn && (alvo.getLinha() == fonte.getLinha() - 2 || alvo.getLinha() == fonte.getLinha() + 2)){
            enPassante = pecaMovida;
        }
        else{
            enPassante = null;
        }
        
        return (PecaXadrez) capturada;
    }
    
    public PecaXadrez colocaPecaPromovida(String str){
        if(promovido == null){
            throw new IllegalStateException("Não há peca a ser promovida");
        }
        if(!str.equals("B") && !str.equals("N") && !str.equals("R") && !str.equals("Q")){
            throw new InvalidParameterException("Tipo Invalido");
        }
        
        Posicao pos = promovido.getPosicaoXadrez().toPosicao();
        Peca p = tabuleiro.removePeca(pos);
        pecasTabuleiro.remove(p);
        
        PecaXadrez novaPeca = novaPeca(str, promovido.getCor());
        tabuleiro.colocaPeca(novaPeca, pos);
        pecasTabuleiro.add(novaPeca);
        
        return novaPeca;
    }
    
    private PecaXadrez novaPeca(String str, Cor c){
        if(str.equals("B")) return new Bishop(tabuleiro, c);
        if(str.equals("N")) return new Knight(tabuleiro, c);
        if(str.equals("R")) return new Rook(tabuleiro, c);
        return new Queen(tabuleiro, c);
    }
    
    private void validaAlvoPosicao(Posicao fonte, Posicao alvo){
        if(!tabuleiro.pecaMatriz(fonte).possivelMove(alvo)){
            throw new XadrezException("Essa peça não pode ser movida pra essa casa");
        }
    }
    
    private void validaFontePosicao(Posicao pos){
        if(!tabuleiro.temPeca(pos)){
            throw new XadrezException("Não existe peça aqui");
        }
        if(jogadorAtual != ((PecaXadrez)tabuleiro.pecaMatriz(pos)).getCor()){
           throw new XadrezException("Essa peça não é sua");
        }
        if(!tabuleiro.pecaMatriz(pos).temMovePossivel()){
            throw new XadrezException("Não pode se mover");
        }
    }
    
    private void proximoTurno(){
        turno++;
        jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;    
    }
    
    private Peca movimenta(Posicao fonte, Posicao alvo){
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(fonte);
        p.aumentaMove();
        Peca capturada = tabuleiro.removePeca(alvo);
        tabuleiro.colocaPeca(p, alvo);
        
        if(capturada != null){
            pecasTabuleiro.remove(capturada);
            pecasCapturadas.add(capturada);
        }
        
        // #specialmove castling lado do rei rook
        if(p instanceof King && alvo.getColuna() == fonte.getColuna() + 2){
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() + 3);
            Posicao alvoT = new Posicao(fonte.getLinha(), fonte.getColuna() + 1);
            PecaXadrez rook = (PecaXadrez) tabuleiro.removePeca(fonteT);
            tabuleiro.colocaPeca(rook, alvoT);
            rook.aumentaMove();
        }
        // #specialmove castling lado da rainha rook
        if(p instanceof King && alvo.getColuna() == fonte.getColuna() - 2){
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() - 4);
            Posicao alvoT = new Posicao(fonte.getLinha(), fonte.getColuna() - 1);
            PecaXadrez rook = (PecaXadrez) tabuleiro.removePeca(fonteT);
            tabuleiro.colocaPeca(rook, alvoT);
            rook.aumentaMove();
        }
        
        // #specialmove en pssant
        if(p instanceof Pawn){
            if(fonte.getColuna()!= alvo.getColuna() && capturada == null){
                Posicao pawnPosicao;
                if(p.getCor() == Cor.BRANCO){
                    pawnPosicao = new Posicao(alvo.getLinha() + 1, alvo.getColuna());
                }
                else{
                    pawnPosicao = new Posicao(alvo.getLinha() - 1, alvo.getColuna());
                }
                capturada = tabuleiro.removePeca(pawnPosicao);
                pecasCapturadas.add(capturada);
                pecasTabuleiro.remove(capturada);
            } 
        }
        return capturada;
    }
    
    private void desfazMovimento(Posicao fonte, Posicao alvo, Peca capturada){
        PecaXadrez p = (PecaXadrez) tabuleiro.removePeca(alvo);
        p.diminuiMove();
        tabuleiro.colocaPeca(p, fonte);
        
        if (capturada != null){
            tabuleiro.colocaPeca(capturada, alvo);
            pecasCapturadas.remove(capturada);
            pecasTabuleiro.add(capturada);
        }
        
        // #specialmove castling lado do rei rook
        if(p instanceof King && alvo.getColuna() == fonte.getColuna() + 2){
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() + 3);
            Posicao alvoT = new Posicao(fonte.getLinha(), fonte.getColuna() + 1);
            PecaXadrez rook = (PecaXadrez) tabuleiro.removePeca(alvoT);
            tabuleiro.colocaPeca(rook, fonteT);
            rook.diminuiMove();
        }
        
        // #specialmove castling lado da rainha rook
        if(p instanceof King && alvo.getColuna() == fonte.getColuna() - 2){
            Posicao fonteT = new Posicao(fonte.getLinha(), fonte.getColuna() - 4);
            Posicao alvoT = new Posicao(fonte.getLinha(), fonte.getColuna() - 1);
            PecaXadrez rook = (PecaXadrez) tabuleiro.removePeca(alvoT);
            tabuleiro.colocaPeca(rook, fonteT);
            rook.diminuiMove();
        }
        
        // #specialmove en pssant
        if(p instanceof Pawn){
            if(fonte.getColuna()!= alvo.getColuna() && capturada == enPassante){
                PecaXadrez pawn = (PecaXadrez)tabuleiro.removePeca(alvo);
                Posicao pawnPosicao;
                if(p.getCor() == Cor.BRANCO){
                    pawnPosicao = new Posicao(3, alvo.getColuna());
                }
                else{
                    pawnPosicao = new Posicao(4, alvo.getColuna());
                }
                tabuleiro.colocaPeca(pawn, pawnPosicao);
            } 
        }
    }
    
    private Cor oponente(Cor c){
        return (c == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
    }
    
    private PecaXadrez king(Cor c) {
        List<Peca> lista = pecasTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == c).collect(Collectors.toList());
        for (Peca p : lista) {
            if (p instanceof King) {
                return (PecaXadrez) p;
            }
        }
        throw new IllegalStateException("Não existe o rei da cor " + c + " no tabuleiro");
    }
    
    private void colocaPecaNova(char coluna, int linha, PecaXadrez peca){
        tabuleiro.colocaPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecasTabuleiro.add(peca);
    }
    
    private boolean testeCheck(Cor c) {
        Posicao posicaoKing = king(c).getPosicaoXadrez().toPosicao();
        List<Peca> PecasInimigas = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(c)).collect(Collectors.toList());
        for (Peca p : PecasInimigas) {
            boolean[][] mat = p.possiveisMoves();
            if (mat[posicaoKing.getLinha()][posicaoKing.getColuna()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testeCheckMate(Cor c) {
        if (!testeCheck(c)) {
            return false;
        }
        List<Peca> list = pecasTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == c).collect(Collectors.toList());
        for (Peca p : list) {
            boolean[][] mat = p.possiveisMoves();
            for (int i=0; i<tabuleiro.getLinhas(); i++) {
                for (int j=0; j<tabuleiro.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao source = ((PecaXadrez)p).getPosicaoXadrez().toPosicao();
                        Posicao target = new Posicao(i, j);
                        Peca capturedPiece = movimenta(source, target);
                        boolean testCheck = testeCheck(c);
                        desfazMovimento(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private void iniciarSetup(){
        colocaPecaNova('a', 1, new Rook(tabuleiro, Cor.BRANCO));
        colocaPecaNova('b', 1, new Knight(tabuleiro, Cor.BRANCO));
        colocaPecaNova('c', 1, new Bishop(tabuleiro, Cor.BRANCO));
        colocaPecaNova('d', 1, new Queen(tabuleiro, Cor.BRANCO));
        colocaPecaNova('e', 1, new King(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('f', 1, new Bishop(tabuleiro, Cor.BRANCO));
        colocaPecaNova('g', 1, new Knight(tabuleiro, Cor.BRANCO));
        colocaPecaNova('h', 1, new Rook(tabuleiro, Cor.BRANCO));
        colocaPecaNova('a', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('b', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('c', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('d', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('e', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('f', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('g', 2, new Pawn(tabuleiro, Cor.BRANCO, this));
        colocaPecaNova('h', 2, new Pawn(tabuleiro, Cor.BRANCO, this));

        colocaPecaNova('a', 8, new Rook(tabuleiro, Cor.PRETO));
        colocaPecaNova('b', 8, new Knight(tabuleiro, Cor.PRETO));
        colocaPecaNova('c', 8, new Bishop(tabuleiro, Cor.PRETO));
        colocaPecaNova('d', 8, new Queen(tabuleiro, Cor.PRETO));
        colocaPecaNova('e', 8, new King(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('f', 8, new Bishop(tabuleiro, Cor.PRETO));
        colocaPecaNova('g', 8, new Knight(tabuleiro, Cor.PRETO));
        colocaPecaNova('h', 8, new Rook(tabuleiro, Cor.PRETO));
        colocaPecaNova('a', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('b', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('c', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('d', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('e', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('f', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('g', 7, new Pawn(tabuleiro, Cor.PRETO, this));
        colocaPecaNova('h', 7, new Pawn(tabuleiro, Cor.PRETO, this));
    }

    public int getTurno() {
        return turno;
    }

    public Cor getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public PecaXadrez getEnPassante() {
        return enPassante;
    }

    public PecaXadrez getPromovido() {
        return promovido;
    }
    
}
