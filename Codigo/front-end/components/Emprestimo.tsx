"use client"

import React from "react";
import EmprestimoInfoRow from "./EmprestimoInfoRow";
import { Item, Tecnico } from "./types";

interface EmprestimoProps {
  items: Item[];
  tecnico: Tecnico;
  dataHoraFim: string;
  dataHoraInicio: string;
}

const Emprestimo: React.FC<EmprestimoProps> = ({ items, tecnico, dataHoraFim, dataHoraInicio }) => {
  return (
    <div className="flex justify-center">
      {items.map((item) => (
        <EmprestimoInfoRow key={item.id} item={item} tecnico={tecnico} dataHoraFim={dataHoraFim} dataHoraInicio={dataHoraInicio}/>
      ))}
    </div>
  );
};

export default Emprestimo;