export const translateStatus = (status: string): string => {
  switch (status) {
    case "AGUARDANDO_CONFIRMACAO_PROFESSOR":
      return "aguardando confirmação do professor";
    case "EM_ANALISE":
      return "em análise";
    case "APROVADO":
      return "aprovado";
    case "RECUSADO":
      return "recusado";
    case "CANCELADO":
      return "cancelado";
    case "CONFIRMADO":
      return "confirmado";
    case "NAO_COMPARECEU":
      return "não compareceu";
    case "CONCLUIDO":
      return "Concluído";
    default:
      return "status desconhecido";
  }
};
