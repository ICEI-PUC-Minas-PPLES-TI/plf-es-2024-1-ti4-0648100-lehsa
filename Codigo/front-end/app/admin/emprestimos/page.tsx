"use client";

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Emprestimo as EmprestimoType } from "@/components/types"; // Assuming Tecnico type is imported here
import Emprestimo from "@/components/Emprestimo";
import Link from "next/link";

const Emprestimos = () => {
  const [emprestimos, setEmprestimos] = useState<EmprestimoType[]>([]);

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    fetch("http://localhost:8080/emprestimo", {
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
        console.error("Error fetching emprestimos:", error);
      });
  }, []);

  return (
    <div className="flex flex-col items-center p-4">
      {emprestimos.map((emprestimo) => (
        <Link key={emprestimo.id} href={`/admin/emprestimos/${emprestimo.id}`}>
          <div className="block mb-4 cursor-pointer"></div>
          <Emprestimo
            key={emprestimo.id}
            items={emprestimo.itens}
            dataHoraFim={emprestimo.dataHoraFim}
            dataHoraInicio={emprestimo.dataHoraInicio}
            solicitante={emprestimo.solicitante}
            observacaoSolicitacao={emprestimo.observacaoSolicitacao}
            statusTransacaoItem={emprestimo.statusTransacaoItem}
          />
        </Link>
      ))}
    </div>
  );
};

export default Emprestimos;
