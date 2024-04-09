"use client";
import React, { useState } from "react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import OneIcon from "@/assets/OneIcon";
import TwoIcon from "@/assets/TwoIcon";
import ThreeIcon from "@/assets/ThreeIcon";
import Cookie from "js-cookie";
import router from "next/router";

const CadastroForm = () => {
  const [userData, setUserData] = useState({
    nome: "",
    cpf: "",
    telefone: "",
    email: "",
    password: "",
  });

  const [complete1, setComplete1] = useState(false);
  const [complete2, setComplete2] = useState(false);
  const [complete3, setComplete3] = useState(false);

  const handleClick1 = () => {
    setComplete1(true);
  };

  const handleClick2 = () => {
    setComplete2(true);
  };

  const handleClick3 = () => {
    setComplete3(true);
    createUser(userData);
  };

  const handleChange = (e: { target: { id: any; value: any } }) => {
    const { id, value } = e.target;
    setUserData({ ...userData, [id]: value });
  };

  const createUser = async (userData: { nome?: string; cpf?: string; telefone?: string; email: any; password: any; }) => {
    try {
      const response = await fetch(`http://localhost:8080/usuario`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        console.log("User created successfully");

        // Automatically log in the user
        const loginResponse = await fetch("http://localhost:8080/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: userData.email,
            password: userData.password,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          const { token, userId } = data; // Destructure both token and userId from the response
      
          // Set the token in cookies
          Cookie.set("token", token, { expires: 7 });
      
          // Optionally, store the user ID if needed
          Cookie.set("userId", userId, { expires: 7 });
      
          userId(userId); // Update state or context as needed
      
          // Redirect to admin page
          router.push("/admin");
        } else {
          window.alert("Login failed");
        }
      } else {
        console.error("Failed to create user");
        // Handle failure to create user
      }
    } catch (error) {
      console.error("Error creating user:", error);
    }
  };

  return (
    <section className="flex flex-col gap-2 items-center">
      {!complete1 ? (
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <OneIcon color={"#444"} />
            <h1 className="title">Informações básicas</h1>
          </div>

          <form className="flex flex-col gap-2 items-center">
            <div className="w-full">
              <Label htmlFor="nome">Nome</Label>
              <Input id="nome" type="string" onChange={handleChange} />
            </div>

            <div className="w-full">
              <Label htmlFor="cpf">CPF</Label>
              <Input
                placeholder="xxx.xxx.xxx-xx"
                id="cpf"
                type="cpf"
                onChange={handleChange}
              />
            </div>

            <Button
              className="mt-3 w-[12rem]"
              type="button"
              onClick={handleClick1}
            >
              Proximo
            </Button>
          </form>
        </div>
      ) : (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <OneIcon color={"#03B0F0"} />
          <h1 className="title">Informações básicas</h1>
        </div>
      )}

      {complete1 && !complete2 ? (
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <TwoIcon color={"#444444"} />
            <h1 className="title">Contato</h1>
          </div>

          <form className="flex flex-col gap-2 items-center">
            <div className="w-full">
              <Label htmlFor="telefone">Celular</Label>
              <Input id="telefone" type="number" onChange={handleChange} />
            </div>

            <Button
              className="mt-3 w-[12rem]"
              type="button"
              onClick={handleClick2}
            >
              Proximo
            </Button>
          </form>
        </div>
      ) : (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <TwoIcon color={"#03B0F0"} />
          <h1 className="title">Contato</h1>
        </div>
      )}

      {complete2 && !complete3 ? (
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <ThreeIcon color={"#444444"} />
            <h1 className="title">Login</h1>
          </div>

          <form className="flex flex-col gap-2 items-center">
            <div className="w-full">
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="text" onChange={handleChange} />
            </div>

            <div className="w-full">
              <Label htmlFor="password">Senha</Label>
              <Input id="password" type="password" onChange={handleChange} />
            </div>

            <Button
              className="mt-3 w-[12rem]"
              type="button"
              onClick={handleClick3}
            >
              Concluir Cadastro
            </Button>
          </form>
        </div>
      ) : (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <ThreeIcon color={"#03B0F0"} />
          <h1 className="title">Login</h1>
        </div>
      )}
    </section>
  );
};

export default CadastroForm;
