"use client"

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Agendamento as AgendamentoType} from "@/components/types";
import Agendamento from "@/components/Agendamento";
import Link from "next/link";

const Agendamentos = () => {
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/agendamento`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
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
  }, []);

  return (
    <div className="flex flex-col items-center p-4">
      {agendamentos.map((agendamento) => (
        <Link key={agendamento.id} href={`/admin/agendamentos/${agendamento.id}`}>
          <div className="block mb-4 cursor-pointer">
            <Agendamento
              items={agendamento.itens}
              tecnico={agendamento.tecnico}
              professor={agendamento.professor}
              solicitantes={agendamento.solicitantes}
              dataHoraFim={agendamento.dataHoraFim}
              dataHoraInicio={agendamento.dataHoraInicio}
              observacaoSolicitacao={agendamento.observacaoSolicitacao}
              statusTransacaoItem={agendamento.statusTransacaoItem}
            />
          </div>
        </Link>
      ))}
    </div>
  );
};

export default Agendamentos;
