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

  const [errors, setErrors] = useState({
    nome: "",
    cpf: "",
    telefone: "",
    email: "",
    password: "",
  });

  const handleClick1 = () => {
    const cpfError = validateCPF(userData.cpf);
    if (userData.nome === "" || cpfError) {
      setErrors({
        ...errors,
        nome: userData.nome === "" ? "Nome é obrigatório" : "",
        cpf: cpfError,
      });
    } else {
      setComplete1(true);
    }
  };

  const handleClick2 = () => {
    const telefoneError = validateTelefone(userData.telefone);
    if (telefoneError) {
      setErrors({
        ...errors,
        telefone: telefoneError,
      });
    } else {
      setComplete2(true);
    }
  };

  const handleClick3 = () => {
    const emailError = validateEmail(userData.email);
    if (emailError || userData.password === "") {
      setErrors({
        ...errors,
        email: emailError,
        password: userData.password === "" ? "Senha é obrigatória" : "",
      });
    } else {
      setComplete3(true);
      createUser(userData);
    }
  };

  const handleChange = (e: { target: { id: any; value: any } }) => {
    const { id, value } = e.target;
    if (id === "cpf") {
      const formattedValue = formatCPF(value);
      setUserData({ ...userData, [id]: formattedValue });
    } else {
      setUserData({ ...userData, [id]: value });
    }
    setErrors({ ...errors, [id]: "" });
  };

  const formatCPF = (value: string) => {
    value = value.replace(/\D/g, ""); // Remove tudo o que não é dígito
    value = value.replace(/(\d{3})(\d)/, "$1.$2"); // Coloca um ponto entre o terceiro e o quarto dígitos
    value = value.replace(/(\d{3})(\d)/, "$1.$2"); // Coloca um ponto entre o sexto e o sétimo dígitos
    value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2"); // Coloca um hífen entre o nono e o décimo dígitos
    return value;
  };

  const validateCPF = (cpf: string) => {
    cpf = cpf.replace(/\D/g, ""); // Remove tudo o que não é dígito
    if (cpf.length !== 11) return "CPF deve conter 11 dígitos";
    // Adicione aqui uma validação de CPF mais robusta se necessário
    return "";
  };

  const validateTelefone = (telefone: string) => {
    const telefonePattern = /^\d{10,11}$/;
    if (!telefonePattern.test(telefone)) return "Telefone deve conter 10 ou 11 dígitos";
    return "";
  };

  const validateEmail = (email: string) => {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) return "Email inválido";
    return "";
  };

  const createUser = async (userData: {
    nome?: string;
    cpf?: string;
    telefone?: string;
    email: any;
    password: any;
  }) => {
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
        window.alert("Falha ao criar usuário. Por favor, confirme seus dados e tente novamente.");
      }
    } catch (error) {
      console.error("Error creating user:", error);
      window.alert("Erro ao criar usuário. Por favor, confirme seus dados e tente novamente.");
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
          <div onClick={() => setComplete1(!complete1)} className="flex gap-4 w-full hover:cursor-pointer">
            <OneIcon color={"#03B0F0"} />
            <h1 className="title">Informações básicas</h1>
          </div>
        )}
        <form className={!complete1 ? "flex flex-col gap-2 items-center" : "hidden"}>
          <div className="w-full">
            <Label htmlFor="nome">Nome</Label>
            <Input id="nome" type="string" onChange={handleChange} />
            {errors.nome && <p className="text-red-500 text-sm">{errors.nome}</p>}
          </div>

          <div className="w-full">
            <Label htmlFor="cpf">CPF</Label>
            <Input placeholder="xxx.xxx.xxx-xx" id="cpf" type="text" onChange={handleChange} value={userData.cpf} />
            {errors.cpf && <p className="text-red-500 text-sm">{errors.cpf}</p>}
          </div>

          <Button className="mt-3 w-[12rem]" type="button" onClick={handleClick1}>
            Próximo
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
          <div onClick={() => setComplete2(!complete2)} className="flex gap-4 w-full hover:cursor-pointer">
            <TwoIcon color={"#03B0F0"} />
            <h1 className="title">Contato</h1>
          </div>
        )}

        <form className={!complete1 || complete2 ? "hidden" : "flex flex-col gap-2 items-center"}>
          <div className="w-full">
            <Label htmlFor="telefone">Celular</Label>
            <Input id="telefone" type="number" onChange={handleChange} />
            {errors.telefone && <p className="text-red-500 text-sm">{errors.telefone}</p>}
          </div>

          <Button className="mt-3 w-[12rem]" type="button" onClick={handleClick2}>
            Próximo
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
          <div onClick={() => setComplete3(!complete3)} className="flex gap-4 w-full hover:cursor-pointer">
            <ThreeIcon color={"#03B0F0"} />
            <h1 className="title">Login</h1>
          </div>
        )}

        <form className={!complete1 || !complete2 ? "hidden" : "flex flex-col gap-2 items-center"}>
          <div className="w-full">
            <Label htmlFor="email">Email</Label>
            <Input id="email" type="text" onChange={handleChange} />
            {errors.email && <p className="text-red-500 text-sm">{errors.email}</p>}
          </div>

          <div className="w-full">
            <Label htmlFor="password">Senha</Label>
            <Input id="password" type="password" onChange={handleChange} />
            {errors.password && <p className="text-red-500 text-sm">{errors.password}</p>}
          </div>

          <Button className="mt-3 w-[12rem]" type="button" onClick={handleClick3}>
            Concluir Cadastro
          </Button>
        </form>
      </div>
    </section>
  );
};

export default CadastroForm;
