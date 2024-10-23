package med.voll.api.domain.consulta.cancelar;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
		 @NotNull
	        Long idConsulta,

	        @NotNull
	        MotivoCancelamento motivo) {

}
