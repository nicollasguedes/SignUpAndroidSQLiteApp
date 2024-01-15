package com.example.signupsqliteapp;

import androidx.annotation.NonNull;

import java.util.Objects;

public class User {
    private int id;
    private String nome;
    private String username;
    private String password;
    private String foto;
    private String endereco;
    private String email;
    private String dataNascimento;
    private int sexo;
    private String tipo;
    private String cpfCnpj;


    public User() {
    }

    public User(int id, String nome, String username, String password, String foto, String endereco, String email, String dataNascimento, int sexo, String tipo, String cpfCnpj) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.foto = foto;
        this.endereco = endereco;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.tipo = tipo;
        this.cpfCnpj = cpfCnpj;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFoto() {
        return foto;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEmail() {
        return email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public int getSexo() {
        return sexo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    // Métodos setters

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, username, password, foto, endereco, email, dataNascimento, sexo, tipo, cpfCnpj);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", foto='" + foto + '\'' +
                ", endereco='" + endereco + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", sexo=" + sexo +
                ", tipo='" + tipo + '\'' +
                ", cpfCnpj='" + cpfCnpj + '\'' +
                '}';
    }
}

