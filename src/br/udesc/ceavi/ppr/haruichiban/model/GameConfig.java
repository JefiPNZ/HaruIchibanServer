package br.udesc.ceavi.ppr.haruichiban.model;

import java.awt.Color;
import java.io.Serializable;

public class GameConfig implements Serializable {

	private static final long serialVersionUID = 5832535096318841168L;
	private String estacao;
	private int tamanho;
	private Color topColor, bottonColor;

	public GameConfig() {
	}

	public String getEstacao() {
		return estacao;
	}

	public void setEstacao(String estacao) {
		this.estacao = estacao;
	}

	public int getTamanhoDeck() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public boolean isComplete() {
		return !estacao.isEmpty() && tamanho != 0;
	}

	public void setBottonColor(Color botton) {
		this.bottonColor = botton;
	}

	public void setTopColor(Color topColor) {
		this.topColor = topColor;
	}

	public Color getColorBotton() {
		return bottonColor;
	}

	public Color getColorTop() {
		return topColor;
	}
}
