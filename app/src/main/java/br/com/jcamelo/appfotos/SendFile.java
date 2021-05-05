package br.com.jcamelo.appfotos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.jcamelo.appfotos.model.ClassFtp;

public class SendFile extends AppCompatActivity {

    private ClassFtp conectFTP;
    private List<File> fileListAll;
    private TextView status;
    private MaterialButton buttonSendFtp;
    private ProgressBar progressBar;
    private ImageView check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        status = findViewById(R.id.text_view_sending);
        buttonSendFtp = findViewById(R.id.button_send_ftp);
        progressBar = findViewById(R.id.progressBar);
        check = findViewById(R.id.image_view_check);

        fileListAll = searchFileSend();
        conectFTP = new ClassFtp();
        SendAsync sendAsync = new SendAsync();


        buttonSendFtp.setOnClickListener(v -> {
            if (fileListAll.size() != 0) {
                status.setText("Enviando");
                sendAsync.execute();
            } else {
                Toast.makeText(this, "Não existe arquivo para ser enviado", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private class SendAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            buttonSendFtp.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            boolean access = conectFTP.connect("ftp.armonive.net.br", "appfotos@armonive.net.br",
                    "M5M(;bDf.tOn", 21);
            if (access) {
                for (int i = 0; i < fileListAll.size(); i++) {
                    boolean isSent = conectFTP.upload(fileListAll.get(i).getPath(), fileListAll.get(i).getName());
                    if (isSent) {
                        deleteFile(fileListAll.get(i));
                    } else {
                        Toast.makeText(SendFile.this, "Arquivo: " + fileListAll.get(i).getName()
                                        + " não pode ser deletado",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                conectFTP.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            status.setText("Processo finalizado");
            check.setVisibility(View.VISIBLE);


            new Handler(Looper.getMainLooper()).postDelayed(SendFile.this::onBackPressed,
                    3000);

        }
    }

    private boolean upload(String path, String name) {
        Log.d("TESTFILESEND", path + name);
        return true;
    }


    private List<File> searchFileSend() {
        List<File> list = new ArrayList<>();
        String[] folder = getResources().getStringArray(R.array.folder);
        for (int i = 0; i < folder.length; i++) {
            String path = getExternalFilesDir(folder[i]).toString();
            File directory = new File(path);
            File[] files = directory.listFiles();

            for (File file : files) {
                list.add(file);
            }
        }

        String sizeList = "Você tem: " + list.size() + " arquivo para enviar";
        status.setText(sizeList);
        return list;
    }

    private void deleteFile(File file) {
        file.delete();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}