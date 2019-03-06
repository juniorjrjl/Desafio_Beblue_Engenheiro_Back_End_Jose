package br.com.discos.resolver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import br.com.discos.repository.query.filtro.FiltroListagemVenda;

public class PeriodoVendaResolver implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(FiltroListagemVenda.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		LocalDate dataInicial = (webRequest.getParameter("dataInicial") != null) ? LocalDate.parse(webRequest.getParameter("dataInicial"), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
		LocalDate dataFinal = (webRequest.getParameter("dataFinal") != null) ? LocalDate.parse(webRequest.getParameter("dataFinal"), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
		return new FiltroListagemVenda(dataInicial, dataFinal);
	}

}
