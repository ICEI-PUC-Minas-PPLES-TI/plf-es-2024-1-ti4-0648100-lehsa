"use client";
import React, { useState } from "react";
import FilterSelect from "@/components/FilterSelect";
import SearchBar from "@/components/SearchBar";
import TopMenu from "@/components/topMenu";
import UserRow from "@/components/UserRow";

const Usuarios = () => {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <div className="">
      <TopMenu title="Usuários" />

      <div className="ml-[10%] flex flex-1 justify-between my-5">
        <SearchBar onChange={(e) => setSearchTerm(e.target.value)} />
        {/* <div className="flex justify-items-end space-x-5">
          <FilterSelect selectItems={selectItems} />
        </div> */}
      </div>
      <UserRow searchTerm={searchTerm} />
    </div>
  );
};

export default Usuarios;
