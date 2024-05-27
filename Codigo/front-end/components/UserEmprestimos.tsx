"use client";
import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import Emprestimo from "./Emprestimo";
import { Emprestimo as EmprestimoType } from "@/components/types";
import { jwtDecode } from "jwt-decode";

const UserEmprestimos = () => {
  const [emprestimos, setEmprestimos] = useState<EmprestimoType[]>([]);
  const [error, setError] = useState<string | null>(null);

  const token = Cookie.get("token");
  let decoded: any = {};
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    if (decoded.userId) {
      fetchEmprestimo();
    } else {
      setError("Invalid user ID");
    }
  }, [decoded.userId]);

  const fetchEmprestimo = () => {
    fetch(`http://localhost:8080/usuario/${decoded.userId}/emprestimos`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch emprestimos");
        }
        return response.json();
      })
      .then((data) => {
        if (Array.isArray(data)) {
          setEmprestimos(data);
        } else {
          throw new Error("Invalid data format");
        }
      })
      .catch((error) => {
        console.error("Error fetching emprestimos:", error);
        setError(error.message);
      });
  };

  return (
    <div className="w-full bg-white h-auto rounded-2xl p-5">
      <div className="flex flex-col justify-center items-center">
        {error ? (
          <div className="text-red-500">{error}</div>
        ) : (
          emprestimos.map((emprestimo) => (
            <Emprestimo
              key={emprestimo.id}
              items={emprestimo.itens}
              dataHoraFim={emprestimo.dataHoraFim}
              dataHoraInicio={emprestimo.dataHoraInicio}
              solicitante={emprestimo.solicitante}
              observacaoSolicitacao={emprestimo.observacaoSolicitacao}
              statusTransacaoItem={emprestimo.statusTransacaoItem}
            />
          ))
        )}
      </div>
    </div>
  );
};

export default UserEmprestimos;
