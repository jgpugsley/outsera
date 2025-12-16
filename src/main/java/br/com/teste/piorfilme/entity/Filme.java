package br.com.teste.piorfilme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "filme")
public class Filme {
	public Filme() { 
    }
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="ano")
	private int ano;
	@Column(name="produtor")
	private String produtor;
	@Column(name="venceu")
	private Boolean venceu;
	@Column(name="nome")
	private String nome;
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
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
	
	public Boolean getVenceu() {
		return venceu;
	}
	public void setVenceu(Boolean venceu) {
		this.venceu = venceu;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	


}