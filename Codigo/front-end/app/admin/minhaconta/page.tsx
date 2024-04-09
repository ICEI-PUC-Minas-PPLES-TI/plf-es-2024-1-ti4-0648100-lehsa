"use client";
import React, { useState, useEffect } from "react";
import TopMenu from "@/components/topMenu";
import { Button } from "@/components/ui/button";
import { Label } from "@radix-ui/react-label";
import { Input } from "@/components/ui/input";

interface User {
  id: string; // assuming user ID is available
  nome: string;
  email: string;
  telefone: string;
}

const MinhaConta = () => {
  const [token, setToken] = useState<string>("");
  const [userData, setUserData] = useState<User>({
    id: "",
    nome: "",
    email: "",
    telefone: "",
  });

  useEffect(() => {
    const authToken =
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJyb2xlIjoiYWRtaW4iLCJleHAiOjE3MTI5MDc3NDR9._Cc-50MEOLi9vIFk2lNxS0hhL6QujjOQVyWpYqeCewwGSF9VuK2GOYMN74nYrP_GmqcPqXws6eaUnFMm4vk0Mw";
    setToken(authToken);

    // Assuming you have access to the current user's ID
    const userId = "119563c0-fc95-4cf2-b9e1-e18a53df5707"; // Replace "user_id_here" with actual user ID
    fetch(`http://localhost:8080/usuario/${userId}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data: User) => {
        setUserData(data);
      })
      .catch((error) => {
        console.error("Error fetching user data:", error);
      });
  }, []);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Add form submission logic here
  };

  return (
    <div>
      <TopMenu title="Minha Conta" />
      <div className="flex flex-col gap-2 mt-10 items-center">
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <h1 className="title">Meus Dados</h1>
          </div>

          <form
            className="flex flex-col gap-2 items-center"
            onSubmit={handleSubmit}
          >
            <div className="w-full">
              <Label htmlFor="firstName">Nome</Label>
              <Input id="nome" type="text" value={userData.nome} readOnly />
            </div>

            <div className="w-full">
              <Label htmlFor="institution">Email</Label>
              <Input id="email" type="text" value={userData.email} readOnly />
            </div>

            <div className="w-full">
              <Label htmlFor="cpf">Celular</Label>
              <Input
                id="telefone"
                type="text"
                value={userData.telefone}
                readOnly
              />
            </div>

            <Button className="mt-3 w-[12rem]" type="submit">
              Salvar
            </Button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default MinhaConta;
