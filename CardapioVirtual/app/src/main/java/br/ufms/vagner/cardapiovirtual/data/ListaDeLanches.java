package br.ufms.vagner.cardapiovirtual.data;

import java.util.ArrayList;
import java.util.List;

import br.ufms.vagner.cardapiovirtual.model.Lanche;

public class ListaDeLanches {

    private static ListaDeLanches instance;

    private List<Lanche> lanches;

    private ListaDeLanches() {
        lanches = new ArrayList<Lanche>();
    }

    public static ListaDeLanches getInstance() {
        if (instance == null) {
            instance = new ListaDeLanches();
        }
        return instance;
    }

    public static void setInstance(ListaDeLanches instance) {
        ListaDeLanches.instance = instance;
    }

    public List<Lanche> getLanches() {
        return lanches;
    }

    public void setLanches(List<Lanche> lanches) {
        this.lanches = lanches;
    }
}
