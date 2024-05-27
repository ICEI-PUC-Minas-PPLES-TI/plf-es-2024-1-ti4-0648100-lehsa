"use client";

import React, { useEffect, useState } from "react";
import Cookie from "js-cookie";
import SingleItemCard from "@/components/SingleItemCard";
import Link from "next/link";
import { jwtDecode } from "jwt-decode";

interface ItensCardProps {
  searchTerm: string;
}

type Props = {
  id: string;
  img: File;
  nome: string;
  quantidade: number;
  tipo_item: string;
  emprestavel: boolean;
};

const ItensDisplay = ({ searchTerm }: ItensCardProps) => {
  const [items, setItems] = useState<Props[]>([]);

  const token = Cookie.get("token");
  let decoded = "";
  if (token) {
    decoded = jwtDecode(token);
  }

  useEffect(() => {
    fetch("http://localhost:8080/item", {
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
    <div className="flex gap-4">
      {filteredItems.map((item: Props) => (
        <Link href={`/user/agendar/${item.id}`} key={item.id}>
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
  );
};

export default ItensDisplay;
