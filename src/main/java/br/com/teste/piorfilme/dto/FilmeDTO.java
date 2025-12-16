package br.com.teste.piorfilme.dto;

public class FilmeDTO {
    private Integer ano;
    private String produtor;
    private String nome;

    // Construtor vazio
    public FilmeDTO() {
    }

    // Construtor com parâmetros
    public FilmeDTO(Integer ano, String produtor, String nome) {
        this.ano = ano;
        this.produtor = produtor;
        this.nome = nome;
    }

    // Getters e Setters
    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getProdutor() {
        return produtor;
    }

    public void setProdutor(String produtor) {
        this.produtor = produtor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "FilmeDTO{" +
                "ano=" + ano +
                ", produtor='" + produtor + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}