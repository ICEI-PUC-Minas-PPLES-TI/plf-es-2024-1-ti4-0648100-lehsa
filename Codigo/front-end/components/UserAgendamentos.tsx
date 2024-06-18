"use client";
import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Agendamento as AgendamentoType } from "@/components/types";
import Agendamento from "@/components/Agendamento";
import { jwtDecode } from "jwt-decode";
import { parse } from "date-fns";

const UserAgendamentos = () => {
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);
  const [error, setError] = useState<string | null>(null);

  const token = Cookie.get("token");
  let decoded: any = {};
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    if (decoded.userId) {
      fetchAgendamento();
    } else {
      setError("Invalid user ID");
    }
  }, [decoded.userId]);

  const fetchAgendamento = () => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/usuario/${decoded.userId}/agendamentos`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch agendamentos");
        }
        return response.json();
      })
      .then((data) => {
        if (Array.isArray(data)) {
          const sortedData = data.sort((a, b) => {
            const dateFormat = "dd/MM/yyyy HH:mm:ss";
            const dateA = parse(a.dataHoraInicio, dateFormat, new Date());
            const dateB = parse(b.dataHoraInicio, dateFormat, new Date());
            return dateA.getTime() - dateB.getTime();
          });
          setAgendamentos(sortedData);
        } else {
          throw new Error("Invalid data format");
        }
      })
      .catch((error) => {
        console.error("Error fetching agendamentos:", error);
        setError(error.message);
      });
  };

  return (
    <div className="w-full bg-white h-auto rounded-2xl p-5">
      <div className="flex flex-col justify-center items-center">
        {error ? (
          <div className="text-red-500">{error}</div>
        ) : (
          agendamentos.map((agendamento) => (
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
          ))
        )}
      </div>
    </div>
  );
};

export default UserAgendamentos;
