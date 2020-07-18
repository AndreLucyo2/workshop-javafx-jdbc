package model.exceptions;

import java.util.HashMap;
import java.util.Map;

// Se��o 23 - 283. Valida��o de dados e ValidationException
//Clase exception perssonalizada que reotorna uma lista com todos os erros de uma tela
// implementa as exceptions
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// For�a a instancia��o da exception com string
	public ValidationException(String msg)
	{
		super(msg);
	}

	// Carregar as mensagens de erros, pois cada campo pode ter uma excepiton especifica , propria
	// Cole��o de pares chave valor:
	// Guarda os erros de cada campo do formulario
	// a chave � o nome do campo
	// o seundo setrine � a menagem do erro
	private Map<String, String> errors = new HashMap<>();

	// Cole��o de pares chave valor:
	// pega os erros de cada campo do formulario
	public Map<String, String> getErrors()
	{
		return errors;
	}

	//Adiciona os erros na cole��o
	//recebe o nome do campo e a mensage de erro no Map
	public void addError(String fieldName, String errorMessage)
	{
		//insere uma par chave e valor
		errors.put(fieldName, errorMessage);
	}
}
