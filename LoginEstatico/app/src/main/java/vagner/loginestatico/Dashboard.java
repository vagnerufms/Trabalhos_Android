package vagner.loginestatico;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import vagner.loginestatico.R;

public class Dashboard extends Activity implements OnClickListener{

    private Button btnSair;
    private TextView textNomeUsuario;

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent i = getIntent();

        usuario = i.getStringExtra("usuario");

        textNomeUsuario = (TextView) findViewById(R.id.textNomeUsuario);
        textNomeUsuario.setText(usuario);

        btnSair = (Button) findViewById(R.id.btnSair);

        // Listeners
        btnSair.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSair:
                CarregaTelaInicial();
                break;
            default:
                break;
        }
    }

    public void CarregaTelaInicial(){
        finish();
    }
}