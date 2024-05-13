"use client"

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Emprestimo as EmprestimoType, Tecnico } from "@/components/types"; // Assuming Tecnico type is imported here
import Emprestimo from "@/components/Emprestimo";
import Link from "next/link";

const Emprestimos = () => {
  const [agendamentos, setEmprestimos] = useState<EmprestimoType[]>([]);

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
        setEmprestimos(data);
      })
      .catch((error) => {
        console.error("Error fetching agendamentos:", error);
      });
  }, []);

  return (
    <div>
      {agendamentos.map((agendamento) => (
        <Link href={`/admin/agendamentos/${agendamento.id}`}><Emprestimo key={agendamento.id} items={agendamento.itens} tecnico={agendamento.tecnico} dataHoraFim={agendamento.dataHoraFim} dataHoraInicio={agendamento.dataHoraInicio} /></Link>
      ))}
    </div>
  );
};

export default Emprestimos;
