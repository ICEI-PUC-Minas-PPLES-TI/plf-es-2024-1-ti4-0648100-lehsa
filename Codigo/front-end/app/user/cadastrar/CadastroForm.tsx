"use client";
import React, { useState } from "react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import OneIcon from "@/assets/OneIcon";
import TwoIcon from "@/assets/TwoIcon";
import ThreeIcon from "@/assets/ThreeIcon";

const CadastroForm = () => {
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
  };

  return (
    <section className="flex flex-col gap-2 items-center">
      {complete1 ? (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <OneIcon color={"#03B0F0"} />
          <h1 className="title">Informações básicas</h1>
        </div>
      ) : (
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <OneIcon color={"#444"} />
            <h1 className="title">Informações básicas</h1>
          </div>

          <form className="flex flex-col gap-2 items-center">
            <div className="w-full">
              <Label htmlFor="firstName">Primeiro nome</Label>
              <Input id="firstName" type="string" />
            </div>

            <div className="w-full">
              <Label htmlFor="lastName">Sobrenome</Label>
              <Input id="lastName" type="string" />
            </div>

            <div className="w-full">
              <Label htmlFor="cpf">CPF</Label>
              <Input placeholder="xxx.xxx.xxx-xx" id="cpf" type="cpf" />
            </div>

            <div className="w-full">
              <Label htmlFor="institution">Instituição</Label>
              <Input id="institution" type="string" />
            </div>

            <div className="w-full">
              <Label htmlFor="birthDate">Data de nascimento</Label>
              <Input id="birthDate" type="date" />
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
      )}

      {complete1 && !complete2 ? (
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <TwoIcon color={"#444444"}/>
            <h1 className="title">Contato</h1>
          </div>

          <form className="flex flex-col gap-2 items-center">
            <div className="w-full">
              <Label htmlFor="phone">Celular</Label>
              <Input id="phone" type="number" />
            </div>

            <div className="w-full">
              <Label htmlFor="cep">CEP</Label>
              <Input id="cep" type="number" />
            </div>

            <div className="w-full">
              <Label htmlFor="state">Estado</Label>
              <Input id="state" type="text" />
            </div>

            <div className="w-full">
              <Label htmlFor="city">Cidade</Label>
              <Input id="city" type="text" />
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
      ) : complete1 && complete2 ? (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <TwoIcon color={"#03B0F0"}/>
          <h1 className="title">Contato</h1>
        </div>
      ) : (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <TwoIcon color={"#444444"}/>
          <h1 className="title">Contato</h1>
        </div>
      )}

      {complete2 && !complete3 ? (
        <div className="bg-white w-full px-10 py-5 rounded-xl">
          <div className="flex gap-4 w-full py-3">
            <ThreeIcon color={"#444444"}/>
            <h1 className="title">Login</h1>
          </div>

          <form className="flex flex-col gap-2 items-center">
            <div className="w-full">
              <Label htmlFor="email">Email</Label>
              <Input id="email" type="text" />
            </div>

            <div className="w-full">
              <Label htmlFor="password">Senha</Label>
              <Input id="password" type="text" />
            </div>

            <div className="w-full">
              <Label htmlFor="password">Confirmar Senha</Label>
              <Input id="password" type="text" />
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
      ) : complete1 && complete2 ? (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <ThreeIcon color={"#03B0F0"}/>
          <h1 className="title">Contato</h1>
        </div>
      ) : (
        <div className="flex items-center gap-4 bg-white w-full px-10 py-3 rounded-xl">
          <ThreeIcon color={"#444444"}/>
          <h1 className="title">Contato</h1>
        </div>
      )}
    </section>
  );
};

export default CadastroForm;
