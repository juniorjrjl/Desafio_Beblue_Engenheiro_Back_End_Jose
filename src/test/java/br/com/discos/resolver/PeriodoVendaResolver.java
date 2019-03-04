package br.com.discos.resolver;

import java.time.LocalDateTime;
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
		LocalDateTime dataInicial = (webRequest.getParameter("dataInicial") != null) ? LocalDateTime.parse(webRequest.getParameter("dataInicial"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
		LocalDateTime dataFinal = (webRequest.getParameter("dataFinal") != null) ? LocalDateTime.parse(webRequest.getParameter("dataFinal"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
		return new FiltroListagemVenda(dataInicial, dataFinal);
	}

}
