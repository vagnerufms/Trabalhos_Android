package vagner.logindinamico;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vagner.loginestatico.R;


public class Cadastro extends Activity implements OnClickListener{

    private EditText edtUsuario, edtSenha, edtConfirmacaoSenha;
    private String usuario, senha;
    private Button btnCadastrar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtConfirmacaoSenha = (EditText) findViewById(R.id.edtConfirmacaoSenha);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        // Listeners
        btnCadastrar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCadastrar:
                Cadastrar();
                break;
            case R.id.btnCancelar:
                CarregaTelaInicial();
                break;
            default:
                break;
        }
    }

    public boolean validaCampos(){
        // Validação do Input Usuário
        if (TextUtils.isEmpty(edtUsuario.getText().toString())) {
            edtUsuario.setError("Informe o Usuário");
            edtUsuario.setHintTextColor(Color.parseColor("#110F10"));
            edtUsuario.requestFocus();
            return false;
        }
        // Validação do Input Senha
        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError("Informe a Senha");
            edtSenha.setHintTextColor(Color.parseColor("#110F10"));
            edtSenha.requestFocus();
            return false;
        }
        // Validação do Input Repetição de Senha
        if (TextUtils.isEmpty(edtConfirmacaoSenha.getText().toString())) {
            edtConfirmacaoSenha.setError("Confirme a Senha");
            edtConfirmacaoSenha.setHintTextColor(Color.parseColor("#110F10"));
            edtConfirmacaoSenha.requestFocus();
            return false;
        }
        if(!edtSenha.getText().toString().equals(edtConfirmacaoSenha.getText().toString())){
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.password_not_equals), Toast.LENGTH_LONG).show();
            edtSenha.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isDadosUsuarioValido(){
        for (Usuario usuarioObj : ListaDeUsuarios.getInstance().getUsuarios()) {
            if (usuarioObj.getNome().equals(usuario)) {
                return false;
            }
        }
        return true;
    }

    public void exibirTeclado(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
    }

    public void ocultarTeclado(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void getCampos(){
        // Leituras dos Inputs Usuário e Senha
        usuario = edtUsuario.getText().toString();
        senha = edtSenha.getText().toString();
    }

    public void SalvaUsuario(){
        Usuario usuarioObj = new Usuario();

        usuarioObj.setNome(usuario);
        usuarioObj.setSenha(senha);

        ListaDeUsuarios.getInstance().getUsuarios().add(usuarioObj);
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.sucess_register), Toast.LENGTH_LONG).show();
    }

    public void Cadastrar(){
        // Valida os Campo de Usuário e Senha
        if(!validaCampos()) return;
        // Seta os Dados de Usuário e Senha
        getCampos();
        // Consulta os Dados do Usuário na Lista de Usuários
        if(!isDadosUsuarioValido()){
            edtUsuario.setText("");
            edtSenha.setText("");
            edtConfirmacaoSenha.setText("");
            edtUsuario.requestFocus();
            ocultarTeclado();
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.login_in_use), Toast.LENGTH_LONG).show();
            return;
        }
        //Salva Usuário
        SalvaUsuario();
        // Chama a Tela Inicial
        CarregaTelaInicial();
    }

    public void CarregaTelaInicial(){
        finish();
    }
}