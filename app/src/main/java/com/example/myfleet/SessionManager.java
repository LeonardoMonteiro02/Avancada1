/*********************************************************************

 Nome do arquivo: SessionManager.java

 Descrição: Essa classe pode ser utilizada para gerenciar a autenticação e o controle de sessão de
            um aplicativo Android, armazenando informações de login do usuário e verificando se o
            usuário está logado em diferentes partes do aplicativo.

 Autor: Leonardo Monteiro sa Sé Pinto

 Data: 13/06/2023

 Histórico de modificações:

 [Data da modificação]: [Breve descrição da modificação realizada]
 [Data da modificação]: [Breve descrição da modificação realizada]
 ...
 **********************************************************************/
package com.example.myfleet;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MyPrefs"; // Nome das preferências compartilhadas
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // Chave para indicar se o usuário está logado

    private SharedPreferences sharedPreferences; // Objeto para acessar as preferências compartilhadas
    private SharedPreferences.Editor editor; // Objeto para editar as preferências compartilhadas
    private Context context; // Contexto da aplicação

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // Inicializa as preferências compartilhadas com o nome definido
        editor = sharedPreferences.edit(); // Inicializa o editor das preferências compartilhadas
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn); // Armazena o valor de login nas preferências compartilhadas
        editor.apply(); // Aplica as alterações nas preferências compartilhadas
    }

    public void clearSession() {
        editor.clear(); // Remove todas as informações das preferências compartilhadas
        editor.apply(); // Aplica as alterações nas preferências compartilhadas
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false); // Lê o valor de login das preferências compartilhadas, retorna false se a chave não estiver definida
    }
}
