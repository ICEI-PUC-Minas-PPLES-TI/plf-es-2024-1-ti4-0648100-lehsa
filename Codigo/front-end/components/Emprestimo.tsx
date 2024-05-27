"use client";

import React, { useEffect, useState } from "react";
import ImageComp from "./ImageComp";
import { Item, Solicitante } from "./types";
import { differenceInDays, parse, format } from "date-fns";

interface EmprestimoProps {
  items: Item[];
  solicitante: Solicitante;
  dataHoraFim: string;
  dataHoraInicio: string;
  observacaoSolicitacao: string;
  statusTransacaoItem: string;
}

const Emprestimo: React.FC<EmprestimoProps> = ({
  items,
  dataHoraFim,
  dataHoraInicio,
  observacaoSolicitacao,
  statusTransacaoItem,
}) => {
  const [currentDuration, setCurrentDuration] = useState(0);
  const [totalDuration, setTotalDuration] = useState(0);
  const [daysRemaining, setDaysRemaining] = useState(0);

  useEffect(() => {
    const dateFormat = "dd/MM/yyyy HH:mm:ss";
    const start = parse(dataHoraInicio, dateFormat, new Date());
    const end = parse(dataHoraFim, dateFormat, new Date());
    const now = new Date();
    // const now = new Date(
    //   "Sat June 25 2024 11:36:22 GMT-0300 (Horário Padrão de Brasília)"
    // );

    if (isNaN(start.getTime()) || isNaN(end.getTime())) {
      console.error("Formato de data inválido");
      return;
    }

    const totalDays = differenceInDays(end, start);
    const currentDays = differenceInDays(now, start);
    const remainingDays = differenceInDays(end, now);

    console.log(`Start: ${format(start, dateFormat)}`);
    console.log(`End: ${format(end, dateFormat)}`);
    console.log(`Now: ${format(now, dateFormat)}`);
    console.log(`Total Days: ${totalDays}`);
    console.log(`Current Days: ${currentDays}`);
    console.log(`Remaining Days: ${remainingDays}`);

    setTotalDuration(totalDays);
    setCurrentDuration(currentDays);
    setDaysRemaining(remainingDays);
  }, [dataHoraInicio, dataHoraFim]);

  const maxLimitDays = 30;
  const greenBarWidth = (totalDuration / maxLimitDays) * 95;

  let grayBarWidth;
  if (currentDuration >= totalDuration) {
    grayBarWidth = 100;
  } else if (currentDuration < 0) {
    grayBarWidth = 0;
  } else {
    grayBarWidth = ((totalDuration - daysRemaining) / totalDuration) * 100;
  }

  console.log("cur dur: " + currentDuration);

  const dateInicio = format(
    parse(dataHoraInicio, "dd/MM/yyyy HH:mm:ss", new Date()),
    "dd/MM"
  );
  const dateFim = format(
    parse(dataHoraFim, "dd/MM/yyyy HH:mm:ss", new Date()),
    "dd/MM"
  );

  const getBarColor = () => {
    if (daysRemaining > totalDuration) {
      return "hidden";
    } else if (daysRemaining >= 5) {
      return "bg-green-500";
    } else if (daysRemaining < 5 && daysRemaining > 2) {
      return "bg-orange-400";
    } else if (daysRemaining <= 2) {
      return "bg-red-500";
    }
    return "bg-green-500"; // Default color
  };

  const renderMarkers = () => {
    const markers = [];
    const markerInterval = 1;

    for (let i = 1; i <= totalDuration; i += markerInterval) {
      const leftPosition = (i / totalDuration) * 100;
      markers.push(
        <div
          key={i}
          className="absolute h-full border-l-2 border-white"
          style={{ left: `${leftPosition}%` }}
        ></div>
      );
    }

    return markers;
  };

  return (
    <div className="flex flex-col items-center my-2 pb-16 w-[60rem] min-h-[12rem] bg-white border shadow-md rounded-lg relative">
      <div className="flex flex-row justify-between">
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
                <h1 className="text-md font-semibold text-center">
                  {item.nome}
                </h1>
              </div>
            ))}
          </div>
        </div>
        <div className="w-px bg-gray-300 mx-2 my-4"></div> {/*separator*/}
        <div className="p-4 w-[28rem]">
          <h2 className="text-xl font-bold mb-2">Detalhes</h2>
          <ul className="text-gray-500 dark:text-gray-400">
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
      <div
        className={`h-4 ${getBarColor()} rounded-full bottom-12 absolute`}
        style={{ width: `${greenBarWidth}%` }}
      >
        {daysRemaining <= totalDuration && (
          <>
            <h3 className="bottom-0 left-[-45px] text-gray-500 text-xs font-medium absolute">{`${dateInicio}`}</h3>
            <h3 className="bottom-0 right-[-45px] text-gray-500 text-xs font-medium absolute">{`${dateFim}`}</h3>
          </>
        )}

        <div
          className={`h-4 ${
            daysRemaining < 0 ? "bg-gray-300" : "bg-slate-600"
          } ${
            grayBarWidth >= 100 ? "rounded-full" : "rounded-l-full"
          }  bottom-0 left-0 absolute`}
          style={{ width: `${grayBarWidth}%` }}
        ></div>
        {renderMarkers()}
      </div>

      {daysRemaining > totalDuration ? (
        <h1 className="bottom-7 font-semibold text-gray-400 absolute">{`${
          daysRemaining - totalDuration === 1
            ? "Equipamento disponível em "
            : "Equipamento disponível em "
        }${daysRemaining - totalDuration} ${
          daysRemaining - totalDuration === 1 ? " dia" : " dias"
        }`}</h1>
      ) : daysRemaining < 0 ? (
        <h1 className="bottom-4 font-semibold text-gray-400 absolute">{`Tempo encerrado`}</h1>
      ) : (
        <h1 className="bottom-4 font-semibold absolute">{`${daysRemaining} ${
          daysRemaining === 1 ? "dia restante" : "dias restantes"
        }`}</h1>
      )}
    </div>
  );
};

export default Emprestimo;
