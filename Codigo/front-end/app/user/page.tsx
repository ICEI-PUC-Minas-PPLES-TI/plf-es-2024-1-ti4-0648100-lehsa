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
import { AgendamentosPage } from "./AgendamentosPage";
import { EmprestimosPage } from "./EmprestimosPage";

const UserPage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [agendamentos, setAgendamentos] = useState<AgendamentoType[]>([]);
  const [currentPage, setCurrentPage] = useState("agendamentos");

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
        <AgendamentosPage />
      ) : (
        <EmprestimosPage />
      )}
    </>
  );
};

export default UserPage;
