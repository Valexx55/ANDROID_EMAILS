package com.example.formador.cuentasmail;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String[] componerListaCorreos(String listaxcomas) {
        String[] lista_correos = null;

        lista_correos = listaxcomas.split(","); //VALE, VAMOS, HOLA --> [vale, vamos, hola]

        return lista_correos;
    }

    private String[] getCuentasMail(AccountManager accountManager) {
        String[] cuentas_mail = null;

        //TODO recueperar las cuentas de EMAIL

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Account[] lista_cuentas = accountManager.getAccounts();
            String str_aux = new String();

            for (Account cuenta : lista_cuentas)
            {
                Log.d(getClass().getCanonicalName(), " Cuenta = " + cuenta.name);
                if (cuenta.type.equals("com.google")) //si la cuenta es de gmail
                {
                    str_aux = str_aux + cuenta.name+",";
                }
            }

            if (str_aux.length() != 0)
            {
                cuentas_mail = componerListaCorreos(str_aux.substring(0, str_aux.length() - 1));
            } else //cuentas = 0
                {
                    Log.d(getClass().getCanonicalName(), " NO HAY CUENTAS ");
                }

        return cuentas_mail;
    }

    private boolean tengoPermisosCuenta ()
    {
        boolean permiso = false;

             permiso = (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)== PackageManager.PERMISSION_GRANTED);

        return permiso;
    }

    private void pidoPermisos ()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 100);

    }

    private void obtenerCorreos ()
    {
        AccountManager accountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        String[] correos_usuario = getCuentasMail(accountManager);

        if (correos_usuario != null)
        {
            for (String correo : correos_usuario)
            {
                Log.d(getClass().getCanonicalName(), "CORREO = " + correo);
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (tengoPermisosCuenta ())
        {
            obtenerCorreos();

        } else
            {
                pidoPermisos();
                Log.d(getClass().getCanonicalName(), "SIN PERMISOS");

            }

    }

    private boolean permiso_concedido (int[] array_res) {
        return (array_res.length>0 && array_res[0] == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (permiso_concedido(grantResults)) {
                obtenerCorreos();


            } else {
                finish();
            }
        }


    }
}
