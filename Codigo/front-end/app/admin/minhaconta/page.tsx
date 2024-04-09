"use client";
import React, { useState, useEffect, FormEvent } from "react";
import TopMenu from "@/components/topMenu";
import { Button } from "@/components/ui/button";
import { Label } from "@radix-ui/react-label";
import { Input } from "@/components/ui/input";
import Cookie from "js-cookie";
import { useRouter } from "next/navigation";

interface User {
  id: string;
  nome: string;
  email: string;
  telefone: string;
  password: string;
  cpf: string;
  perfil_usuario: number;
}

const MinhaConta = () => {
  const [userData, setUserData] = useState<User>({
    id: "",
    nome: "",
    email: "",
    telefone: "",
    password: "",
    cpf: "",
    perfil_usuario: 0,
  });
  const router = useRouter();

  useEffect(() => {
    const authToken = Cookie.get("token");

    if (!authToken) {
      // No token found, redirect to login
      router.push("/login");
      return;
    }

    // Decode token to get userId
    const decodedToken = decodeToken(authToken);
    if (!decodedToken || !decodedToken.userId) {
      // Token is invalid or doesn't contain userId, redirect to login
      router.push("/login");
      return;
    }

    // Fetch user data using userId from the decoded token
    fetchUserData(decodedToken.userId, authToken);
  }, []); // Empty dependency array means this runs once on component mount

  const fetchUserData = async (userId: string, authToken: string) => {
    try {
      const response = await fetch(`http://localhost:8080/usuario/${userId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${authToken}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error("Error fetching user data");
      }

      const data = await response.json();
      setUserData(data);
    } catch (error) {
      console.error("Error fetching user data:", error);
      // Optionally, handle user feedback or redirection here
    }
  };

  const decodeToken = (token: string) => {
    try {
      const decodedToken = JSON.parse(atob(token.split(".")[1]));
      return decodedToken;
    } catch (error) {
      console.error("Error decoding token:", error);
      return null;
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const authToken = Cookie.get("token");
    if (!authToken) {
      // No token found, redirect to login
      router.push("/login");
      return;
    }

    try {
      const userDataToSend = {
        nome: userData.nome,
        email: userData.email,
        password: userData.password, // assuming you have a password field in userData
        telefone: userData.telefone,
        cpf: userData.cpf,
        perfil_usuario: userData.perfil_usuario,
      };

      const response = await fetch(
        `http://localhost:8080/usuario/${userData.id}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${authToken}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(userDataToSend),
        }
      );

      if (!response.ok) {
        throw new Error("Error updating user data");
      }

      console.log("User data updated successfully");
    } catch (error) {
      console.error("Error updating user data:", error);
      // Optionally, handle user feedback or redirection here
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUserData({
      ...userData,
      [e.target.id]: e.target.value,
    });
  };

  return (
    <div>
      <TopMenu title="Minha Conta" />
      <div className="flex flex-col gap-2 mt-10 items-center">
        <div className="bg-white w-[45rem] px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <h1 className="title">Meus Dados</h1>
          </div>

          <form
            className="flex flex-col gap-2 items-center"
            onSubmit={handleSubmit}
          >
            <div className="w-full">
              <Label htmlFor="nome">Nome</Label>
              <Input
                id="nome"
                type="text"
                value={userData.nome}
                onChange={handleChange}
              />
            </div>

            <div className="w-full">
              <Label htmlFor="telefone">Celular</Label>
              <Input
                id="telefone"
                type="text"
                value={userData.telefone}
                onChange={handleChange}
              />
            </div>

            {/* <div className="w-full">
              <Label htmlFor="cpf">CPF</Label>
              <Input id="cpf" type="text" value={userData.cpf} readOnly />
            </div> */}

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
