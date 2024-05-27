"use client";
import React, { useState } from "react";
import Cookie from "js-cookie";
import TopMenu from "@/components/topMenu";
import { jwtDecode } from "jwt-decode";
import UserTabs from "@/components/UserTabs";
import UserAgendamentos from "@/components/UserAgendamentos";
import UserEmprestimos from "@/components/UserEmprestimos";
import UserItensAgendamento from "@/components/UserItensAgendamento";
import UserItensEmprestimo from "@/components/UserItensEmprestimos";

const UserPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<"Agendamentos" | "Emprestimos">(
    "Agendamentos"
  );

  const token = Cookie.get("token");
  let decoded: any = {};
  if (token) {
    decoded = jwtDecode(token);
  }

  return (
    <div className="max-w-7xl m-auto space-y-5">
      <TopMenu title="Dashboard" />
      <div className="flex flex-col">
        <UserTabs activeTab={activeTab} setActiveTab={setActiveTab} />
        {activeTab === "Agendamentos" ? (
          <UserAgendamentos />
        ) : (
          <UserEmprestimos />
        )}
      </div>
      {activeTab === "Agendamentos" ? (
        <UserItensAgendamento />
      ) : (
        <UserItensEmprestimo />
      )}
    </div>
  );
};

export default UserPage;
