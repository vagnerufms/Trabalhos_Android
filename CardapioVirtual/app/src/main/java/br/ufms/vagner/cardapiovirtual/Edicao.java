package br.ufms.vagner.cardapiovirtual;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import br.ufms.vagner.cardapiovirtual.data.ListaDeLanches;
import br.ufms.vagner.cardapiovirtual.model.Lanche;
import br.ufms.vagner.cardpiovirtual.R;

public class Edicao extends Activity implements OnClickListener {

    private EditText edtTextNome, edtTextDescricao, edtTextPreco;
    private Button btnAtualizar, btnCancelar, btnExcluir;
    private Lanche lanche;
    private int posicaoLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicao);

        Intent i = getIntent();
        lanche = (Lanche)i.getSerializableExtra("lanche");
        posicaoLista = (int)i.getSerializableExtra("posicao");

        edtTextNome = (EditText) findViewById(R.id.edtTextNome);
        edtTextDescricao = (EditText) findViewById(R.id.edtTextDescricao);
        edtTextPreco = (EditText) findViewById(R.id.edtTextPreco);

        //Setando Valores
        edtTextNome.setText(lanche.getNome());
        edtTextDescricao.setText(lanche.getDescricao());
        edtTextPreco.setText(lanche.getPreco().toString());

        btnAtualizar = (Button) findViewById(R.id.btnAtualizar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        // Listeners
        btnAtualizar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAtualizar:
                // Atualizar
                Atualizar();
                break;
            case R.id.btnCancelar:
                // Cancelar
                CarregaTelaInicial();
                break;
            case R.id.btnExcluir:
                // Excluir
                ExcluirLanche();
                break;
            default:
                break;
        }
    }

    public boolean validaCampos(){
        // Validação do Input Nome
        if (TextUtils.isEmpty(edtTextNome.getText().toString())) {
            edtTextNome.setError("Informe o Nome.");
            edtTextNome.setHintTextColor(Color.parseColor("#110F10"));
            edtTextNome.requestFocus();
            return false;
        }
        // Validação do Input Descrição
        else if (TextUtils.isEmpty(edtTextDescricao.getText().toString())) {
            edtTextDescricao.setError("Informe a Descrição");
            edtTextDescricao.setHintTextColor(Color.parseColor("#110F10"));
            edtTextDescricao.requestFocus();
            return false;
        }
        // Validação do Input Repetição de Senha
        else if (TextUtils.isEmpty(edtTextPreco.getText().toString())) {
            edtTextPreco.setError("Informe o Preço");
            edtTextPreco.setHintTextColor(Color.parseColor("#110F10"));
            edtTextPreco.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public void getCampos(){
        // Leituras dos Inputs Nome, Descrição e Preço
        lanche.setNome(edtTextNome.getText().toString());
        lanche.setDescricao(edtTextDescricao.getText().toString());
        lanche.setPreco(Float.valueOf(edtTextPreco.getText().toString()));
    }

    public void Atualizar(){
        // Valida os Campos de Nome, Descrição e Preço
        if(!validaCampos()) return;
        // Seta os Dados de Usuário e Senha
        getCampos();
        //Salva o Lanche na Lista
        SalvaLanche();
        // Chama a Tela Inicial
        CarregaTelaInicial();
    }

    public void SalvaLanche(){
        ListaDeLanches.getInstance().getLanches().set(posicaoLista, lanche);
        Intent intent = new Intent(getBaseContext(), Edicao.class);
        intent.putExtra("updateLista", true);
        intent.putExtra("mensagem", "Lanche atualizado com Sucesso!");
        setResult(RESULT_OK, intent);
        CarregaTelaInicial();
    }

    public void ExcluirLanche() {
        ListaDeLanches.getInstance().getLanches().remove(posicaoLista);
        Intent intent = new Intent(getBaseContext(), Edicao.class);
        intent.putExtra("updateLista", true);
        intent.putExtra("mensagem", "Lanche Removido com Sucesso!");
        setResult(RESULT_OK, intent);
        CarregaTelaInicial();
    }

    public void CarregaTelaInicial() {
        finish();
    }

}