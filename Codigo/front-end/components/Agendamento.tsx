"use client"

import React from "react";
import AgendamentoInfoRow from "./AgendamentoInfoRow";
import { Item, Tecnico } from "./types";

interface AgendamentoProps {
  items: Item[];
  tecnico: Tecnico;
  dataHoraFim: string;
  dataHoraInicio: string;
}

const Agendamento: React.FC<AgendamentoProps> = ({ items, tecnico, dataHoraFim, dataHoraInicio }) => {
  return (
    <div className="flex justify-center">
      {items.map((item) => (
        <AgendamentoInfoRow key={item.id} item={item} tecnico={tecnico} dataHoraFim={dataHoraFim} dataHoraInicio={dataHoraInicio}/>
      ))}
    </div>
  );
};

export default Agendamento;