package br.ufms.vagner.agenda.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import br.ufms.vagner.agenda.R;
import br.ufms.vagner.agenda.model.Contato;
import br.ufms.vagner.agenda.util.Alert;
import br.ufms.vagner.agenda.util.AlertDialogUtil;
import br.ufms.vagner.agenda.util.BitmapUtils;
import br.ufms.vagner.agenda.util.FireBaseUtil;
import br.ufms.vagner.agenda.util.ImageConverter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ContatoViewActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.nome)
    public TextView mNome;
    @Bind(R.id.telefone)
    public TextView mTelefone;
    @Bind(R.id.email)
    public TextView mEmail;
    @Bind(R.id.image_preview)
    public ImageView mImageView;
    private Contato contato;
    private Bitmap photo;
    private Uri outputFileUri;
    private int posicaoLista;
    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_view);
        ButterKnife.bind(this);
        firebase = FireBaseUtil.getFirebase();
        setSupportActionBar(mToolbar);
        // Toolbar Title
        getSupportActionBar().setTitle(getString(R.string.detalhes));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        Intent i = getIntent();
        contato = (Contato) i.getSerializableExtra("contato");
        setValuesElements();
    }

    public void setValuesElements() {
        if (contato != null) {
            mNome.setText(contato.getNome());
            mTelefone.setText(contato.getTelefone());
            mEmail.setText(contato.getEmail());
            mImageView.setImageBitmap(ImageConverter.getRoundedCornerBitmap(BitmapUtils.getBitmapFromImgString(contato.getImagem(), getApplicationContext()), 100));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contato_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (new AlertDialogUtil().getYesNoWithExecutionStop("Exclusão", "Deseja realmente Excluir o Contato?", ContatoViewActivity.this)) {
                    excluirContatoFirebase(contato);
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_contato_excluido_sucesso), Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            case R.id.action_edit:
                loadEditContato(contato);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadEditContato(Contato contato) {
        // Carrega a Tela de Edicao
        Intent intent = new Intent(getBaseContext(), ContatoEditActivity.class);
        intent.putExtra("contato", contato);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Bitmap image = savedInstanceState.getParcelable("BitmapImageContato");
        Uri outputFileUri = savedInstanceState.getParcelable("outputFileUri");
        if (image != null) {
            photo = image;
            mImageView.setImageBitmap(image);
        }
        if (outputFileUri != null) {
            this.outputFileUri = outputFileUri;
        }
    }

    public void excluirContatoFirebase(Contato contato) {
        Firebase mFirebase = firebase.child("agenda").child(contato.getId());
        mFirebase.removeValue();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (photo != null) {
            savedInstanceState.putParcelable("BitmapImageContato", photo);
        }
        if (outputFileUri != null) {
            savedInstanceState.putParcelable("outputFileUri", outputFileUri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        contato = (Contato) data.getSerializableExtra("contato");
                        setValuesElements();
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            Alert.information(getApplicationContext(), "Edição Cancelada.");
        } else {
            Alert.information(getApplicationContext(), "Erro Editar Contato.");
        }
    }
}