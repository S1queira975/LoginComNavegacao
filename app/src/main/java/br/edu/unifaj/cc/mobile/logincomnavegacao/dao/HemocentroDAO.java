package br.edu.unifaj.cc.mobile.logincomnavegacao.dao;

import java.util.ArrayList;
import java.util.List;

import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Endereco;
import br.edu.unifaj.cc.mobile.logincomnavegacao.model.entity.Hemocentro;

public class HemocentroDAO {
    
    private static final List<Hemocentro> hemocentros = new ArrayList<>();
    
    static {
        hemocentros.add(new Hemocentro(
            "HEMO-001",
            "Hemocentro Central de São Paulo",
            new Endereco("Av. Dr. Enéas de Carvalho Aguiar", "155", "Cerqueira César", "São Paulo", "SP", "05403-000"),
            "(11) 3069-6000"
        ));
        hemocentros.get(0).setEmail("hemosp@saude.sp.gov.br");
        hemocentros.get(0).setHorarioAbertura("07:00");
        hemocentros.get(0).setHorarioFechamento("18:00");
        
        hemocentros.add(new Hemocentro(
            "HEMO-002",
            "Hemocentro do Rio de Janeiro",
            new Endereco("Rua Frei Caneca", "91", "Centro", "Rio de Janeiro", "RJ", "20211-030"),
            "(21) 3855-1400"
        ));
        hemocentros.get(1).setEmail("hemorio@ri.rj.gov.br");
        hemocentros.get(1).setHorarioAbertura("08:00");
        hemocentros.get(1).setHorarioFechamento("17:00");
        
        hemocentros.add(new Hemocentro(
            "HEMO-003",
            "Hemocentro de Belo Horizonte",
            new Endereco("Rua Arlindo Lúcio", "235", "Cachorro", "Belo Horizonte", "MG", "30622-020"),
            "(31) 3273-5600"
        ));
        hemocentros.get(2).setEmail("hemobh@mg.gov.br");
        hemocentros.get(2).setHorarioAbertura("07:00");
        hemocentros.get(2).setHorarioFechamento("18:00");
        
        hemocentros.add(new Hemocentro(
            "HEMO-004",
            "Hemocentro de Curitiba",
            new Endereco("Av. João B. G. da Silva", "500", "Batel", "Curitiba", "PR", "80730-000"),
            "(41) 3362-4500"
        ));
        hemocentros.get(3).setEmail("hemopr@pr.gov.br");
        hemocentros.get(3).setHorarioAbertura("08:00");
        hemocentros.get(3).setHorarioFechamento("17:00");
    }
    
    public static List<Hemocentro> getTodos() {
        return new ArrayList<>(hemocentros);
    }
}
