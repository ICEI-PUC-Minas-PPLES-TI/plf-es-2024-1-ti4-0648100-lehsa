"use client";

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Emprestimo as EmprestimoType } from "@/components/types"; 
import Emprestimo from "@/components/Emprestimo";
import Link from "next/link";

const Emprestimos = () => {
  const [emprestimos, setEmprestimos] = useState<EmprestimoType[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/emprestimo`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("API response:", data); 
        if (Array.isArray(data)) {
          setEmprestimos(data);
        } else {
          throw new Error("API response is not an array");
        }
      })
      .catch((error) => {
        console.error("Error fetching emprestimos:", error);
        setError(error.message);
      });
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div className="flex flex-col items-center p-4">
      {emprestimos.map((emprestimo) => (
        <Link key={emprestimo.id} href={`/admin/emprestimos/${emprestimo.id}`}>
          <div className="block mb-4 cursor-pointer">
            <Emprestimo
              key={emprestimo.id}
              items={emprestimo.itens}
              dataHoraFim={emprestimo.dataHoraFim}
              dataHoraInicio={emprestimo.dataHoraInicio}
              solicitante={emprestimo.solicitante}
              observacaoSolicitacao={emprestimo.observacaoSolicitacao}
              statusTransacaoItem={emprestimo.statusTransacaoItem}
            />
          </div>
        </Link>
      ))}
    </div>
  );
};

export default Emprestimos;
