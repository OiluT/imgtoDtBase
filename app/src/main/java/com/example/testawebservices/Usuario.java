package com.example.testawebservices;

import java.io.Serializable;
import java.math.BigDecimal;

public class Usuario implements Serializable {

	private static final long serialVersionUID = -2229832341556924673L;

	private int id;
	private String nome;
	private String data;
	private byte[] foto;
	private String tipoNota;
	private String valorNota;
	private BigDecimal testeBigDecimal;

	public Usuario() {
	}

	;

	public Usuario(int id, String nome, String data, String tipoNota, String valorNota, BigDecimal testeBigDecimal) {
		this.id = id;
		this.nome = nome;
		this.data = data;
		this.tipoNota = tipoNota;
		this.valorNota = valorNota;
		this.testeBigDecimal = testeBigDecimal;

	}

	public Usuario(int id, String nome, String data, byte[] foto, String tipoNota, String valorNota, BigDecimal testeBigDecimal) {
		this.id = id;
		this.nome = nome;
		this.data = data;
		this.foto = foto;
		this.tipoNota = tipoNota;
		this.valorNota = valorNota;
		this.testeBigDecimal = testeBigDecimal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public String getValorNota() {
		return valorNota;
	}

	public void setValorNota(String valorNota) {
		this.valorNota = valorNota;
	}

	public BigDecimal getTesteBigDecimal() {
		return testeBigDecimal;
	}

	public void setTesteBigDecimal(BigDecimal testeBigDecimal) {
		this.testeBigDecimal = testeBigDecimal;
	}
}
