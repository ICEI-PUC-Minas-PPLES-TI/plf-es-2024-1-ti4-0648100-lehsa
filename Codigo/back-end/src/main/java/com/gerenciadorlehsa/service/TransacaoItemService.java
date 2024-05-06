package com.gerenciadorlehsa.service;

import com.gerenciadorlehsa.entity.TransacaoItem;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.service.interfaces.ValidadorAutorizacaoRequisicaoService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.TRANSACAO_ITEM_SERVICE;


@Slf4j(topic = TRANSACAO_ITEM_SERVICE)
@Service
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class TransacaoItemService<T extends TransacaoItem> {

    protected final ValidadorAutorizacaoRequisicaoService validadorAutorizacaoRequisicaoService;


    public abstract void atualizarStatus (@NotNull String status, @NotNull UUID id);


    protected boolean temConflitoDeData(T transacaoExistente, T novaTransacao) {
        log.info(">>> Verificando datas conflitantes: barrando agendamento solicitado em uma mesma data");

        if (transacaoExistente.getId() == novaTransacao.getId())
            return false;

        LocalDateTime dataHoraInicioExistente = transacaoExistente.getDataHoraInicio();
        LocalDateTime dataHoraFimExistente = transacaoExistente.getDataHoraFim();
        LocalDateTime dataHoraInicioNovo = novaTransacao.getDataHoraInicio();
        LocalDateTime dataHoraFimNovo = novaTransacao.getDataHoraFim();

        return (dataHoraInicioNovo.isBefore(dataHoraFimExistente) ||
                dataHoraInicioNovo.isEqual(dataHoraFimExistente)) &&
                (dataHoraFimNovo.isAfter(dataHoraInicioExistente) ||
                        dataHoraFimNovo.isEqual(dataHoraInicioExistente));
    }

    public abstract void verificarConflitoComTransacoesAprovadasOuConfirmadas(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);

    public abstract void verificarLimiteTransacaoEmAnalise(User participante);


}
