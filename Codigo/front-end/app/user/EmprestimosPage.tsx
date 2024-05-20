"use client";
import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Emprestimo as EmprestimoType } from "@/components/types";
import Emprestimo from "@/components/Emprestimo";
import TopMenu from "@/components/topMenu";
import SearchBar from "@/components/SearchBar";
import FilterSelect from "@/components/FilterSelect";
import ItensDisplay from "./ItensDisplay";
import { jwtDecode } from "jwt-decode";
import { AgendamentosPage } from "./AgendamentosPage";

export const EmprestimosPage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [emprestimos, setEmprestimos] = useState<EmprestimoType[]>([]);
  const [currentPage, setCurrentPage] = useState("emprestimos");

  const token = Cookie.get("token");
  let decoded = "";
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    fetch(`http://localhost:8080/emprestimo/usuario/${decoded.sub}`, {
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
    <>
      {currentPage === "emprestimos" ? (
        <div className="max-w-7xl m-auto space-y-5">
          <TopMenu title="Dashboard" />
          <div className="flex space-x-5">
            <div className="w-full bg-white h-auto rounded-2xl p-5">
              <h2 className="font-semibold text-xl mb-4">Seus Emprestimos</h2>
              <div className="relative">
                <button
                  className="p-2 justify-center bg-[#03B0F0] text-white rounded-xl mr-5 right-[-1rem] top-[-3rem] absolute"
                  onClick={() => setCurrentPage("agendamentos")}
                >
                  Ver Agendamentos
                </button>
              </div>
              <div className="flex flex-col justify-center">
                {/* {emprestimos.map((emprestimo) => (
              <Emprestimo
                key={emprestimo.id}
                items={emprestimo.itens}
                dataHoraFim={emprestimo.dataHoraFim}
                dataHoraInicio={emprestimo.dataHoraInicio}
                tecnico={emprestimo.tecnico}
              />
            ))} */}
              </div>
            </div>
          </div>
          <div className="w-full bg-white rounded-2xl p-5">
            <h2 className="font-semibold text-xl mb-6">
              Itens disponíveis para emprestimo
            </h2>
            <div className="flex flex-1 justify-between my-5">
              <SearchBar onChange={(e) => setSearchTerm(e.target.value)} />
              <div className="flex justify-items-end space-x-5">
                {/* <FilterSelect /> */}
              </div>
            </div>
            <ItensDisplay searchTerm={searchTerm} />
          </div>
        </div>
      ) : (
        <AgendamentosPage />
      )}
    </>
  );
};
