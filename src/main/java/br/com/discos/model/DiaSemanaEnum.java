package br.com.discos.model;

import java.time.DayOfWeek;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiaSemanaEnum {

	DOMINGO(DayOfWeek.SUNDAY.getValue()),
	SEGUNDA(DayOfWeek.MONDAY.getValue()),
	TERCA(DayOfWeek.THURSDAY.getValue()),
	QUARTA(DayOfWeek.WEDNESDAY.getValue()),
	QUINTA(DayOfWeek.TUESDAY.getValue()),
	SEXTA(DayOfWeek.FRIDAY.getValue()),
	SABADO(DayOfWeek.SATURDAY.getValue());
	
	private int codigo;
	
}
