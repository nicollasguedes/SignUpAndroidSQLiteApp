package com.example.signupsqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "SignLog.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "SignLog.db", null, 1);
    }



    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "foto TEXT," +
                "endereco TEXT," +
                "email TEXT," +
                "dataNascimento TEXT," + // Alterado para TEXT
                "sexo INTEGER," +
                "tipo TEXT," +
                "cpfCnpj TEXT" +
                ")";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    public Boolean insertData(Object object) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = objectToContentValues(object);
        long result = MyDatabase.insert("users", null, contentValues);

        return result != -1;
    }
    public boolean updateData(User user, String userId) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = objectToContentValues(user);
        int rowsAffected = MyDatabase.update("users", contentValues, "id = ?", new String[]{userId});

        return rowsAffected > 0;
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(userId) };
        db.delete("users", whereClause, whereArgs);
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});

        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});

        return cursor.getCount() > 0;
    }

    public Boolean checkRegistry(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});
        List<User> userQuery = cursorToUserList(cursor);

        return userQuery.size() > 0 && isValidUser(userQuery.get(0));
    }

    public List<User> getUsersByEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        return cursorToUserList(cursor);
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE email = ?", new String[]{email});

        int userIdIndex = cursor.getColumnIndex("id");
        if (cursor.moveToFirst() && userIdIndex != -1) {
            userId = cursor.getInt(userIdIndex);
        }

        cursor.close();
        return userId;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        // Verifica se há resultados e obtenha os atributos do usuário
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nomeIndex = cursor.getColumnIndex("nome");
            int usernameIndex = cursor.getColumnIndex("username");
            int passwordIndex = cursor.getColumnIndex("password");
            int fotoIndex = cursor.getColumnIndex("foto");
            int enderecoIndex = cursor.getColumnIndex("endereco");
            int emailIndex = cursor.getColumnIndex("email");
            int dataNascimentoIndex = cursor.getColumnIndex("dataNascimento");
            int sexoIndex = cursor.getColumnIndex("sexo");
            int tipoIndex = cursor.getColumnIndex("tipo");
            int cpfCnpjIndex = cursor.getColumnIndex("cpfCnpj");

            String userNome = cursor.getString(nomeIndex);
            String userUsername = cursor.getString(usernameIndex);
            String userPassword = cursor.getString(passwordIndex);
            String userFoto = cursor.getString(fotoIndex);
            String userEndereco = cursor.getString(enderecoIndex);
            String userEmail = cursor.getString(emailIndex);
            String userDataNascimento = cursor.getString(dataNascimentoIndex);
            int userSexo = cursor.getInt(sexoIndex);
            String userTipo = cursor.getString(tipoIndex);
            String userCpfCnpj = cursor.getString(cpfCnpjIndex);

            user = new User(userId, userNome, userUsername, userPassword, userFoto, userEndereco,
                    userEmail, userDataNascimento, userSexo, userTipo, userCpfCnpj);
        }
        cursor.close();
        return user;
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor query =  db.rawQuery("SELECT * FROM users", null);
        List<User> userList = cursorToUserList(query);
        query.close();
        return userList;
    }

    private List<User> cursorToUserList(Cursor cursor) {
        List<User> userList = new ArrayList<>();

        int idIndex = cursor.getColumnIndex("id");
        int nomeIndex = cursor.getColumnIndex("nome");
        int usernameIndex = cursor.getColumnIndex("username");
        int passwordIndex = cursor.getColumnIndex("password");
        int fotoIndex = cursor.getColumnIndex("foto");
        int enderecoIndex = cursor.getColumnIndex("endereco");
        int emailIndex = cursor.getColumnIndex("email");
        int dataNascimentoIndex = cursor.getColumnIndex("dataNascimento");
        int sexoIndex = cursor.getColumnIndex("sexo");
        int tipoIndex = cursor.getColumnIndex("tipo");
        int cpfCnpjIndex = cursor.getColumnIndex("cpfCnpj");

        while (cursor.moveToNext()) {
            int userId = cursor.getInt(idIndex);
            String userNome = cursor.getString(nomeIndex);
            String userUsername = cursor.getString(usernameIndex);
            String userPassword = cursor.getString(passwordIndex);
            String userFoto = cursor.getString(fotoIndex);
            String userEndereco = cursor.getString(enderecoIndex);
            String userEmail = cursor.getString(emailIndex);
            String userDataNascimento = cursor.getString(dataNascimentoIndex);
            int userSexo = cursor.getInt(sexoIndex);
            String userTipo = cursor.getString(tipoIndex);
            String userCpfCnpj = cursor.getString(cpfCnpjIndex);

            User user = new User(userId, userNome, userUsername, userPassword, userFoto, userEndereco,
                    userEmail, userDataNascimento, userSexo, userTipo, userCpfCnpj);
            userList.add(user);
        }

        cursor.close();
        return userList;
    }

    private ContentValues objectToContentValues(Object object) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    if (value != null && !field.getName().equals("id")) {
                        if (field.getType() == String.class) {
                            contentValues.put(field.getName(), (String) value);
                        } else if (field.getType() == Integer.TYPE) {
                            contentValues.put(field.getName(), (Integer) value);
                        } else if (field.getType() == Long.TYPE) {
                            contentValues.put(field.getName(), (Long) value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return contentValues;
    }

    public boolean isValidUser(User user) {
        return user != null &&
                user.getNome() != null && !user.getNome().isEmpty() &&
                user.getUsername() != null && !user.getUsername().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty() &&
                user.getFoto() != null && !user.getFoto().isEmpty() &&
                user.getEndereco() != null && !user.getEndereco().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty() &&
                user.getDataNascimento() != null && !user.getDataNascimento().isEmpty() &&
                user.getTipo() != null && !user.getTipo().isEmpty() &&
                user.getCpfCnpj() != null && !user.getCpfCnpj().isEmpty();
    }

}