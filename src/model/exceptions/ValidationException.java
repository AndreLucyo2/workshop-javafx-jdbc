package model.exceptions;

import java.util.HashMap;
import java.util.Map;

// Seção 23 - 283. Validação de dados e ValidationException
//Clase exception perssonalizada que reotorna uma lista com todos os erros de uma tela
// implementa as exceptions
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Força a instanciação da exception com string
	public ValidationException(String msg)
	{
		super(msg);
	}

	// Carregar as mensagens de erros, pois cada campo pode ter uma excepiton especifica , propria
	// Coleção de pares chave valor:
	// Guarda os erros de cada campo do formulario
	// a chave é o nome do campo
	// o seundo setrine é a menagem do erro
	private Map<String, String> errors = new HashMap<>();

	// Coleção de pares chave valor:
	// pega os erros de cada campo do formulario
	public Map<String, String> getErrors()
	{
		return errors;
	}

	//Adiciona os erros na coleção
	//recebe o nome do campo e a mensage de erro no Map
	public void addError(String fieldName, String errorMessage)
	{
		//insere uma par chave e valor
		errors.put(fieldName, errorMessage);
	}
}
