package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import xadrez.*;

public class Programa {
    
    public static void main (String[] args){
    
        Scanner sc = new Scanner(System.in);
        PartidaXadrez partidaXadrez = new PartidaXadrez();
        ArrayList<PecaXadrez> capturadas = new ArrayList();

        while (!partidaXadrez.getCheckMate()) {
            try {
                UI.limpaTela();
                UI.printaPartida(partidaXadrez, capturadas);
                System.out.println();
                System.out.print("Fonte: ");
                PosicaoXadrez fonte = UI.lePosicaoXadrez(sc);

                boolean[][] possiveisMoves = partidaXadrez.possiveisMoves(fonte);
                UI.limpaTela();
                UI.printaTabuleiro(partidaXadrez.getPecas(), possiveisMoves);
                System.out.println();
                System.out.print("Alvo: ");
                
                PosicaoXadrez target = UI.lePosicaoXadrez(sc);
                PecaXadrez capturedPiece = partidaXadrez.executeMovimentoPeca(fonte, target);

                if (capturedPiece != null) {
                    capturadas.add(capturedPiece);
                }

                if (partidaXadrez.getPromovido() != null) {
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.nextLine();
                    partidaXadrez.colocaPecaPromovida(type);
                }
            }
            catch (XadrezException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
        UI.limpaTela();
        UI.printaPartida(partidaXadrez, capturadas);
    }
}