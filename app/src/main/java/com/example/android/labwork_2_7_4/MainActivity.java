package com.example.android.labwork_2_7_4;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;

    private ListView listViewContacts;

    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnReadContacts = findViewById(R.id.button_read);
        listViewContacts = findViewById(R.id.contactList);

        // получаем разрешения
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        // если устройство до API 23, устанавливаем разрешение
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true;
        } else {
            // вызываем диалоговое окно для установки разрешений для API > 23
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        // если разрешение установлено, загружаем контакты через кнопку btnReadContacts
        if (!READ_CONTACTS_GRANTED) {
            Toast.makeText(this, "Требуется установить разрешения",
                    Toast.LENGTH_LONG).show();
        }

        btnReadContacts.setOnClickListener(v -> loadContacts());

    }

    @SuppressLint("Range")
    private void loadContacts() {

        ContentResolver contentResolver = getContentResolver();
        cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        // Не смог вытащить телефоны из контактов. Вместо них - ID контактов
        String[] from = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts._ID};
        int[] too = new int[]{R.id.item_name, R.id.item_telephone};

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                MainActivity.this,
                R.layout.contacts_list_item,
                cursor,
                from, too,
                0
        );

        listViewContacts.setAdapter(cursorAdapter);

    }

}