"use client";

import React from "react";
import ImageComp from "./ImageComp";
import { Item, Tecnico } from "./types";

interface EmprestimoInfoRowProps {
  item: Item;
  tecnico: Tecnico;
  dataHoraFim: string;
  dataHoraInicio: string;
}

const EmprestimoInfoRow: React.FC<EmprestimoInfoRowProps> = ({
  item,
  tecnico,
  dataHoraFim,
  dataHoraInicio,
}) => {
  return (
    <div className="h-[10rem] bg-white relative mb-5 w-[42rem] shadow-xl rounded-[20px]">
      <div className="flex justify-between w-full h-[3rem] bg-primary rounded-t-[20px] items-center z-10">
        <h1 className="ml-[11rem] text-white text-lg">{item.nome}</h1>
      </div>
      <div className="flex flex-row pt-1 pl-1">
        <div className="w-[10rem] h-[10rem] rounded-l-[20px] absolute left-[0rem] top-[0rem] z-0"></div>
        <ImageComp
          src={`http://localhost:8080/item/img/${item.id}`}
          alt="item picture"
          width={400}
          height={400}
          className="object-fill w-[10rem] h-[10rem] rounded-l-[19px] absolute left-[0rem] top-[0rem] z-0"
        />
      </div>
      <div className="ml-[11rem] pt-1">
        {tecnico ? (
          <>
            <div className="flex flex-row justify-around gap-1">
              <div className="flex flex-col">
                <p>Técnico: </p>
                <p>{tecnico.nome}</p>
              </div>
              <div className="flex flex-col">
                <p>Email: </p>
                <p> {tecnico.email}</p>
              </div>
              <div className="flex flex-col">
                <p>Telefone: </p>
                <p> {tecnico.telefone}</p>
              </div>
            </div>
            {/* <h2 className="font-bold">Status:</h2>
                      <span
                          className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                            agendamento?.statusTransacaoItem === 'APROVADO' ? "bg-green-100 text-green-800" :
                            agendamento?.statusTransacaoItem === 'EM_ANALISE' ? "bg-yellow-100 text-yellow-800" :
                            agendamento?.statusTransacaoItem === 'RECUSADO' ? "bg-red-100 text-red-800" :
                            'bg-gray-500 text-white'
                          }`}
                        >
                          {agendamento?.statusTransacaoItem}
                      </span> */}
          </>
        ) : (
          <p>Técnico não especificado</p>
        )}
        <div className="flex flex-row justify-between pt-6 pr-6">
          <p className=" text-sm">Início: {dataHoraInicio}</p>
          <p className=" text-sm">Fim: {dataHoraFim}</p>
        </div>
      </div>
    </div>
  );
};

export default EmprestimoInfoRow;
