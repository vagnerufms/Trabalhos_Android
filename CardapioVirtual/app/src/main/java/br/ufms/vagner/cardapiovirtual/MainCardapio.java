package br.ufms.vagner.cardapiovirtual;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;

import br.ufms.vagner.cardapiovirtual.adapter.ItemCardapioAdapter;
import br.ufms.vagner.cardapiovirtual.data.ItemCardapioData;
import br.ufms.vagner.cardapiovirtual.data.ListaDeLanches;
import br.ufms.vagner.cardapiovirtual.model.Lanche;
import br.ufms.vagner.cardpiovirtual.R;

public class MainCardapio extends Activity implements OnClickListener, Serializable {

    private ArrayList<Lanche> lanches = new ArrayList<Lanche>();
    private ItemCardapioAdapter adapterItemCardapio;
    private ListView listViewItensCardapio;
    private Button btnCadastrar;
    private Lanche lanche;
    private static int qtdLanches;
    private boolean updateListView;
    private String mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cardapio);

        ListaDeLanches.getInstance().setLanches(new ItemCardapioData().getListData());

        preencheListViewCardapio();

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        // Listeners
        btnCadastrar.setOnClickListener(this);
        listenerListViewItensCardapio();
    }

    public void preencheListViewCardapio(){
        if(qtdLanches!= ListaDeLanches.getInstance().getLanches().size() || updateListView){
            qtdLanches = ListaDeLanches.getInstance().getLanches().size();
            adapterItemCardapio = new ItemCardapioAdapter(this, R.layout.adapter_item_cardapio, ListaDeLanches.getInstance().getLanches());

            listViewItensCardapio = (ListView) findViewById(R.id.listapessoas);
            listViewItensCardapio.setAdapter(adapterItemCardapio);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCadastrar:
                Cadastrar();
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void listenerListViewItensCardapio(){
        listViewItensCardapio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
                CarregaTelaEdicao(adapter, posicao);
            }
        });
    }

    public void Cadastrar(){
        CarregaTelaCadastro();
    }

    public void CarregaTelaCadastro(){
        // Carrega a Tela do Cadastro
        Intent intent = new Intent(getBaseContext(), Cadastro.class);
        //startActivity(intent);
        startActivityForResult(intent, 1);
    }

    public void CarregaTelaEdicao(AdapterView<?> adapter, int posicao){
        // Carrega a Tela do Edicao
        lanche = (Lanche) adapter.getAdapter().getItem(posicao);
        Intent intent = new Intent(getBaseContext(), Edicao.class);
        intent.putExtra("lanche", lanche);
        intent.putExtra("posicao", posicao);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            if (requestCode == 1) {
                updateListView = data.getBooleanExtra("updateListView", true);
                if (resultCode == RESULT_OK) {
                    mensagem = data.getStringExtra("mensagem");
                    Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
                    preencheListViewCardapio();
                }
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Erro. Operação Cancelada!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}