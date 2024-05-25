"use client";

import React from "react";
import ImageComp from "./ImageComp";
import { Item, Tecnico, Professor, Solicitante } from "./types";

interface AgendamentoProps {
  items: Item[];
  tecnico: Tecnico;
  professor: Professor;
  solicitantes: Solicitante[];
  dataHoraFim: string;
  dataHoraInicio: string;
  observacaoSolicitacao: string;
  statusTransacaoItem: string;
}

const Agendamento: React.FC<AgendamentoProps> = ({
  items,
  tecnico,
  professor,
  solicitantes,
  dataHoraFim,
  dataHoraInicio,
  observacaoSolicitacao,
  statusTransacaoItem,
}) => {
  return (
    <div className="flex flex-row justify-between my-2 w-[60rem] min-h-[12rem] bg-white border shadow-md rounded-lg">
      <div className="flex flex-col w-[28rem] justify-center py-4 items-center">
        <h2 className="text-xl font-bold mb-2">Itens</h2>
        <div className="grid grid-cols-4 gap-2">
          {items.map((item) => (
            <div key={item.id}>
              <ImageComp
                src={`http://localhost:8080/item/img/${item.id}`}
                alt="item picture"
                width={400}
                height={400}
                className="object-fill w-[5rem] h-[5rem] m-4 rounded-lg"
              />

              <h1 className="text-md font-semibold text-center">{item.nome}</h1>
            </div>
          ))}
        </div>
      </div>
      <div className="w-px bg-gray-300 mx-2 my-4"></div> {/*separator*/}
      <div className="p-4 w-[28rem]">
        <h2 className="text-xl font-bold mb-2">Detalhes</h2>
        <ul className="text-gray-500 dark:text-gray-400">
          {/* <li className="flex space-x-2">
            <strong>Solicitantes:</strong>{" "}
            {solicitantes.map((solicitante) => solicitante.email).join(", ")}
          </li> */}
          <li className="flex space-x-2">
            <strong>Data Hora Início:</strong> {dataHoraInicio}
          </li>
          <li className="flex space-x-2">
            <strong>Data Hora Fim:</strong> {dataHoraFim}
          </li>
          <h1 className="my-4" />
          <span
            className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
              statusTransacaoItem === "APROVADO"
                ? "bg-green-100 text-green-800"
                : statusTransacaoItem === "EM_ANALISE"
                ? "bg-yellow-100 text-yellow-800"
                : statusTransacaoItem === "RECUSADO"
                ? "bg-red-100 text-red-800"
                : "bg-gray-500 text-white"
            }`}
          >
            {statusTransacaoItem}
          </span>
          <h1 className="my-2" />
          <li className="flex space-x-2">
            <strong>Observação:</strong> {observacaoSolicitacao}
          </li>
        </ul>
      </div>
    </div>
  );
};

export default Agendamento;
