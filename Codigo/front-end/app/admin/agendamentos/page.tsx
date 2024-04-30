"use client"

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Agendamento as AgendamentoType, Tecnico } from "@/components/types"; // Assuming Tecnico type is imported here
import Agendamento from "@/components/Agendamento";

const Agendamentos = () => {
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    fetch("http://localhost:8080/agendamento", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("API response:", data); // Log the data to see its structure
        setAgendamentos(data);
      })
      .catch((error) => {
        console.error("Error fetching agendamentos:", error);
      });
  }, []);

  return (
    <div>
      {agendamentos.map((agendamento) => (
        <Agendamento key={agendamento.id} items={agendamento.itens} tecnico={agendamento.tecnico} dataHoraFim={agendamento.dataHoraFim} dataHoraInicio={agendamento.dataHoraInicio}/>
      ))}
    </div>
  );
};

export default Agendamentos;
