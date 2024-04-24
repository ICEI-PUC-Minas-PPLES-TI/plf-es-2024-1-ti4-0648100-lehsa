package com.gerenciadorlehsa.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static com.gerenciadorlehsa.util.ConstantesTopicosUtil.DATA_HORA_UTIL;

@UtilityClass
@Slf4j(topic = DATA_HORA_UTIL)
public class DataHoraUtil {
    public static final DateTimeFormatter FORMATAR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String converterDataHora(LocalDateTime dataHora) {
        return dataHora.format (FORMATAR_DATA_HORA);
    }

}
