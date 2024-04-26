"use client";

import Agendamento from "@/components/Agendamento";
import FilterSelect from "@/components/FilterSelect";
import ItensCard from "@/components/ItemCard";
import SearchBar from "@/components/SearchBar";
import TopMenu from "@/components/topMenu";
import React, { useState } from "react";

const UserPage = () => {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <div className="max-w-7xl m-auto space-y-5">
      <TopMenu title="Dashboard" />
      <div className="flex space-x-5">
        <div className="w-full bg-white h-auto rounded-2xl p-5">
          <h2 className="font-semibold text-xl mb-4">Seus Agendamentos</h2>
          <div className="grid gap-4 justify-center lg:grid-cols-2">
            <Agendamento />
            <Agendamento />
            <Agendamento />
          </div>
        </div>
      </div>
      <div className="w-full bg-white   rounded-2xl p-5">
        <h2 className="font-semibold text-xl mb-6">
          Itens disponíveis para agendamento
        </h2>
        <div className="flex flex-1 justify-between my-5">
          <SearchBar onChange={(e) => setSearchTerm(e.target.value)} />
          <div className="flex justify-items-end space-x-5">
            <FilterSelect />
          </div>
        </div>
        <ItensCard searchTerm={searchTerm} />
      </div>
    </div>
  );
};

export default UserPage;
