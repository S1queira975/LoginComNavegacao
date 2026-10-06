package br.edu.unifaj.cc.mobile.logincomnavegacao.util;

public class ValidacaoUtils {

    public static boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
    
    public static boolean validarCpf(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        return cpfNumerico.length() == 11;
    }
    
    public static boolean validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            return false;
        }
        return senha.length() >= 6;
    }
    
    public static boolean validarVolumeDoacao(int volumeMl) {
        return volumeMl >= 200 && volumeMl <= 470;
    }

    /**
     * Faixa de bolsas coletadas em um agendamento. O agente registra o que
     * saiu da sessão de coleta, então o valor varia de um agendamento para outro.
     */
    public static boolean validarQuantidadeBolsas(int quantidade) {
        return quantidade >= 1 && quantidade <= 6;
    }

    public static String validarCamposObrigatorios(String... campos) {
        for (int i = 0; i < campos.length; i++) {
            if (campos[i] == null || campos[i].trim().isEmpty()) {
                return "Campo " + (i + 1) + " é obrigatório";
            }
        }
        return null;
    }
}