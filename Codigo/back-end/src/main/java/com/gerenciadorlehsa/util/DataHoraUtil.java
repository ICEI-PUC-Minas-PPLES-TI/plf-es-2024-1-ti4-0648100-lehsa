package com.gerenciadorlehsa.util;

import com.gerenciadorlehsa.exceptions.lancaveis.DataException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.DATA_HORA_UTIL;

@UtilityClass
@Slf4j(topic = DATA_HORA_UTIL)
public class DataHoraUtil {
    public static final DateTimeFormatter FORMATAR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    public static String converterDataHora(LocalDateTime dataHora) {
        return dataHora.format (FORMATAR_DATA_HORA);
    }

    public static LocalDateTime converterDataHora(String dataHora) {
        if(dataHora == null)
            throw new DataException ("Não é permitido ficar sem data e hora");
        try {
            return LocalDateTime.parse (dataHora, FORMATAR_DATA_HORA);
        } catch (DateTimeParseException e) {
            throw new DataException ("Formato de data inválido");
        }
    }

    public static void dataValida(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim){
        boolean NaoEhValida = dataHoraInicio.isBefore (LocalDateTime.now ()) ||
                dataHoraFim.isBefore (LocalDateTime.now ()) ||
                dataHoraFim.isBefore (dataHoraInicio);
        if(NaoEhValida)
            throw new DataException ("Data inválida!");
    }


}
