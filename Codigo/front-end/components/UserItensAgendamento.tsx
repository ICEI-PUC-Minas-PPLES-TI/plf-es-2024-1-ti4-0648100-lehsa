"use client";

import React, { useState } from "react";
import SearchBar from "@/components/SearchBar";
import ItensDisplay from "@/app/user/ItensDisplay";

const UserItensAgendamento = () => {
  const [searchTerm, setSearchTerm] = useState("");

  return (
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
  );
};

export default UserItensAgendamento;
