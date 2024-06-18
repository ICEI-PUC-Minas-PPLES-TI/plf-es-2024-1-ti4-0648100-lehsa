"use client";

import SearchBar from "@/components/SearchBar";
import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import SingleItemCard from "@/components/SingleItemCard";
import Link from "next/link";
import { jwtDecode } from "jwt-decode";

type Props = {
  id: string;
  img: File;
  nome: string;
  quantidade: number;
  tipo_item: string;
  emprestavel: boolean;
};

const UserItensEmprestimo = () => {
  const [searchTerm, setSearchTerm] = useState("");

  const [items, setItems] = useState<Props[]>([]);

  const token = Cookie.get("token");
  let decoded = "";
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_API_URL}/item/emprestaveis`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setItems(data);
      })
      .catch((error) => console.error("Error fetching items:", error));
  }, []);

  const filteredItems = items.filter((item) =>
    item.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="w-full bg-white rounded-2xl p-5">
      <h2 className="font-semibold text-xl mb-6">
        Itens disponíveis para empréstimo
      </h2>
      <div className="flex flex-1 justify-between my-5">
        <SearchBar onChange={(e) => setSearchTerm(e.target.value)} />
        <div className="flex justify-items-end space-x-5">
          {/* <FilterSelect /> */}
        </div>
      </div>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {filteredItems.map((item: Props) => (
          <Link href={`/user/emprestimo/${item.id}`} key={item.id}>
            <SingleItemCard
              id={item.id}
              nome={item.nome}
              tipo_item={item.tipo_item}
              quantidade={item.quantidade}
              emprestavel={item.emprestavel}
            />
          </Link>
        ))}
      </div>
    </div>
  );
};

export default UserItensEmprestimo;
