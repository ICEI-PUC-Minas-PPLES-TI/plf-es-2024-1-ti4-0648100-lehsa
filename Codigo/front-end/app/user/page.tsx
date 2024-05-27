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
import UserTabs from "@/components/UserTabs";
import UserAgendamentos from "@/components/UserAgendamentos";
import UserEmprestimos from "@/components/UserEmprestimos";

const UserPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);
  const [activeTab, setActiveTab] = useState<"Agendamentos" | "Emprestimos">(
    "Agendamentos"
  );

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
    <div className="max-w-7xl m-auto space-y-5">
      <TopMenu title="Dashboard" />
      <div className="flex flex-col space-x-5">
        <UserTabs activeTab={activeTab} setActiveTab={setActiveTab} />
        {activeTab === "Agendamentos" ? (
          <UserAgendamentos />
        ) : (
          <UserEmprestimos />
        )}
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
  );
};

export default UserPage;
