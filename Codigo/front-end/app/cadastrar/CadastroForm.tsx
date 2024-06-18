"use client";
import { useRouter } from "next/navigation";
import Cookie from "js-cookie";
import React, { useState } from "react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import OneIcon from "@/assets/OneIcon";
import TwoIcon from "@/assets/TwoIcon";
import ThreeIcon from "@/assets/ThreeIcon";

const CadastroForm = () => {
  const router = useRouter();
  const [userId, setUserId] = useState<string>("");
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

  const toggleComplete1 = () => {
    setComplete1(!complete1);
  };

  const toggleComplete2 = () => {
    setComplete2(!complete2);
  };

  const toggleComplete3 = () => {
    setComplete3(!complete3);
  };

  const handleChange = (e: { target: { id: any; value: any } }) => {
    const { id, value } = e.target;
    setUserData({ ...userData, [id]: value });
  };

  const createUser = async (userData: {
    nome?: string;
    cpf?: string;
    telefone?: string;
    email: any;
    password: any;
  }) => {
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/usuario`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        console.log("User created successfully");

        const loginResponse = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/login`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: userData.email,
            password: userData.password,
          }),
        });

        if (loginResponse.ok) {
          const data = await loginResponse.json();
          const { token, userId } = data;
          Cookie.set("token", token, { expires: 7 });
          setUserId(userId);
          if (typeof window !== "undefined") {
            router.push("/admin");
          }
        } else {
          window.alert("Login failed");
        }
      } else {
        console.error("Failed to create user");
      }
    } catch (error) {
      console.error("Error creating user:", error);
    }
  };

  return (
    <section className="flex flex-col gap-2 items-center">
      <div className="bg-white w-full px-10 py-3 rounded-xl">
        {!complete1 ? (
          <div className="flex gap-4 w-full py-3">
            <OneIcon color={"#444"} />
            <h1 className="title">Informações básicas</h1>
          </div>
        ) : (
          <div onClick={toggleComplete1} className="flex gap-4 w-full hover:cursor-pointer">
            <OneIcon color={"#03B0F0"} />
            <h1 className="title">Informações básicas</h1>
          </div>
        )}
        <form
          className={!complete1 ? "flex flex-col gap-2 items-center" : "hidden"}
        >
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

      <div className="bg-white w-full px-10 py-3 rounded-xl">
        {!complete2 || !complete1 ? (
          <div className="flex gap-4 w-full">
            <TwoIcon color={"#444444"} />
            <h1 className="title">Contato</h1>
          </div>
        ) : (
          <div onClick={toggleComplete2} className="flex gap-4 w-full hover:cursor-pointer">
            <TwoIcon color={"#03B0F0"} />
            <h1 className="title">Contato</h1>
          </div>
        )}

        <form
          className={!complete1 || complete2 ? "hidden" : "flex flex-col gap-2 items-center"}
        >
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

      <div className="bg-white w-full px-10 py-3 rounded-xl">
        {!complete3 || !complete2 || !complete1 ? (
          <div className="flex gap-4 w-full">
            <ThreeIcon color={"#444444"} />
            <h1 className="title">Login</h1>
          </div>
        ) : (
          <div className="flex gap-4 w-full">
            <ThreeIcon color={"#03B0F0"} />
            <h1 className="title">Login</h1>
          </div>
        )}

        <form
          className={!complete1 || !complete2 ? "hidden" : "flex flex-col gap-2 items-center"}
        >
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
    </section>
  );
};

export default CadastroForm;
