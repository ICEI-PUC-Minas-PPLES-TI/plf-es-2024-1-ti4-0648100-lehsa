import React from "react";
import AgendamentoInfoRow from "./AgendamentoInfoRow";

const Agendamento = () => {
  return (
    <div className="w-[34rem] h-[10rem] bg-secondary rounded-[20px] relative">
      <div className="flex w-full h-[3rem] bg-primary rounded-t-[20px] items-center z-10">
        <h1 className="ml-[9.5rem] text-white text-lg">Nome do equipamento</h1>
      </div>
      <div className="flex flex-row pt-1 pl-1">
        <div className="w-[8rem] h-[8rem] bg-gray-200 rounded-[10px] absolute left-[1rem] top-[1rem] z-0"></div>
        <div className="pl-[8rem]">
          <AgendamentoInfoRow searchTerm={""} />
        </div>
      </div>
    </div>
  );
};

export default Agendamento;
