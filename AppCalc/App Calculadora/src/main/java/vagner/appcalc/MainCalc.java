package vagner.appcalc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.app.Activity;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainCalc extends Activity implements OnClickListener {
    private float n1;
    private float n2;
    private float resultado;

    private EditText edt1;
    private EditText edt2;

    private Button btnAdicao;
    private Button btnSubtracao;
    private Button btnMultiplicacao;
    private Button btnDivisao;
    private Button btnLimpar;

    private TextView txtVwResultado;

    private String oper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calc);

        // Inputs dos Números
        edt1 = (EditText) findViewById(R.id.edt1);
        edt2 = (EditText) findViewById(R.id.edt2);

        // Botões
        btnAdicao = (Button) findViewById(R.id.btnAdicao);
        btnSubtracao = (Button) findViewById(R.id.btnSubtracao);
        btnMultiplicacao = (Button) findViewById(R.id.btnMultiplicacao);
        btnDivisao = (Button) findViewById(R.id.btnDivisao);
        btnLimpar = (Button) findViewById(R.id.btnLimpar);

        // TextView
        txtVwResultado = (TextView) findViewById(R.id.txtVwResultado);

        // Listeners
        btnAdicao.setOnClickListener(this);
        btnSubtracao.setOnClickListener(this);
        btnMultiplicacao.setOnClickListener(this);
        btnDivisao.setOnClickListener(this);
        btnLimpar.setOnClickListener(this);
    }

    public boolean validaInputNumeros(){
        // Validação de Campos Input Número 1
        if (TextUtils.isEmpty(edt1.getText().toString())) {
            edt1.setError(getResources().getText(R.string.enter_a_number));
            edt1.setHintTextColor(Color.parseColor("#110F10"));
            edt1.requestFocus();
            exibirTeclado();
            return false;
        }
        // Validação de Campos Input Número 2
        if (TextUtils.isEmpty(edt2.getText().toString())) {
            edt2.setError(getResources().getText(R.string.enter_a_number));
            edt2.setHintTextColor(Color.parseColor("#110F10"));
            edt2.requestFocus();
            exibirTeclado();
            return false;
        }
        ocultarTeclado();
        return true;
    }

    public boolean getNumeros(){
        if(!validaInputNumeros()){
            return false;
        }

        // Leituras dos Números dos Inputs
        n1 = Float.parseFloat(edt1.getText().toString());
        n2 = Float.parseFloat(edt2.getText().toString());
        return true;
    }

    public void Adicao(){
        // Operação de Adição
        oper = "+";
        if(!getNumeros()) return;
        resultado = n1+n2;
        ExibeResultado();
    }

    public void Subtracao(){
        // Operação de Subtração
        oper = "-";
        if(!getNumeros()) return;
        resultado = n1-n2;
        ExibeResultado();
    }

    public void Multiplicacao(){
        // Operação de Multiplicação
        oper = "*";
        if(!getNumeros()) return;
        resultado = n1*n2;
        ExibeResultado();
    }

    public void Divisao(){
        // Operação de Divisão
        oper = "/";
        if(!getNumeros()) return;
        try {
            resultado = n1/n2;
            if (resultado == Float.POSITIVE_INFINITY || resultado == Float.NEGATIVE_INFINITY)
                throw new ArithmeticException();
            ExibeResultado();
        } catch (ArithmeticException e) { //
            edt2.setText("");
            edt2.setError(getResources().getText(R.string.enter_number_different_zero));
            edt2.setHintTextColor(Color.parseColor("#110F10"));
            edt2.requestFocus();
            exibirTeclado();
        }
    }

    public void Limpar(){
        edt1.setText("");
        edt2.setText("");
        txtVwResultado.setText("");
        oper = "";
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.inputs_clear), Toast.LENGTH_LONG).show();
        edt1.requestFocus();
    }

    public void exibirTeclado(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
    }

    public void ocultarTeclado(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void ExibeResultado(){
        // Saída
        txtVwResultado.setText(n1 + " " + oper + " " + n2 + " = " + resultado);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdicao:
                Adicao();
                break;
            case R.id.btnSubtracao:
                Subtracao();
                break;
            case R.id.btnMultiplicacao:
                Multiplicacao();
                break;
            case R.id.btnDivisao:
                Divisao();
                resultado = n1 / n2;
                break;
            case R.id.btnLimpar:
                Limpar();
                break;
            default:
                break;
        }
    }
}