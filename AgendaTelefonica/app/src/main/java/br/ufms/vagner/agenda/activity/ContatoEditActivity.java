package br.ufms.vagner.agenda.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import br.ufms.vagner.agenda.R;
import br.ufms.vagner.agenda.model.Contato;
import br.ufms.vagner.agenda.util.Alert;
import br.ufms.vagner.agenda.util.BitmapUtils;
import br.ufms.vagner.agenda.util.CropOption;
import br.ufms.vagner.agenda.util.CropOptionAdapter;
import br.ufms.vagner.agenda.util.FireBaseUtil;
import br.ufms.vagner.agenda.util.ImageConverter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ContatoEditActivity extends AppCompatActivity {

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
    @Bind(R.id.file_selector)
    public Button buttonSelectImagem;
    private Contato contato;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Bitmap photo;
    private Uri outputFileUri;
    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_cadastro);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        firebase = FireBaseUtil.getFirebase();
        initComponent();
        menuAlertDialogSelecionarImagem();
    }

    private void initComponent() {
        // Toolbar Title
        getSupportActionBar().setTitle(getString(R.string.edicao));
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

    public void atualizarContatoFirebase(Contato contato) {
        Firebase mFirebase = firebase.child("agenda");
        mFirebase.child(contato.getId()).setValue(contato);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contato_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (atualiza()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_success_atualizado), Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(getBaseContext(), ContatoEditActivity.class);
                    Intent intent = new Intent();
                    intent.putExtra("contato", contato);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    doCrop();
                    break;
                case PICK_FROM_FILE:
                    mImageCaptureUri = data.getData();
                    doCrop();
                    break;
                case CROP_FROM_CAMERA:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        photo = extras.getParcelable("data");
                        //Salvar Imagem
                        File filePhoto = storeImage(photo);
                        outputFileUri = Uri.fromFile(filePhoto);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        try {
                            photo = BitmapFactory.decodeStream(new FileInputStream(filePhoto), null, options);
                            photo = ImageConverter.getRoundedCornerBitmap(photo, 100);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        mImageView.setImageBitmap(photo);
                    }
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists()) {
                        f.delete();
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            Alert.information(getApplicationContext(), "Seleção de Imagem Cancelada.");
        } else {
            Alert.information(getApplicationContext(), "Erro ao selecionar Imagem.");
        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Não foi possível Recortar a Imagem!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }
                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("App de Edição");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mImageCaptureUri != null) {
                            getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private File storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            System.out.println("Error creating media file, check storage permissions");
            return pictureFile;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error accessing file: " + e.getMessage());
        }
        return pictureFile;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName() + "/images");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = timeStamp + "_" + getRandomString(10) + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < sizeOfRandomString; ++i) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        return sb.toString();
    }

    public boolean validaCampos() {
        // Validação do Input Nome
        if (TextUtils.isEmpty(mNome.getText().toString())) {
            mNome.setError("Informe o Nome.");
            mNome.setHintTextColor(Color.parseColor("#110F10"));
            mNome.requestFocus();
            mNome.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mNome.setError(null);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mNome.setError(null);
                }

                @Override
                public void afterTextChanged(Editable edt) {
                    mNome.setError(null);
                }
            });

            return false;
        }
        // Validação do Input Descrição
        else if (TextUtils.isEmpty(mTelefone.getText().toString())) {
            mTelefone.setError("Informe o Telefone");
            mTelefone.setHintTextColor(Color.parseColor("#110F10"));
            mTelefone.requestFocus();
            mTelefone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable edt) {
                    mTelefone.setError(null);
                }
            });
            return false;
        } else {
            return true;
        }
    }

    public void menuAlertDialogSelecionarImagem() {
        final String[] items = new String[]{"Câmera", "Selecionar da Galeria"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecionar Imagem");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {//Imagem from camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //Imagem from arquivo
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
                }
            }
        });
        final AlertDialog dialog = builder.create();
        buttonSelectImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public void getCampos() {
        contato.setNome(mNome.getText().toString());
        contato.setTelefone(mTelefone.getText().toString());
        contato.setEmail(mEmail.getText().toString());
        if (contato.getImagem() != null && !contato.getImagem().equals("")) {
            contato.setImagem(new File(outputFileUri.getPath()).getName());
        } else {
            contato.setImagem("");
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public boolean atualiza() {
        // Valida os Campos de Nome, Descrição e Preço
        if (!validaCampos()) {
            return false;
        }
        // Seta os Dados do Contato
        getCampos();
        //Salva o Contato na Lista
        atualizarContatoFirebase(contato);
        return true;
    }

    public void finalizarContato() {
        Intent intent = new Intent(getBaseContext(), ContatoEditActivity.class);
        intent.putExtra("novoContato", contato);
        intent.putExtra("updateLista", true);
        intent.putExtra("mensagem", getString(R.string.message_success_cadastro));
        setResult(RESULT_OK, intent);
        finish();
    }
}