package med.voll.api.infra.exception;

@SuppressWarnings("serial")
public class ValidacaoException extends RuntimeException {
	public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
