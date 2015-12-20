package br.ufms.vagner.cardapiovirtual.data;

import java.util.ArrayList;

import br.ufms.vagner.cardapiovirtual.model.Lanche;

public class ItemCardapioData {
    public ArrayList<Lanche> getListData(){
        ArrayList<Lanche> listaDeLanches = new ArrayList<Lanche>();

        listaDeLanches.add(new Lanche("MISTO QUENTE", "Presunto, Mussarela e Maionese.", new Float(String.valueOf(5.00))));
        listaDeLanches.add(new Lanche("BAURU", "Presunto, Mussarela, Tomate e Maionese.", new Float(String.valueOf(6.00))));
        listaDeLanches.add(new Lanche("CACHORRO-QUENTE PRENSADO", "Presunto, Mussarela, Salsicha, Catupiry, Ketchup e Maionese.", new Float(String.valueOf(7.00))));
        listaDeLanches.add(new Lanche("X-BURGUER", "Presunto, Mussarela, Hambúrguer e Maionese.", new Float(String.valueOf(7.50))));
        listaDeLanches.add(new Lanche("X-SALADA", "Hambúrguer, Tomate, Alface, Presunto, Mussarela e Maionese.", new Float(String.valueOf(8.00))));
        listaDeLanches.add(new Lanche("HOT BURGER", "Hambúrguer, Presunto, Mussarela, Maionese, Salsicha, Milho-Verde e Batata-Palha.", new Float(String.valueOf(8.50))));
        listaDeLanches.add(new Lanche("X-FRANGO", "Frango, Tomate, Presunto, Milho, Mussarela, Maionese e Alface.", new Float(String.valueOf(8.50))));
        listaDeLanches.add(new Lanche("X-BACON", "Hambúrguer, Bacon, Tomate, Alface, Presunto, Mussarela e Maionese.", new Float(String.valueOf(9.00))));
        listaDeLanches.add(new Lanche("X-CALABRESA", "Hambúrguer, Calabresa, Tomate, Alface, Presunto, Mussarela e Maionese.", new Float(String.valueOf(9.50))));
        listaDeLanches.add(new Lanche("X-TUDO", "Hambúrguer, Bacon, Calabresa, Ovo, Salsicha, Presunto, Mussarela, Tomate, Alface, Milho, Batata-Palha e Maionese.", new Float(String.valueOf(10.00))));

        return listaDeLanches;
    }
}
