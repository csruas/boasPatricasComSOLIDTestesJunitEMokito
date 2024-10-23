package med.voll.api.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

@Service
public class AgendadeConsultas {
	
	@Autowired
	private ConsultaRepository consultaRepository;  
	
	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private List<ValidadorAgendamentoDeConsulta> validadores;
	
	public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
		
		if(!pacienteRepository.existsById(dados.idPaciente())) {
    		throw new ValidationException("ID do paciente informado não existe!");
    	}
    	
    	if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
    		throw new ValidationException("ID do medico informado não existe!");
    	}
    	
    	validadores.forEach(v -> v.validar(dados));
    	
		var paciente = pacienteRepository.findById(dados.idPaciente()).get();
		var medico =  escolherMedico(dados);
		if(medico == null) {
			throw new ValidationException("Não existe Medico disponivel!");
		}
		var consulta = new Consulta(null, medico, paciente, dados.data());
		
		
		consultaRepository.save(consulta);
		
		return new DadosDetalhamentoConsulta(consulta);
		
	}
	
	private Medico escolherMedico(DadosAgendamentoConsulta dados) {
		if(dados.idMedico() !=null) {
			return medicoRepository.getReferenceById(dados.idMedico());
		}
		
		if(dados.especialidade() == null ) {
			throw new ValidationException("Especialidade e obrigatorio quando médico não for escolhido!");
			
		}
		
//		return null;
		
		return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
	}

}
