"use client";
import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Agendamento as AgendamentoType } from "@/components/types";
import Agendamento from "@/components/Agendamento";
import { jwtDecode } from "jwt-decode";

const UserAgendamentos = () => {
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);

  const token = Cookie.get("token");
  let decoded: any = {};
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    fetchAgendamento();
  }, []);

  const fetchAgendamento = () => {
    fetch(`http://localhost:8080/usuario/${decoded.userId}/agendamentos`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("API response:", data);
        setAgendamentos(data);
      })
      .catch((error) => {
        console.error("Error fetching agendamentos:", error);
      });
  };

  return (
    
    <div className="w-full bg-white h-auto rounded-2xl p-5">
      <div className="flex flex-col justify-center items-center">
        {agendamentos.map((agendamento) => (
          <Agendamento
            key={agendamento.id}
            items={agendamento.itens}
            tecnico={agendamento.tecnico}
            professor={agendamento.professor}
            solicitantes={agendamento.solicitantes}
            dataHoraFim={agendamento.dataHoraFim}
            dataHoraInicio={agendamento.dataHoraInicio}
            observacaoSolicitacao={agendamento.observacaoSolicitacao}
            statusTransacaoItem={agendamento.statusTransacaoItem}
          />
        ))}
      </div>
    </div>
  );
};

export default UserAgendamentos;
