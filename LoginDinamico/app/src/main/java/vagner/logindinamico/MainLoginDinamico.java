package vagner.logindinamico;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vagner.loginestatico.R;

public class MainLoginDinamico extends Activity implements OnClickListener {

    private EditText edtUsuario, edtSenha;
    private String usuario, senha;
    private Button btnLogin, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_dinamico);

        Intent i = getIntent();

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtSenha = (EditText) findViewById(R.id.edtSenha);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCadastro = (Button) findViewById(R.id.btnCadastro);

        // Listeners
        btnLogin.setOnClickListener(this);
        btnCadastro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Logar();
                break;
            case R.id.btnCadastro:
                CarregaTelaCadastro();
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

        // Validação do Input Usuário
        if (TextUtils.isEmpty(edtSenha.getText().toString())) {
            edtSenha.setError("Informe a Senha");
            edtSenha.setHintTextColor(Color.parseColor("#110F10"));
            edtSenha.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isDadosUsuarioValido(){
        for (Usuario usuarioObj : ListaDeUsuarios.getInstance().getUsuarios()) {
            if (usuarioObj.getNome().equals(usuario) && usuarioObj.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
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

    public void Logar(){
        // Valida os Campo de Usuário e Senha
        if(!validaCampos()) return;
        // Seta os Dados de Usuário e Senha
        getCampos();
        // Consulta os Dados do Usuário na Lista de Usuários
        if(!isDadosUsuarioValido()){
            edtUsuario.setText("");
            edtSenha.setText("");
            edtUsuario.requestFocus();
            ocultarTeclado();
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.invalid_data), Toast.LENGTH_LONG).show();
            return;
        }
        // Chama a Tela do Dashboard
        CarregaTelaDashboard();
    }

    public void CarregaTelaCadastro(){
        // Carrega a Tela de Cadastro
        Intent i = new Intent(getBaseContext(), Cadastro.class);
        startActivity(i);
    }

    public void CarregaTelaDashboard(){
        // Carrega a Tela do Dashboard
        Intent cadSucesso = new Intent(getBaseContext(), Dashboard.class);
        Bundle b = new Bundle();
        b.putString("usuario", usuario);
        cadSucesso.putExtras(b);
        startActivity(cadSucesso);
    }
}