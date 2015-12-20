package br.ufms.vagner.cardapiovirtual.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.ufms.vagner.cardpiovirtual.R;
import br.ufms.vagner.cardapiovirtual.model.Lanche;

public class ItemCardapioAdapter extends ArrayAdapter<Lanche> {
    private List<Lanche> objects;
    private TextView txtVwNome;
    private TextView txtVwDescricao;
    private TextView txtVwPreco;

    public ItemCardapioAdapter(Context context, int textViewResourceId, List<Lanche> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.adapter_item_cardapio, null);
        }

        Lanche i = objects.get(position);

        if (i != null) {
            NumberFormat formatarFloat = NumberFormat.getInstance(new Locale("pt", "BR"));

            txtVwNome = (TextView) v.findViewById(R.id.txtVwNome);
            txtVwDescricao = (TextView) v.findViewById(R.id.txtVwDescricao);
            txtVwPreco = (TextView) v.findViewById(R.id.txtVwPreco);

            txtVwNome.setText((position+1)+" - "+i.getNome());
            txtVwDescricao.setText(i.getDescricao());
            txtVwPreco.setText("R$ "+formatarFloat.format(i.getPreco()));
        }

        return v;
    }
}