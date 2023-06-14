/*********************************************************************

 Nome do arquivo: BottomNavigationConfig.java

 Descrição: A classe "BottomNavigationConfig" é responsável por configurar a navegação
            inferior (bottom navigation) em uma atividade Android. Ela define o comportamento
            dos itens de menu selecionados, redirecionando o usuário para diferentes atividades
            ou exibindo uma caixa de diálogo de confirmação de saída. A classe também possui métodos
            auxiliares para exibir mensagens de toast, fazer logout do aplicativo e redirecionar
            para a tela de login. Essa configuração é aplicada a duas instâncias de BottomNavigationView,
            permitindo diferentes opções de navegação dependendo da instância em uso.

 Autor: Leonardo Monteiro sa Sé Pinto

 Data: 13/06/2023

 Histórico de modificações:

 [Data da modificação]: [Breve descrição da modificação realizada]
 [Data da modificação]: [Breve descrição da modificação realizada]
 ...
 **********************************************************************/

package com.example.myfleet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationConfig {

    /**
     * Configura a navegação inferior (bottom navigation) com base nos itens de menu selecionados.
     * Redireciona o usuário para diferentes atividades ou exibe uma caixa de diálogo de confirmação de saída.
     *
     * @param activity              A atividade em que a navegação inferior está sendo configurada.
     * @param bottomNavigationView  A instância de BottomNavigationView a ser configurada.
     * @param bottomNavigationView2 Outra instância de BottomNavigationView opcional para configuração adicional.
     */
    public static void configureNavigation(final Activity activity, final BottomNavigationView bottomNavigationView, final BottomNavigationView bottomNavigationView2) {


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(activity, ActivityHome.class);
                    activity.startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_exit) {
                    showExitConfirmationDialog(activity);
                    return true;
                }
                return false;
            }
        });

        if (bottomNavigationView2 != null) {
            bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_dados) {
                        Intent intent = new Intent(activity, ActivityData.class);
                        activity.startActivity(intent);
                        return true;
                    } else if (itemId == R.id.nav_Calculadora) {
                        Intent intent = new Intent(activity, CalculatorActivity.class);
                        activity.startActivity(intent);
                        return true;
                    } else if (itemId == R.id.nav_map) {
                        Intent intent = new Intent(activity, ActivityMap.class);
                        activity.startActivity(intent);
                        return true;
                    }

                    return false;
                }
            });
        }
    }

    /**
     * Exibe uma mensagem de toast na atividade.
     *
     * @param activity A atividade em que a mensagem de toast será exibida.
     * @param message  A mensagem a ser exibida.
     */
    private static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Exibe uma caixa de diálogo de confirmação de saída.
     * Se o usuário confirmar a saída, realiza o logout e redireciona para a tela de login.
     *
     * @param activity A atividade em que a caixa de diálogo será exibida.
     */
    private static void showExitConfirmationDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Confirmação")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout(activity);
                        redirectToLogin(activity);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    /**
     * Realiza o logout do aplicativo.
     *
     * @param activity A atividade em que o logout será realizado.
     */
    private static void logout(Activity activity) {
        SessionManager sessionManager = new SessionManager(activity);
        sessionManager.clearSession();
    }

    /**
     * Redireciona para a tela de login e finaliza a atividade atual.
     *
     * @param activity A atividade atual que será finalizada.
     */
    private static void redirectToLogin(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
