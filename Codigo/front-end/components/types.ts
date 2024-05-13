enum TipoItem {
  "VIDRARIA",
  "EQUIPAMENTO",
}

export interface Item {
  id: string;
  imagem: File;
  tipoItem: TipoItem;
  quantidade: number;
  valorUnitario: number;
  nome: string;
  emprestavel: boolean;
  nomeImg: string;
}

export interface Tecnico {
  id: string;
  curso: null;
  email: string;
  nome: string;
  telefone: string;
}

export interface Agendamento {
  id: string;
  dataHoraFim: string;
  dataHoraInicio: string;
  tecnicoResponsavel: string; // Nome do tecnico responsavel
  contato: string; // Contato do responsavel
  itens: Item[];
  tecnico: Tecnico;
}

export interface Emprestimo {
  id: string;
  dataHoraFim: string;
  dataHoraInicio: string;
  tecnicoResponsavel: string; // Nome do tecnico responsavel
  contato: string; // Contato do responsavel
  itens: Item[];
  tecnico: Tecnico;
}