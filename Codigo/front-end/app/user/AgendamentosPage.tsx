"use client";
import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import { Agendamento as AgendamentoType } from "@/components/types";
import Agendamento from "@/components/Agendamento";
import TopMenu from "@/components/topMenu";
import SearchBar from "@/components/SearchBar";
import FilterSelect from "@/components/FilterSelect";
import ItensDisplay from "./ItensDisplay";
import { jwtDecode } from "jwt-decode";
import { EmprestimosPage } from "./EmprestimosPage"; // Import EmprestimosPage

export const AgendamentosPage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);
  const [currentPage, setCurrentPage] = useState("agendamentos"); // State to manage current page

  const token = Cookie.get("token");
  let decoded = "";
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    const authToken = Cookie.get("token") ?? "";
    fetch(`http://localhost:8080/agendamento/usuario/${decoded.sub}`, {
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
  }, [decoded.sub]);

  return (
    <>
      {currentPage === "agendamentos" ? (
        <div className="max-w-7xl m-auto space-y-5">
          <TopMenu title="Dashboard" />
          <div className="flex space-x-5">
            <div className="w-full bg-white h-auto rounded-2xl p-5">
              <h2 className="font-semibold text-xl mb-4">Seus Agendamentos</h2>
              <div className="relative">
                <button
                  className="p-2 justify-center bg-[#03B0F0] text-white rounded-xl mr-5 right-[-1rem] top-[-3rem] absolute"
                  onClick={() => setCurrentPage("emprestimos")}
                >
                  Ver Emprestimos
                </button>
              </div>
              <div className="flex flex-col justify-center">
                {agendamentos.map((agendamento) => (
                  <Agendamento
                    key={agendamento.id}
                    items={agendamento.itens}
                    dataHoraFim={agendamento.dataHoraFim}
                    dataHoraInicio={agendamento.dataHoraInicio}
                    tecnico={agendamento.tecnico}
                  />
                ))}
              </div>
            </div>
          </div>
          <div className="w-full bg-white rounded-2xl p-5">
            <h2 className="font-semibold text-xl mb-6">
              Itens disponíveis para agendamento
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
        <EmprestimosPage />
      )}
    </>
  );
};
