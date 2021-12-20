package com.example.testawebservices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        Button btLogin = (Button)findViewById(R.id.btLogin);
        Button bSair = (Button)findViewById(R.id.bSair);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tLogin = (TextView)findViewById(R.id.tLogin);
                TextView tSenha = (TextView)findViewById(R.id.tSenha);
                String login = tLogin.getText().toString();
                String senha = tSenha.getText().toString();

                if(login.equals("t")&&senha.equals("1")){
                    String nomePasta = "Túlio Mariano de Oliveira";
                    Bundle pasta = new Bundle();
                    pasta.putString("paste", nomePasta);
                    Intent intent = new Intent(Login.this, CadastroActivity.class);
                    intent.putExtras(pasta);
                    startActivity(intent);

                }else{
                    alert("Login ou Senha incorretos");
                }

                if(login.equals("p")&&senha.equals("1")){
                    String nomePasta = "Paulo Prudente";
                    Bundle pasta = new Bundle();
                    pasta.putString("paste", nomePasta);
                    Intent intent = new Intent(Login.this, CadastroActivity.class);
                    intent.putExtras(pasta);
                    startActivity(intent);

                }

            }
        });
        bSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert("Volte Sempre!!");
                finishAffinity();
            }
        });
    }
 private void alert (String s){
     Toast.makeText(this,s,Toast.LENGTH_LONG).show();
 }
}