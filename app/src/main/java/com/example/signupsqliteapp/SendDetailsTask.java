package com.example.signupsqliteapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendDetailsTask extends AsyncTask<Void, Void, Boolean> {
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private Context context;

    public SendDetailsTask(Context context, DatabaseHelper databaseHelper, SessionManager sessionManager) {
        this.context = context;
        this.databaseHelper = databaseHelper;
        this.sessionManager = sessionManager;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        int userId = sessionManager.getUserId();
        User currentUser = databaseHelper.getUserById(userId);

        // Criar o objeto JSON com os detalhes do usuário
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nome", currentUser.getNome());
            jsonBody.put("username", currentUser.getUsername());
            jsonBody.put("password", currentUser.getPassword());
            jsonBody.put("foto", currentUser.getFoto());
            jsonBody.put("endereco", currentUser.getEndereco());
            jsonBody.put("email", currentUser.getEmail());
            jsonBody.put("dataNascimento", currentUser.getDataNascimento());
            jsonBody.put("sexo", currentUser.getSexo());
            jsonBody.put("cpfCnpj", currentUser.getCpfCnpj());

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try {
            //Configura do cliente OkHttp para desabilitar a verificação do certificado
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(createUnsafeSSLSocketFactory(), (X509TrustManager) createUnsafeTrustManager())
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

            // Construção da requisição
            Request request = new Request.Builder()
                    .url("https://test.avaty.com.br/Desafio/rest/desafioRest")
                    .post(RequestBody.create(MediaType.get("application/json"), jsonBody.toString()))
                    .build();

            Response response = client.newCall(request).execute();
            return response.isSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Métodos auxiliares para desabilitar a verificação do certificado
    private javax.net.ssl.SSLSocketFactory createUnsafeSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{createUnsafeTrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSLSocketFactory", e);
        }
    }

    private X509TrustManager createUnsafeTrustManager() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            // A chamada foi bem-sucedida, mostrar uma mensagem de sucesso
            showAlertDialog("Sucesso", "Dados enviados com sucesso!");
        } else {
            // A chamada falhou, mostrar uma mensagem de erro
            showAlertDialog("Erro", "Falha ao enviar os dados. Por favor, tente novamente.");
        }
    }

    // Método auxiliar para mostrar um AlertDialog
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
