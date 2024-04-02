"use client";
import React, { useState } from "react";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

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
    <section className="">
      {complete1 ? (
        <h1 className="title">1 Informacoes basicas</h1>
      ) : (
        <div>
          <h1 className="title">1 Informacoes basicas</h1>
          <form>
            <Label htmlFor="firstName">Primeiro nome</Label>
            <Input id="firstName" type="string" />

            <Label htmlFor="lastName">Sobrenome</Label>
            <Input id="lastName" type="string" />

            <Label htmlFor="cpf">CPF</Label>
            <Input placeholder="xxx.xxx.xxx-xx" id="cpf" type="cpf" />

            <Label htmlFor="institution">Instituicao</Label>
            <Input id="institution" type="string" />

            <Label htmlFor="birthDate">Data de nascimento</Label>
            <Input id="birthDate" type="date" />
            <button type="button" onClick={handleClick1}>
              Proximo
            </button>
          </form>
        </div>
      )}

      {complete1 && !complete2 ? (
        <div>
          <h1 className="title">2 Contato</h1>
          <form>
            <Label htmlFor="phone">Celular</Label>
            <Input id="phone" type="number" />

            <Label htmlFor="cep">CEP</Label>
            <Input id="cep" type="number" />

            <Label htmlFor="state">Estado</Label>
            <Input id="state" type="text" />

            <Label htmlFor="city">Cidade</Label>
            <Input id="city" type="text" />
            <button type="button" onClick={handleClick2}>
              Proximo
            </button>
          </form>
        </div>
      ) : (
        <h1>2 Contato</h1>
      )}

      {complete2 && !complete3 ? (
        <div>
          <h1 className="title">3 Login</h1>
          <form>
            <Label htmlFor="email">Email</Label>
            <Input id="email" type="text" />

            <Label htmlFor="password">Senha</Label>
            <Input id="password" type="number" />

            <Label htmlFor="password">Confirmar Senha</Label>
            <Input id="password" type="text" />
            <button type="button" onClick={handleClick3}>
              Concluir Cadastro
            </button>
          </form>
        </div>
      ) : (
        <h1>3 Login</h1>
      )}
    </section>
  );
};

export default CadastroForm;
