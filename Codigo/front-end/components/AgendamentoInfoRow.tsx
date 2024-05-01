"use client";

import React from "react";
import ImageComp from "./ImageComp";
import { Item, Tecnico } from "./types";

interface AgendamentoInfoRowProps {
  item: Item;
  tecnico: Tecnico;
  dataHoraFim: string;
  dataHoraInicio: string;
}

const AgendamentoInfoRow: React.FC<AgendamentoInfoRowProps> = ({
  item,
  tecnico,
  dataHoraFim,
  dataHoraInicio,
}) => {
  return (
    <div className="mb-5">
      <div className="w-[42rem] h-[10rem] bg-white rounded-[20px] relative mb-1">
        <div className="flex justify-between w-full h-[3rem] bg-primary rounded-t-[20px] items-center z-10">
          <h1 className="ml-[9.5rem] text-white text-lg">{item.nome}</h1>
        </div>
        <div className="flex flex-row pt-1 pl-1">
          <div className="w-[8rem] h-[8rem] bg-gray-200 rounded-[10px] absolute left-[1rem] top-[1rem] z-0"></div>
          <ImageComp
            src={`http://localhost:8080/item/img/${item.id}`}
            alt="item picture"
            width={400}
            height={400}
            className="object-cover w-[8rem] h-[8rem] bg-gray-200 rounded-[10px] absolute left-[1rem] top-[1rem] z-0"
          />
        </div>
        <div className="ml-[10rem] pt-1">
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
    </div>
  );
};

export default AgendamentoInfoRow;
