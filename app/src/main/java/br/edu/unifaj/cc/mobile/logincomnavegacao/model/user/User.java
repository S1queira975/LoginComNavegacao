package br.edu.unifaj.cc.mobile.logincomnavegacao.model.user;

/**
 * Dados de identificacao que todo usuario tem.
 *
 * Hoje existe apenas o doador, que e a unica coisa que o app cadastra. A
 * classe continua separada para que outro perfil (agente de saude, por
 * exemplo) possa reaproveitar nome, email e senha sem repetir os campos.
 */
public abstract class User {
    private String nome;
    private String email;
    private String senha;

    public User() {
    }

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
