import FilterSelect from "@/components/FilterSelect";
import SearchBar from "@/components/SearchBar";
import TopMenu from "@/components/topMenu";
import UserRow from "@/components/UserRow";
import React from "react";

const Usuarios = () => {
  return (
    <div className="">
      <TopMenu title="Usuários" />

      <div className='pl-72 pr-14 flex flex-1 justify-between my-5'>
            <SearchBar />
            <div className='flex justify-items-end space-x-5'>
                <FilterSelect />
            </div>
        </div>
      <UserRow />
    </div>
  );
};

export default Usuarios;
